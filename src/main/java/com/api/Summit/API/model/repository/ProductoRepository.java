package com.api.Summit.API.model.repository;

import com.api.Summit.API.model.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByNombre(String nombre);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Producto p WHERE p.nombre = :nombre AND p.id != :id")
    boolean existsByNombreAndIdNot(@Param("nombre") String nombre, @Param("id") Long id);
}
