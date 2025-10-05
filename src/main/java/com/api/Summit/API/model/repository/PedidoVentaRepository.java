package com.api.Summit.API.model.repository;

import com.api.Summit.API.model.entities.PedidoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoVentaRepository extends JpaRepository<PedidoVenta, Long> {
}
