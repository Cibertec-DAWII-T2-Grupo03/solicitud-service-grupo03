package com.grupo03.solicitudservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SolicitudPrestamoRequestDTO(
        @NotNull
        Long clienteId,

        @NotBlank
        String nombreCliente,

        @NotBlank
        String dni,

        @NotNull
        @DecimalMin(value = "0.01")
        Double montoSolicitado,

        @NotNull
        @Min(1)
        Integer plazoMeses
) {
}
