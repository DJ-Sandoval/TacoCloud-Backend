package com.api.Summit.API.view.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteReportDTO {
    private String titulo;
    private String fechaGeneracion;
    private List<ClienteDTO> clientes;
    private int totalClientes;
    private int totalFrecuentes;
}
