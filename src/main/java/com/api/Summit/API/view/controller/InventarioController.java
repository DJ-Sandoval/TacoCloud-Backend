package com.api.Summit.API.view.controller;

import com.api.Summit.API.service.exception.ApiResponse;
import com.api.Summit.API.service.interfaces.InventarioService;
import com.api.Summit.API.view.dto.InventarioDTO;
import com.api.Summit.API.view.dto.InventarioRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    // Listar inventario por negocio
    @GetMapping("/negocio/{negocioId}")
    public ResponseEntity<Page<InventarioDTO>> getInventarioByNegocio(
            @PathVariable Long negocioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<InventarioDTO> inventario = inventarioService.findAllByNegocioId(negocioId, pageable);
        return ResponseEntity.ok(inventario);
    }

    // Buscar inventario por ID y negocio
    @GetMapping("/{id}/negocio/{negocioId}")
    public ResponseEntity<InventarioDTO> getInventarioById(
            @PathVariable Long id,
            @PathVariable Long negocioId) {

        InventarioDTO inventario = inventarioService.findByIdAndNegocioId(id, negocioId);
        return ResponseEntity.ok(inventario);
    }

    // Buscar inventario por producto y negocio
    @GetMapping("/producto/{productoId}/negocio/{negocioId}")
    public ResponseEntity<InventarioDTO> getInventarioByProducto(
            @PathVariable Long productoId,
            @PathVariable Long negocioId) {

        InventarioDTO inventario = inventarioService.findByProductoIdAndNegocioId(productoId, negocioId);
        return ResponseEntity.ok(inventario);
    }

    // Crear nuevo registro de inventario
    @PostMapping("/negocio/{negocioId}")
    public ResponseEntity<InventarioDTO> createInventario(
            @Valid @RequestBody InventarioRequestDTO inventarioRequestDTO,
            @PathVariable Long negocioId) {

        InventarioDTO nuevoInventario = inventarioService.saveWithNegocio(inventarioRequestDTO, negocioId);
        return ResponseEntity.ok(nuevoInventario);
    }

    // Actualizar registro de inventario
    @PutMapping("/{id}/negocio/{negocioId}")
    public ResponseEntity<InventarioDTO> updateInventario(
            @PathVariable Long id,
            @Valid @RequestBody InventarioRequestDTO inventarioRequestDTO,
            @PathVariable Long negocioId) {

        InventarioDTO inventarioActualizado = inventarioService.updateWithNegocio(id, inventarioRequestDTO, negocioId);
        return ResponseEntity.ok(inventarioActualizado);
    }

    // Actualizar cantidad específica
    @PatchMapping("/{id}/cantidad/negocio/{negocioId}")
    public ResponseEntity<InventarioDTO> updateCantidad(
            @PathVariable Long id,
            @RequestParam int cantidad,
            @PathVariable Long negocioId) {

        InventarioDTO inventarioActualizado = inventarioService.updateCantidad(id, cantidad, negocioId);
        return ResponseEntity.ok(inventarioActualizado);
    }

    // Ajustar inventario (sumar/restar cantidad)
    @PatchMapping("/{id}/ajustar/negocio/{negocioId}")
    public ResponseEntity<InventarioDTO> ajustarInventario(
            @PathVariable Long id,
            @RequestParam int cantidad,
            @PathVariable Long negocioId) {

        InventarioDTO inventarioActualizado = inventarioService.ajustarInventario(id, cantidad, negocioId);
        return ResponseEntity.ok(inventarioActualizado);
    }

    // Obtener productos con stock bajo
    @GetMapping("/negocio/{negocioId}/bajo-stock")
    public ResponseEntity<Page<InventarioDTO>> getProductosBajoStock(
            @PathVariable Long negocioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<InventarioDTO> inventario = inventarioService.findProductosBajoStock(negocioId, pageable);
        return ResponseEntity.ok(inventario);
    }

    // Obtener productos con sobre stock
    @GetMapping("/negocio/{negocioId}/sobre-stock")
    public ResponseEntity<Page<InventarioDTO>> getProductosSobreStock(
            @PathVariable Long negocioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<InventarioDTO> inventario = inventarioService.findProductosSobreStock(negocioId, pageable);
        return ResponseEntity.ok(inventario);
    }

    // Verificar stock suficiente
    @GetMapping("/verificar-stock/producto/{productoId}/negocio/{negocioId}")
    public ResponseEntity<ApiResponse<Boolean>> verificarStock(
            @PathVariable Long productoId,
            @RequestParam int cantidad,
            @PathVariable Long negocioId) {

        boolean stockSuficiente = inventarioService.verificarStockSuficiente(productoId, cantidad, negocioId);
        return ResponseEntity.ok(ApiResponse.success(stockSuficiente, "Verificación de stock exitosa"));

    }

    // Eliminar registro de inventario
    @DeleteMapping("/{id}/negocio/{negocioId}")
    public ResponseEntity<ApiResponse<String>> deleteInventario(
            @PathVariable Long id,
            @PathVariable Long negocioId) {

        try {
            inventarioService.deleteByIdAndNegocioId(id, negocioId);
            return ResponseEntity.ok(ApiResponse.success("Registro de inventario eliminado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
