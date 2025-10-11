package com.api.Summit.API.model.repository;

import com.api.Summit.API.model.entities.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Métodos existentes
    boolean existsByNombre(String nombre);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Producto p WHERE p.nombre = :nombre AND p.id != :id")
    boolean existsByNombreAndIdNot(@Param("nombre") String nombre, @Param("id") Long id);

    // Nuevos métodos para negocio
    Page<Producto> findByNegocioId(Long negocioId, Pageable pageable);

    Optional<Producto> findByIdAndNegocioId(Long id, Long negocioId);

    boolean existsByNombreAndNegocioId(String nombre, Long negocioId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Producto p WHERE p.nombre = :nombre AND p.negocio.id = :negocioId AND p.id != :id")
    boolean existsByNombreAndNegocioIdAndIdNot(@Param("nombre") String nombre, @Param("negocioId") Long negocioId, @Param("id") Long id);

    Page<Producto> findByNegocioIdAndNombreContainingIgnoreCase(Long negocioId, String nombre, Pageable pageable);
}
