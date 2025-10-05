package com.api.Summit.API.view.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoDTO {
    private Long id;
    private String nombre;
    private double precioUnitario;
    private double costo;
    private Set<CategoriaDTO> categorias;
}
