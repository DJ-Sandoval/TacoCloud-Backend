package com.api.Summit.API.view.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoSimpleDTO {
    private Long id;
    private String nombre;
    private double precioUnitario;
    private double costo;
    private Long negocioId;
}
