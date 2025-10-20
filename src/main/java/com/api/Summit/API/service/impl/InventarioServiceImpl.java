package com.api.Summit.API.service.impl;

import com.api.Summit.API.model.entities.Inventario;
import com.api.Summit.API.model.entities.Negocio;
import com.api.Summit.API.model.entities.Producto;
import com.api.Summit.API.model.repository.InventarioRepository;
import com.api.Summit.API.model.repository.NegocioRepository;
import com.api.Summit.API.model.repository.ProductoRepository;
import com.api.Summit.API.service.interfaces.InventarioService;
import com.api.Summit.API.view.dto.InventarioDTO;
import com.api.Summit.API.view.dto.InventarioRequestDTO;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;
    private final NegocioRepository negocioRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<InventarioDTO> findAllByNegocioId(Long negocioId, Pageable pageable) {
        Page<Inventario> inventarios = inventarioRepository.findByNegocioId(negocioId, pageable);
        return inventarios.map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioDTO findByIdAndNegocioId(Long id, Long negocioId) {
        Inventario inventario = inventarioRepository.findByIdAndNegocioId(id, negocioId)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con ID: " + id + " para el negocio: " + negocioId));
        return convertToDTO(inventario);
    }

    @Override
    @Transactional(readOnly = true)
    public InventarioDTO findByProductoIdAndNegocioId(Long productoId, Long negocioId) {
        Inventario inventario = inventarioRepository.findByProductoIdAndNegocioId(productoId, negocioId)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado para el producto ID: " + productoId + " en el negocio: " + negocioId));
        return convertToDTO(inventario);
    }

    @Override
    @Transactional
    public InventarioDTO saveWithNegocio(InventarioRequestDTO inventarioRequestDTO, Long negocioId) {
        // Verificar si ya existe inventario para este producto en el negocio
        if (inventarioRepository.existsByProductoIdAndNegocioId(inventarioRequestDTO.getProductoId(), negocioId)) {
            throw new RuntimeException("Ya existe un registro de inventario para este producto en el negocio");
        }

        Negocio negocio = negocioRepository.findById(negocioId)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado con ID: " + negocioId));

        Producto producto = productoRepository.findByIdAndNegocioId(inventarioRequestDTO.getProductoId(), negocioId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + inventarioRequestDTO.getProductoId() + " para el negocio: " + negocioId));

        // Validar cantidades
        validarCantidades(inventarioRequestDTO);

        Inventario inventario = Inventario.builder()
                .producto(producto)
                .cantidad(inventarioRequestDTO.getCantidad())
                .cantidadMinima(inventarioRequestDTO.getCantidadMinima())
                .cantidadMaxima(inventarioRequestDTO.getCantidadMaxima())
                .negocio(negocio)
                .build();

        Inventario inventarioGuardado = inventarioRepository.save(inventario);
        return convertToDTO(inventarioGuardado);
    }

    @Override
    @Transactional
    public InventarioDTO updateWithNegocio(Long id, InventarioRequestDTO inventarioRequestDTO, Long negocioId) {
        Inventario inventario = inventarioRepository.findByIdAndNegocioId(id, negocioId)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con ID: " + id + " para el negocio: " + negocioId));

        // Verificar si el producto ha cambiado y si ya existe inventario para el nuevo producto
        if (!inventario.getProducto().getId().equals(inventarioRequestDTO.getProductoId()) &&
                inventarioRepository.existsByProductoIdAndNegocioId(inventarioRequestDTO.getProductoId(), negocioId)) {
            throw new RuntimeException("Ya existe un registro de inventario para este producto en el negocio");
        }

        // Si el producto cambió, obtener el nuevo producto
        if (!inventario.getProducto().getId().equals(inventarioRequestDTO.getProductoId())) {
            Producto nuevoProducto = productoRepository.findByIdAndNegocioId(inventarioRequestDTO.getProductoId(), negocioId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + inventarioRequestDTO.getProductoId() + " para el negocio: " + negocioId));
            inventario.setProducto(nuevoProducto);
        }

        // Validar cantidades
        validarCantidades(inventarioRequestDTO);

        inventario.setCantidad(inventarioRequestDTO.getCantidad());
        inventario.setCantidadMinima(inventarioRequestDTO.getCantidadMinima());
        inventario.setCantidadMaxima(inventarioRequestDTO.getCantidadMaxima());

        Inventario inventarioActualizado = inventarioRepository.save(inventario);
        return convertToDTO(inventarioActualizado);
    }

    @Override
    @Transactional
    public InventarioDTO updateCantidad(Long id, int nuevaCantidad, Long negocioId) {
        Inventario inventario = inventarioRepository.findByIdAndNegocioId(id, negocioId)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con ID: " + id + " para el negocio: " + negocioId));

        if (nuevaCantidad < 0) {
            throw new RuntimeException("La cantidad no puede ser negativa");
        }

        if (nuevaCantidad > inventario.getCantidadMaxima()) {
            throw new RuntimeException("La cantidad no puede exceder el máximo permitido: " + inventario.getCantidadMaxima());
        }

        inventario.setCantidad(nuevaCantidad);
        Inventario inventarioActualizado = inventarioRepository.save(inventario);
        return convertToDTO(inventarioActualizado);
    }

    @Override
    @Transactional
    public InventarioDTO ajustarInventario(Long id, int cantidadAjuste, Long negocioId) {
        Inventario inventario = inventarioRepository.findByIdAndNegocioId(id, negocioId)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con ID: " + id + " para el negocio: " + negocioId));

        int nuevaCantidad = inventario.getCantidad() + cantidadAjuste;

        if (nuevaCantidad < 0) {
            throw new RuntimeException("No hay suficiente stock. Stock actual: " + inventario.getCantidad());
        }

        if (nuevaCantidad > inventario.getCantidadMaxima()) {
            throw new RuntimeException("El ajuste excede la cantidad máxima permitida. Máximo: " + inventario.getCantidadMaxima());
        }

        inventario.setCantidad(nuevaCantidad);
        Inventario inventarioActualizado = inventarioRepository.save(inventario);
        return convertToDTO(inventarioActualizado);
    }

    @Override
    @Transactional
    public void deleteByIdAndNegocioId(Long id, Long negocioId) {
        Inventario inventario = inventarioRepository.findByIdAndNegocioId(id, negocioId)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con ID: " + id + " para el negocio: " + negocioId));
        inventarioRepository.delete(inventario);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventarioDTO> findProductosBajoStock(Long negocioId, Pageable pageable) {
        Page<Inventario> inventarios = inventarioRepository.findByCantidadLessThanCantidadMinimaAndNegocioId(negocioId, pageable);
        return inventarios.map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventarioDTO> findProductosSobreStock(Long negocioId, Pageable pageable) {
        Page<Inventario> inventarios = inventarioRepository.findByCantidadGreaterThanCantidadMaximaAndNegocioId(negocioId, pageable);
        return inventarios.map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarStockSuficiente(Long productoId, int cantidadRequerida, Long negocioId) {
        Inventario inventario = inventarioRepository.findByProductoIdAndNegocioId(productoId, negocioId)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado para el producto ID: " + productoId));
        return inventario.getCantidad() >= cantidadRequerida;
    }

    private void validarCantidades(InventarioRequestDTO inventarioRequestDTO) {
        if (inventarioRequestDTO.getCantidadMinima() >= inventarioRequestDTO.getCantidadMaxima()) {
            throw new RuntimeException("La cantidad mínima debe ser menor que la cantidad máxima");
        }

        if (inventarioRequestDTO.getCantidad() < 0) {
            throw new RuntimeException("La cantidad no puede ser negativa");
        }

        if (inventarioRequestDTO.getCantidad() > inventarioRequestDTO.getCantidadMaxima()) {
            throw new RuntimeException("La cantidad no puede exceder la cantidad máxima");
        }
    }

    private InventarioDTO convertToDTO(Inventario inventario) {
        return InventarioDTO.builder()
                .id(inventario.getId())
                .productoId(inventario.getProducto().getId())
                .productoNombre(inventario.getProducto().getNombre())
                .cantidad(inventario.getCantidad())
                .cantidadMinima(inventario.getCantidadMinima())
                .cantidadMaxima(inventario.getCantidadMaxima())
                .negocioId(inventario.getNegocio().getId())
                .createdAt(inventario.getCreatedAt())
                .updatedAt(inventario.getUpdatedAt())
                .build();
    }
}
