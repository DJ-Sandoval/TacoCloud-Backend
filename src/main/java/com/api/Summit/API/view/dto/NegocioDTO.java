package com.api.Summit.API.view.dto;

import com.api.Summit.API.model.entities.Negocio;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NegocioDTO {
    private Long id;
    private String nombre;
    private String descripcion;

    public static NegocioDTO fromNegocio(Negocio negocio) {
        return NegocioDTO.builder()
                .id(negocio.getId())
                .nombre(negocio.getNombre())
                .descripcion(negocio.getDescripcion())
                .build();
    }

    public static Negocio toEntity(NegocioDTO negocioDTO) {
        return Negocio.builder()
                .id(negocioDTO.getId())
                .nombre(negocioDTO.getNombre())
                .descripcion(negocioDTO.getDescripcion())
                .build();
    }
}
