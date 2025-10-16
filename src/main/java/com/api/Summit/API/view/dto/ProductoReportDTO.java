package com.api.Summit.API.view.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoReportDTO {
    private String titulo;
    private String fechaGeneracion;
    private List<ProductoReportDataDTO> productos;
    private int totalProductos;
    private double valorTotalInventario;
    private double precioPromedio;
    private String negocioNombre;
}
