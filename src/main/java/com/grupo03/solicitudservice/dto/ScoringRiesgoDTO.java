package com.grupo03.solicitudservice.dto;

public record ScoringRiesgoDTO(
        Long clienteId,
        Integer puntajeCrediticio,
        String nivelRiesgo,
        Double capacidadMaxEndeudamiento,
        String dictamenRiesgo
) {
}
