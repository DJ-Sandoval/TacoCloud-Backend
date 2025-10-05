package com.api.Summit.API.service.interfaces;
import com.api.Summit.API.view.dto.CategoriaDTO;
import com.api.Summit.API.view.dto.CategoriaDetailDTO;
import com.api.Summit.API.view.dto.CategoriaRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoriaService {
    Page<CategoriaDTO> findAll(Pageable pageable);
    CategoriaDetailDTO findById(Long id);
    CategoriaDTO save(CategoriaRequestDTO categoriaRequestDTO);
    CategoriaDTO update(Long id, CategoriaRequestDTO categoriaRequestDTO);
    void delete(Long id);
}
