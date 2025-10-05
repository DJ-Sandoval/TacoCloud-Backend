package com.api.Summit.API.view.controller;


import com.api.Summit.API.service.exception.ApiResponse;
import com.api.Summit.API.service.interfaces.CategoriaService;
import com.api.Summit.API.view.dto.CategoriaDTO;
import com.api.Summit.API.view.dto.CategoriaDetailDTO;
import com.api.Summit.API.view.dto.CategoriaRequestDTO;
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
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<CategoriaDTO>>> getAllCategorias(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<CategoriaDTO> categorias = categoriaService.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(categorias, "Categorías obtenidas exitosamente"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoriaDetailDTO>> getCategoriaById(@PathVariable Long id) {
        CategoriaDetailDTO categoria = categoriaService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(categoria, "Categoría obtenida exitosamente"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoriaDTO>> createCategoria(@Valid @RequestBody CategoriaRequestDTO categoriaRequestDTO) {
        CategoriaDTO nuevaCategoria = categoriaService.save(categoriaRequestDTO);
        return new ResponseEntity<>(ApiResponse.success(nuevaCategoria, "Categoría creada exitosamente"), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CategoriaDTO>> updateCategoria(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequestDTO categoriaRequestDTO) {
        CategoriaDTO categoriaActualizada = categoriaService.update(id, categoriaRequestDTO);
        return ResponseEntity.ok(ApiResponse.success(categoriaActualizada, "Categoría actualizada exitosamente"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCategoria(@PathVariable Long id) {
        categoriaService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Categoría eliminada exitosamente"));
    }
}