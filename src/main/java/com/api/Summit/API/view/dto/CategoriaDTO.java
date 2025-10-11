package com.api.Summit.API.view.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long negocioId;
    private Set<ProductoDTO> productos;
}
