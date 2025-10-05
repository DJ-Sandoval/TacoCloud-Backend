package com.api.Summit.API.model.enums;

public enum EstadoPedido {
    PENDIENTE,           // Pedido recibido, pendiente de confirmación
    CONFIRMADO,          // Pedido confirmado por la cocina
    EN_PREPARACION,      // En proceso de preparación
    LISTO,               // Pedido listo para entregar
    EN_CAMINO,           // Para domicilio: en camino
    ENTREGADO,           // Pedido entregado al cliente
    CANCELADO,           // Pedido cancelado
    RECHAZADO            // Pedido rechazado por el negocio
}
