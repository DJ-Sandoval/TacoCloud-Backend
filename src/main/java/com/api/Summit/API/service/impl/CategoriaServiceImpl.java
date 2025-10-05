package com.api.Summit.API.service.impl;

import com.api.Summit.API.model.entities.Categoria;
import com.api.Summit.API.model.repository.CategoriaRepository;
import com.api.Summit.API.service.exception.BusinessException;
import com.api.Summit.API.service.exception.ResourceNotFoundException;
import com.api.Summit.API.service.interfaces.CategoriaService;
import com.api.Summit.API.view.dto.CategoriaDTO;
import com.api.Summit.API.view.dto.CategoriaDetailDTO;
import com.api.Summit.API.view.dto.CategoriaRequestDTO;
import com.api.Summit.API.view.dto.ProductoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaDTO> findAll(Pageable pageable) {
        try {
            log.info("Obteniendo todas las categorías paginadas");
            return categoriaRepository.findAll(pageable)
                    .map(this::convertToDTO);
        } catch (Exception e) {
            log.error("Error al obtener categorías paginadas: {}", e.getMessage());
            throw new BusinessException("Error al obtener la lista de categorías");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaDetailDTO findById(Long id) {
        try {
            log.info("Buscando categoría con ID: {}", id);
            Categoria categoria = categoriaRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
            return convertToDetailDTO(categoria);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al buscar categoría con ID {}: {}", id, e.getMessage());
            throw new BusinessException("Error al buscar la categoría");
        }
    }

    @Override
    @Transactional
    public CategoriaDTO save(CategoriaRequestDTO categoriaRequestDTO) {
        try {
            log.info("Creando nueva categoría: {}", categoriaRequestDTO.getNombre());

            // Validar si ya existe una categoría con el mismo nombre
            if (categoriaRepository.existsByNombre(categoriaRequestDTO.getNombre())) {
                throw new BusinessException("Ya existe una categoría con el nombre: " + categoriaRequestDTO.getNombre());
            }

            Categoria categoria = Categoria.builder()
                    .nombre(categoriaRequestDTO.getNombre())
                    .descripcion(categoriaRequestDTO.getDescripcion())
                    .build();

            Categoria savedCategoria = categoriaRepository.save(categoria);
            log.info("Categoría creada exitosamente con ID: {}", savedCategoria.getId());

            return convertToDTO(savedCategoria);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al crear categoría: {}", e.getMessage());
            throw new BusinessException("Error al crear la categoría");
        }
    }

    @Override
    @Transactional
    public CategoriaDTO update(Long id, CategoriaRequestDTO categoriaRequestDTO) {
        try {
            log.info("Actualizando categoría con ID: {}", id);

            Categoria categoria = categoriaRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));

            // Validar si el nuevo nombre ya existe en otra categoría
            if (categoriaRepository.existsByNombreAndIdNot(categoriaRequestDTO.getNombre(), id)) {
                throw new BusinessException("Ya existe otra categoría con el nombre: " + categoriaRequestDTO.getNombre());
            }

            categoria.setNombre(categoriaRequestDTO.getNombre());
            categoria.setDescripcion(categoriaRequestDTO.getDescripcion());

            Categoria updatedCategoria = categoriaRepository.save(categoria);
            log.info("Categoría actualizada exitosamente con ID: {}", id);

            return convertToDTO(updatedCategoria);
        } catch (ResourceNotFoundException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al actualizar categoría con ID {}: {}", id, e.getMessage());
            throw new BusinessException("Error al actualizar la categoría");
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            log.info("Eliminando categoría con ID: {}", id);

            Categoria categoria = categoriaRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));

            // Verificar si la categoría tiene productos asociados
            if (categoria.getProductos() != null && !categoria.getProductos().isEmpty()) {
                throw new BusinessException("No se puede eliminar la categoría porque tiene productos asociados");
            }

            categoriaRepository.deleteById(id);
            log.info("Categoría eliminada exitosamente con ID: {}", id);

        } catch (ResourceNotFoundException | BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al eliminar categoría con ID {}: {}", id, e.getMessage());
            throw new BusinessException("Error al eliminar la categoría");
        }
    }

    private CategoriaDTO convertToDTO(Categoria categoria) {
        return new CategoriaDTO(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );
    }

    private CategoriaDetailDTO convertToDetailDTO(Categoria categoria) {
        var productosDTO = categoria.getProductos().stream()
                .map(producto -> new ProductoDTO(
                        producto.getId(),
                        producto.getNombre(),
                        producto.getPrecioUnitario(),
                        producto.getCosto(),
                        null // No incluir categorías para evitar recursividad
                ))
                .collect(Collectors.toSet());

        return new CategoriaDetailDTO(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                productosDTO
        );
    }
}