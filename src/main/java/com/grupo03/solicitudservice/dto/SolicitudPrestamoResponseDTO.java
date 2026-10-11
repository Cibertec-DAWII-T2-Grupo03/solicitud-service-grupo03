package com.grupo03.solicitudservice.dto;

import java.util.List;

public record SolicitudPrestamoResponseDTO(
        Long idSolicitud,
        Long clienteId,
        String nombreCliente,
        String dni,
        Double montoSolicitado,
        Integer plazoMeses,
        Double tasaInteresAnual,
        String estadoSolicitud,
        List<CuotaProyectadaDTO> listaCuotasProyectadas,
        Double montoTotalPagar
) {
}
