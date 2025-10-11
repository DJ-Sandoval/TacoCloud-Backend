package com.api.Summit.API.view.controller;


import com.api.Summit.API.service.exception.ApiResponse;
import com.api.Summit.API.service.interfaces.CategoriaService;
import com.api.Summit.API.view.dto.CategoriaDTO;
import com.api.Summit.API.view.dto.CategoriaDetailDTO;
import com.api.Summit.API.view.dto.CategoriaRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;

    // Listar categorías por negocio (con paginación)
    @GetMapping("/negocio/{negocioId}")
    public ResponseEntity<Page<CategoriaDTO>> getCategoriasByNegocio(
            @PathVariable Long negocioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CategoriaDTO> categorias = categoriaService.findAllByNegocioId(negocioId, pageable);
        return ResponseEntity.ok(categorias);
    }

    // Buscar categoría por ID y negocio
    @GetMapping("/{id}/negocio/{negocioId}")
    public ResponseEntity<CategoriaDTO> getCategoriaById(
            @PathVariable Long id,
            @PathVariable Long negocioId) {

        CategoriaDTO categoria = categoriaService.findByIdAndNegocioId(id, negocioId);
        return ResponseEntity.ok(categoria);
    }

    // Crear nueva categoría en un negocio
    @PostMapping("/negocio/{negocioId}")
    public ResponseEntity<CategoriaDTO> createCategoria(
            @Valid @RequestBody CategoriaRequestDTO categoriaRequestDTO,
            @PathVariable Long negocioId) {

        CategoriaDTO nuevaCategoria = categoriaService.saveWithNegocio(categoriaRequestDTO, negocioId);
        return ResponseEntity.ok(nuevaCategoria);
    }

    // Actualizar categoría en un negocio
    @PutMapping("/{id}/negocio/{negocioId}")
    public ResponseEntity<CategoriaDTO> updateCategoria(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequestDTO categoriaRequestDTO,
            @PathVariable Long negocioId) {

        CategoriaDTO categoriaActualizada = categoriaService.updateWithNegocio(id, categoriaRequestDTO, negocioId);
        return ResponseEntity.ok(categoriaActualizada);
    }

    // Buscar categorías por nombre en un negocio
    @GetMapping("/negocio/{negocioId}/buscar")
    public ResponseEntity<Page<CategoriaDTO>> searchCategorias(
            @PathVariable Long negocioId,
            @RequestParam String nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CategoriaDTO> categorias = categoriaService.searchByNombreAndNegocioId(nombre, negocioId, pageable);
        return ResponseEntity.ok(categorias);
    }

    // Eliminar categoría de un negocio
    @DeleteMapping("/{id}/negocio/{negocioId}")
    public ResponseEntity<ApiResponse<String>> deleteCategoria(
            @PathVariable Long id,
            @PathVariable Long negocioId) {

        try {
            categoriaService.deleteByIdAndNegocioId(id, negocioId);
            return ResponseEntity.ok(ApiResponse.success("Categoría eliminada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

}