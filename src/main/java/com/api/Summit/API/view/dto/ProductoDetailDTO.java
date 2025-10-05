package com.api.Summit.API.view.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDetailDTO {
    private Long id;
    private String nombre;
    private double precioUnitario;
    private double costo;
    private Set<CategoriaDTO> categorias;
    private List<ConceptoDTO> conceptosVenta;
}
