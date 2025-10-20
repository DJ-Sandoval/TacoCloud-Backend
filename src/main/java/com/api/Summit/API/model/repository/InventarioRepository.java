package com.api.Summit.API.model.repository;

import com.api.Summit.API.model.entities.Inventario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    // Métodos básicos por negocio
    Page<Inventario> findByNegocioId(Long negocioId, Pageable pageable);
    Optional<Inventario> findByIdAndNegocioId(Long id, Long negocioId);
    Optional<Inventario> findByProductoIdAndNegocioId(Long productoId, Long negocioId);
    boolean existsByProductoIdAndNegocioId(Long productoId, Long negocioId);

    // Métodos para verificación de stock
    @Query("SELECT i FROM Inventario i WHERE i.cantidad < i.cantidadMinima AND i.negocio.id = :negocioId")
    Page<Inventario> findByCantidadLessThanCantidadMinimaAndNegocioId(@Param("negocioId") Long negocioId, Pageable pageable);

    @Query("SELECT i FROM Inventario i WHERE i.cantidad > i.cantidadMaxima AND i.negocio.id = :negocioId")
    Page<Inventario> findByCantidadGreaterThanCantidadMaximaAndNegocioId(@Param("negocioId") Long negocioId, Pageable pageable);

    // Método para buscar por nombre de producto
    @Query("SELECT i FROM Inventario i WHERE i.negocio.id = :negocioId AND LOWER(i.producto.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Page<Inventario> findByNegocioIdAndProductoNombreContainingIgnoreCase(@Param("negocioId") Long negocioId, @Param("nombre") String nombre, Pageable pageable);
}
