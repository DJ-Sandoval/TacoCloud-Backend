package com.api.Summit.API.model.repository;

import com.api.Summit.API.model.entities.DetallePedidoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoVentaRepository extends JpaRepository<DetallePedidoVenta, Long> {
}
