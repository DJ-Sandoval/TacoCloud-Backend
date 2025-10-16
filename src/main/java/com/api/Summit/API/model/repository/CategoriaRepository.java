package com.api.Summit.API.model.repository;

import com.api.Summit.API.model.entities.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Nuevos métodos para negocio
    Page<Categoria> findByNegocioId(Long negocioId, Pageable pageable);

    Optional<Categoria> findByIdAndNegocioId(Long id, Long negocioId);

    boolean existsByNombreAndNegocioId(String nombre, Long negocioId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Categoria c WHERE c.nombre = :nombre AND c.negocio.id = :negocioId AND c.id != :id")
    boolean existsByNombreAndNegocioIdAndIdNot(@Param("nombre") String nombre, @Param("negocioId") Long negocioId, @Param("id") Long id);

    Page<Categoria> findByNegocioIdAndNombreContainingIgnoreCase(Long negocioId, String nombre, Pageable pageable);
    List<Categoria> findByNegocioId(Long negocioId);
}
