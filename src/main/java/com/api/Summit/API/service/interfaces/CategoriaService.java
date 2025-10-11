package com.api.Summit.API.service.interfaces;
import com.api.Summit.API.view.dto.CategoriaDTO;
import com.api.Summit.API.view.dto.CategoriaDetailDTO;
import com.api.Summit.API.view.dto.CategoriaRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoriaService {
    Page<CategoriaDTO> findAll(Pageable pageable);
    CategoriaDTO findById(Long id);
    CategoriaDTO save(CategoriaRequestDTO categoriaRequestDTO);
    CategoriaDTO update(Long id, CategoriaRequestDTO categoriaRequestDTO);
    void deleteById(Long id);

    // Métodos con filtro por negocio
    Page<CategoriaDTO> findAllByNegocioId(Long negocioId, Pageable pageable);
    CategoriaDTO findByIdAndNegocioId(Long id, Long negocioId);
    CategoriaDTO saveWithNegocio(CategoriaRequestDTO categoriaRequestDTO, Long negocioId);
    CategoriaDTO updateWithNegocio(Long id, CategoriaRequestDTO categoriaRequestDTO, Long negocioId);
    Page<CategoriaDTO> searchByNombreAndNegocioId(String nombre, Long negocioId, Pageable pageable);
    void deleteByIdAndNegocioId(Long id, Long negocioId);
}
