package com.api.Summit.API.service.impl;

import com.api.Summit.API.model.entities.Categoria;
import com.api.Summit.API.model.entities.Producto;
import com.api.Summit.API.model.repository.CategoriaRepository;
import com.api.Summit.API.model.repository.ProductoRepository;
import com.api.Summit.API.service.exception.BusinessException;
import com.api.Summit.API.service.exception.ResourceNotFoundException;
import com.api.Summit.API.service.interfaces.ProductoService;
import com.api.Summit.API.view.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    /*
    @Override
    @Transactional(readOnly = true)
    public Page<ProductoDTO> findAll(Pageable pageable) {
        try {
            log.info("Obteniendo todos los productos paginados");
            return productoRepository.findAll(pageable)
                    .map(this::convertToDTO);
        } catch (Exception e) {
            log.error("Error al obtener productos paginados: {}", e.getMessage());
            throw new BusinessException("Error al obtener la lista de productos");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDetailDTO findById(Long id) {
        try {
            log.info("Buscando producto con ID: {}", id);
            Producto producto = productoRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
            return convertToDetailDTO(producto);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al buscar producto con ID {}: {}", id, e.getMessage());
            throw new BusinessException("Error al buscar el producto");
        }
    }

    @Override
    @Transactional
    public ProductoDTO save(ProductoRequestDTO productoRequestDTO) {
        try {
            log.info("Creando nuevo producto: {}", productoRequestDTO.getNombre());

            // Validar si ya existe un producto con el mismo nombre
            if (productoRepository.existsByNombre(productoRequestDTO.getNombre())) {
                throw new BusinessException("Ya existe un producto con el nombre: " + productoRequestDTO.getNombre());
            }

            Producto producto = new Producto();
            producto.setNombre(productoRequestDTO.getNombre());
            producto.setPrecioUnitario(productoRequestDTO.getPrecioUnitario());
            producto.setCosto(productoRequestDTO.getCosto());
            producto.setCategorias(new HashSet<>());

            // Asignar categorías si se proporcionan
            if (productoRequestDTO.getCategoriasIds() != null && !productoRequestDTO.getCategoriasIds().isEmpty()) {
                for (Long categoriaId : productoRequestDTO.getCategoriasIds()) {
                    Categoria categoria = categoriaRepository.findById(categoriaId)
                            .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + categoriaId));
                    producto.addCategoria(categoria);
                }
            }

            Producto savedProducto = productoRepository.save(producto);
            log.info("Producto creado exitosamente con ID: {}", savedProducto.getId());

            return convertToDTO(savedProducto);
        } catch (BusinessException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al crear producto: {}", e.getMessage());
            throw new BusinessException("Error al crear el producto");
        }
    }

    @Override
    @Transactional
    public ProductoDTO update(Long id, ProductoRequestDTO productoRequestDTO) {
        try {
            log.info("Actualizando producto con ID: {}", id);

            Producto producto = productoRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

            // Validar si el nuevo nombre ya existe en otro producto
            if (productoRepository.existsByNombreAndIdNot(productoRequestDTO.getNombre(), id)) {
                throw new BusinessException("Ya existe otro producto con el nombre: " + productoRequestDTO.getNombre());
            }

            producto.setNombre(productoRequestDTO.getNombre());
            producto.setPrecioUnitario(productoRequestDTO.getPrecioUnitario());
            producto.setCosto(productoRequestDTO.getCosto());

            // Actualizar categorías
            if (productoRequestDTO.getCategoriasIds() != null) {
                // Limpiar categorías existentes
                producto.getCategorias().clear();

                // Agregar nuevas categorías
                for (Long categoriaId : productoRequestDTO.getCategoriasIds()) {
                    Categoria categoria = categoriaRepository.findById(categoriaId)
                            .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + categoriaId));
                    producto.addCategoria(categoria);
                }
            }

            Producto updatedProducto = productoRepository.save(producto);
            log.info("Producto actualizado exitosamente con ID: {}", id);

            return convertToDTO(updatedProducto);
        } catch (ResourceNotFoundException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al actualizar producto con ID {}: {}", id, e.getMessage());
            throw new BusinessException("Error al actualizar el producto");
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            log.info("Eliminando producto con ID: {}", id);

            Producto producto = productoRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

            // Verificar si el producto tiene conceptos de venta asociados

            if (producto.getConceptoVenta() != null && !producto.getConceptoVenta().isEmpty()) {
                throw new BusinessException("No se puede eliminar el producto porque tiene ventas asociadas");
            }

            // Limpiar relaciones con categorías antes de eliminar
            producto.getCategorias().clear();
            productoRepository.save(producto); // Guardar cambios para limpiar relaciones

            productoRepository.deleteById(id);
            log.info("Producto eliminado exitosamente con ID: {}", id);

        } catch (ResourceNotFoundException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al eliminar producto con ID {}: {}", id, e.getMessage());
            throw new BusinessException("Error al eliminar el producto");
        }
    }

    private ProductoDTO convertToDTO(Producto producto) {
        Set<CategoriaDTO> categoriasDTO = producto.getCategorias().stream()
                .map(this::convertCategoriaToDTO)
                .collect(Collectors.toSet());

        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecioUnitario(),
                producto.getCosto(),
                categoriasDTO
        );
    }

    private ProductoDetailDTO convertToDetailDTO(Producto producto) {
        Set<CategoriaDTO> categoriasDTO = producto.getCategorias().stream()
                .map(this::convertCategoriaToDTO)
                .collect(Collectors.toSet());

        var conceptosDTO = producto.getConceptoVenta().stream()
                .map(concepto -> new ConceptoDTO(
                        concepto.getId(),
                        concepto.getCantidad(),
                        concepto.getPrecioUnitario(),
                        concepto.getImporte(),
                        concepto.getVenta().getId(),
                        concepto.getProducto().getId(),
                        concepto.getProducto().getNombre(),
                        concepto.getProducto().getPrecioUnitario()
                ))
                .collect(Collectors.toList());

        return new ProductoDetailDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecioUnitario(),
                producto.getCosto(),
                categoriasDTO,
                conceptosDTO
        );


    }

    private CategoriaDTO convertCategoriaToDTO(Categoria categoria) {
        return new CategoriaDTO(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );
    }
    */

}
