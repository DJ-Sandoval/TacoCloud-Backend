package com.api.Summit.API.view.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaRequestDTO {

    @NotNull(message = "El cliente es obligatorio")
    @Positive(message = "El ID del cliente debe ser válido")
    private Long clienteId;

    @Valid
    private List<ConceptoRequestDTO> conceptos;
}
