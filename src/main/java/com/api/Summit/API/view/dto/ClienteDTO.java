package com.api.Summit.API.view.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {
    private Long id;
    private String nombre;
    // Agregar otros campos si los necesitas
    private String telefono;
    private String email;
    private String direccion;
    private boolean frecuente;
}
