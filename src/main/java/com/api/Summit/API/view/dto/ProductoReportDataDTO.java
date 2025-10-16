package com.api.Summit.API.view.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoReportDataDTO {
    private Long id;
    private String nombre;
    private double precioUnitario;
    private double costo;
    private double margenGanancia;
    private double porcentajeMargen;
    private String categorias;
    private int cantidadCategorias;
}
