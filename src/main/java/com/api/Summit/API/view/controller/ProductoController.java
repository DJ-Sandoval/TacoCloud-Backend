package com.api.Summit.API.view.controller;

import com.api.Summit.API.service.exception.ApiResponse;
import com.api.Summit.API.service.interfaces.ProductoService;
import com.api.Summit.API.view.dto.ProductoDTO;
import com.api.Summit.API.view.dto.ProductoDetailDTO;
import com.api.Summit.API.view.dto.ProductoRequestDTO;
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
@RequiredArgsConstructor
public class ProductoController {
    /*
    private final ProductoService productoService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<ProductoDTO>>> getAllProductos(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<ProductoDTO> productos = productoService.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(productos, "Productos obtenidos exitosamente"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductoDetailDTO>> getProductoById(@PathVariable Long id) {
        ProductoDetailDTO producto = productoService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(producto, "Producto obtenido exitosamente"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductoDTO>> createProducto(@Valid @RequestBody ProductoRequestDTO productoRequestDTO) {
        ProductoDTO nuevoProducto = productoService.save(productoRequestDTO);
        return new ResponseEntity<>(ApiResponse.success(nuevoProducto, "Producto creado exitosamente"), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductoDTO>> updateProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO productoRequestDTO) {
        ProductoDTO productoActualizado = productoService.update(id, productoRequestDTO);
        return ResponseEntity.ok(ApiResponse.success(productoActualizado, "Producto actualizado exitosamente"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProducto(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Producto eliminado exitosamente"));
    }

     */
}
