package com.api.Summit.API.view.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaReportDTO {
    private String titulo;
    private String fechaGeneracion;
    private List<CategoriaReportDataDTO> categorias;
    private int totalCategorias;
    private int totalProductos;
    private String negocioNombre;
}
