package com.api.Summit.API.model.repository;

import com.api.Summit.API.model.entities.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Verificar si existe un cliente con el mismo nombre en el mismo negocio
    boolean existsByNombreAndNegocioId(String nombre, Long negocioId);

    // Verificar si existe otro cliente con el mismo nombre en el mismo negocio (para updates)
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cliente c WHERE c.nombre = :nombre AND c.negocio.id = :negocioId AND c.id != :id")
    boolean existsByNombreAndNegocioIdAndIdNot(@Param("nombre") String nombre, @Param("negocioId") Long negocioId, @Param("id") Long id);

    // Buscar clientes por negocio
    List<Cliente> findByNegocioId(Long negocioId);

    // Buscar clientes por negocio con paginación
    Page<Cliente> findByNegocioId(Long negocioId, Pageable pageable);

    // Buscar cliente por ID y negocio (para seguridad)
    Optional<Cliente> findByIdAndNegocioId(Long id, Long negocioId);

    // Buscar clientes por nombre (búsqueda) en un negocio específico
    @Query("SELECT c FROM Cliente c WHERE c.negocio.id = :negocioId AND LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Page<Cliente> findByNegocioIdAndNombreContainingIgnoreCase(@Param("negocioId") Long negocioId, @Param("nombre") String nombre, Pageable pageable);

    // Contar clientes por negocio
    long countByNegocioId(Long negocioId);

    // Buscar clientes frecuentes por negocio
    List<Cliente> findByNegocioIdAndFrecuenteTrue(Long negocioId);
}
