package com.api.Summit.API.view.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioDTO {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private int cantidad;
    private int cantidadMinima;
    private int cantidadMaxima;
    private Long negocioId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
