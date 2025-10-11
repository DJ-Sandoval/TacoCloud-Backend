package com.api.Summit.API.view.controller;

import com.api.Summit.API.service.exception.ApiResponse;
import com.api.Summit.API.service.interfaces.ProductoService;
import com.api.Summit.API.view.dto.ProductoDTO;
import com.api.Summit.API.view.dto.ProductoDetailDTO;
import com.api.Summit.API.view.dto.ProductoRequestDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    // Listar productos por negocio (con paginación)
    @GetMapping("/negocio/{negocioId}")
    public ResponseEntity<Page<ProductoDTO>> getProductosByNegocio(
            @PathVariable Long negocioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductoDTO> productos = productoService.findAllByNegocioId(negocioId, pageable);
        return ResponseEntity.ok(productos);
    }

    // Buscar producto por ID y negocio
    @GetMapping("/{id}/negocio/{negocioId}")
    public ResponseEntity<ProductoDTO> getProductoById(
            @PathVariable Long id,
            @PathVariable Long negocioId) {

        ProductoDTO producto = productoService.findByIdAndNegocioId(id, negocioId);
        return ResponseEntity.ok(producto);
    }

    // Crear nuevo producto en un negocio
    @PostMapping("/negocio/{negocioId}")
    public ResponseEntity<ProductoDTO> createProducto(
            @Valid @RequestBody ProductoRequestDTO productoRequestDTO,
            @PathVariable Long negocioId) {

        ProductoDTO nuevoProducto = productoService.saveWithNegocio(productoRequestDTO, negocioId);
        return ResponseEntity.ok(nuevoProducto);
    }

    // Actualizar producto en un negocio
    @PutMapping("/{id}/negocio/{negocioId}")
    public ResponseEntity<ProductoDTO> updateProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO productoRequestDTO,
            @PathVariable Long negocioId) {

        ProductoDTO productoActualizado = productoService.updateWithNegocio(id, productoRequestDTO, negocioId);
        return ResponseEntity.ok(productoActualizado);
    }

    // Buscar productos por nombre en un negocio
    @GetMapping("/negocio/{negocioId}/buscar")
    public ResponseEntity<Page<ProductoDTO>> searchProductos(
            @PathVariable Long negocioId,
            @RequestParam String nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductoDTO> productos = productoService.searchByNombreAndNegocioId(nombre, negocioId, pageable);
        return ResponseEntity.ok(productos);
    }

    // Eliminar producto de un negocio
    @DeleteMapping("/{id}/negocio/{negocioId}")
    public ResponseEntity<ApiResponse<String>> deleteProducto(
            @PathVariable Long id,
            @PathVariable Long negocioId) {

        try {
            productoService.deleteByIdAndNegocioId(id, negocioId);
            return ResponseEntity.ok(ApiResponse.success("Producto eliminado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
