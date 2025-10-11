package com.api.Summit.API.service.impl;

import com.api.Summit.API.model.entities.Categoria;
import com.api.Summit.API.model.entities.Negocio;
import com.api.Summit.API.model.repository.CategoriaRepository;
import com.api.Summit.API.model.repository.NegocioRepository;
import com.api.Summit.API.service.interfaces.CategoriaService;
import com.api.Summit.API.view.dto.CategoriaDTO;
import com.api.Summit.API.view.dto.CategoriaRequestDTO;
import com.api.Summit.API.view.dto.ProductoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final NegocioRepository negocioRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaDTO> findAll(Pageable pageable) {
        throw new UnsupportedOperationException("Use findAllByNegocioId instead");
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaDTO findById(Long id) {
        throw new UnsupportedOperationException("Use findByIdAndNegocioId instead");
    }

    @Override
    @Transactional
    public CategoriaDTO save(CategoriaRequestDTO categoriaRequestDTO) {
        throw new UnsupportedOperationException("Use saveWithNegocio instead");
    }

    @Override
    @Transactional
    public CategoriaDTO update(Long id, CategoriaRequestDTO categoriaRequestDTO) {
        throw new UnsupportedOperationException("Use updateWithNegocio instead");
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        throw new UnsupportedOperationException("Use deleteByIdAndNegocioId instead");
    }

    // Listar categorías por negocio
    @Transactional(readOnly = true)
    public Page<CategoriaDTO> findAllByNegocioId(Long negocioId, Pageable pageable) {
        // Necesitarás agregar este método en el repository
        Page<Categoria> categorias = categoriaRepository.findByNegocioId(negocioId, pageable);
        return categorias.map(this::convertToDTO);
    }

    // Buscar categoría por ID y negocio
    @Transactional(readOnly = true)
    public CategoriaDTO findByIdAndNegocioId(Long id, Long negocioId) {
        Categoria categoria = categoriaRepository.findByIdAndNegocioId(id, negocioId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id + " para el negocio: " + negocioId));
        return convertToDTO(categoria);
    }

    // Crear categoría asociada a un negocio
    @Transactional
    public CategoriaDTO saveWithNegocio(CategoriaRequestDTO categoriaRequestDTO, Long negocioId) {
        // Verificar si ya existe una categoría con el mismo nombre en este negocio
        if (categoriaRepository.existsByNombreAndNegocioId(categoriaRequestDTO.getNombre(), negocioId)) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + categoriaRequestDTO.getNombre() + " en este negocio");
        }

        Negocio negocio = negocioRepository.findById(negocioId)
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado con ID: " + negocioId));

        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaRequestDTO.getNombre());
        categoria.setDescripcion(categoriaRequestDTO.getDescripcion());
        categoria.setNegocio(negocio);

        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return convertToDTO(categoriaGuardada);
    }

    // Actualizar categoría verificando el negocio
    @Transactional
    public CategoriaDTO updateWithNegocio(Long id, CategoriaRequestDTO categoriaRequestDTO, Long negocioId) {
        Categoria categoria = categoriaRepository.findByIdAndNegocioId(id, negocioId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id + " para el negocio: " + negocioId));

        // Verificar si el nuevo nombre ya existe en otra categoría del mismo negocio
        if (categoriaRepository.existsByNombreAndNegocioIdAndIdNot(categoriaRequestDTO.getNombre(), negocioId, id)) {
            throw new RuntimeException("Ya existe otra categoría con el nombre: " + categoriaRequestDTO.getNombre() + " en este negocio");
        }

        categoria.setNombre(categoriaRequestDTO.getNombre());
        categoria.setDescripcion(categoriaRequestDTO.getDescripcion());

        Categoria categoriaActualizada = categoriaRepository.save(categoria);
        return convertToDTO(categoriaActualizada);
    }

    // Buscar categorías por nombre en un negocio específico
    @Transactional(readOnly = true)
    public Page<CategoriaDTO> searchByNombreAndNegocioId(String nombre, Long negocioId, Pageable pageable) {
        Page<Categoria> categorias = categoriaRepository.findByNegocioIdAndNombreContainingIgnoreCase(negocioId, nombre, pageable);
        return categorias.map(this::convertToDTO);
    }

    // Eliminar categoría verificando el negocio
    @Transactional
    public void deleteByIdAndNegocioId(Long id, Long negocioId) {
        Categoria categoria = categoriaRepository.findByIdAndNegocioId(id, negocioId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id + " para el negocio: " + negocioId));

        categoriaRepository.delete(categoria);
    }

    // Método de conversión a DTO
    private CategoriaDTO convertToDTO(Categoria categoria) {
        return CategoriaDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .negocioId(categoria.getNegocio().getId())
                .productos(categoria.getProductos() != null ?
                        categoria.getProductos().stream()
                                .map(producto -> ProductoDTO.builder()
                                        .id(producto.getId())
                                        .nombre(producto.getNombre())
                                        // Agrega más campos si es necesario
                                        .build())
                                .collect(Collectors.toSet()) : null)
                .build();
    }
}