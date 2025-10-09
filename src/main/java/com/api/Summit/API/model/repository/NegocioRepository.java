package com.api.Summit.API.model.repository;

import com.api.Summit.API.model.entities.Negocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NegocioRepository extends JpaRepository<Negocio, Long> {
    // ✅ Agregar método para verificar si existe un negocio con el mismo nombre
    boolean existsByNombre(String nombre);
}
