package com.api.Summit.API.service.interfaces;

import com.api.Summit.API.view.dto.InventarioDTO;
import com.api.Summit.API.view.dto.InventarioRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventarioService {
    Page<InventarioDTO> findAllByNegocioId(Long negocioId, Pageable pageable);
    InventarioDTO findByIdAndNegocioId(Long id, Long negocioId);
    InventarioDTO findByProductoIdAndNegocioId(Long productoId, Long negocioId);
    InventarioDTO saveWithNegocio(InventarioRequestDTO inventarioRequestDTO, Long negocioId);
    InventarioDTO updateWithNegocio(Long id, InventarioRequestDTO inventarioRequestDTO, Long negocioId);
    InventarioDTO updateCantidad(Long id, int nuevaCantidad, Long negocioId);
    InventarioDTO ajustarInventario(Long id, int cantidadAjuste, Long negocioId);
    void deleteByIdAndNegocioId(Long id, Long negocioId);

    // Métodos para verificar niveles de inventario
    Page<InventarioDTO> findProductosBajoStock(Long negocioId, Pageable pageable);
    Page<InventarioDTO> findProductosSobreStock(Long negocioId, Pageable pageable);
    boolean verificarStockSuficiente(Long productoId, int cantidadRequerida, Long negocioId);
}
