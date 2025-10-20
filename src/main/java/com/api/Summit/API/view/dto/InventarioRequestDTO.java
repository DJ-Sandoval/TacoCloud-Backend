package com.api.Summit.API.view.dto;
import lombok.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioRequestDTO {
    @NotNull(message = "El producto es requerido")
    private Long productoId;

    @NotNull(message = "La cantidad es requerida")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;

    @NotNull(message = "La cantidad mínima es requerida")
    @Min(value = 0, message = "La cantidad mínima no puede ser negativa")
    private Integer cantidadMinima;

    @NotNull(message = "La cantidad máxima es requerida")
    @Min(value = 1, message = "La cantidad máxima debe ser al menos 1")
    private Integer cantidadMaxima;
}