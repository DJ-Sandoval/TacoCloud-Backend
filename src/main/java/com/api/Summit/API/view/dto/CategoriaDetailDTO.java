package com.api.Summit.API.view.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDetailDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Set<ProductoDTO> productos;
}
