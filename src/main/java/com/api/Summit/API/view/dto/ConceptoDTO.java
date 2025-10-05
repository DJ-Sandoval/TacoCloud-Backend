package com.api.Summit.API.view.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConceptoDTO {
    private Long id;
    private int cantidad;
    private double precioUnitario;
    private double importe;
    private Long ventaId;
    private Long productoId;
    private String productoNombre;
    private double productoPrecio;
}
