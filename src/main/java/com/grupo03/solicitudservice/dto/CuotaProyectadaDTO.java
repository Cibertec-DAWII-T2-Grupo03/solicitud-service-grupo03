package com.grupo03.solicitudservice.dto;

import java.time.LocalDate;

public record CuotaProyectadaDTO(
        Integer numeroCuota,
        LocalDate fechaPago,
        Double montoCuota,
        Double interes,
        Double amortizacion,
        Double saldoPendiente
) {
}
