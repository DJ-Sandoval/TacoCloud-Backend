package com.api.Summit.API.view.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaSimpleDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long negocioId;
}
