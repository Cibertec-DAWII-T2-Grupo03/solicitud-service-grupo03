package com.grupo03.solicitudservice.service;

import java.time.LocalDate;
import java.util.List;

import com.grupo03.solicitudservice.client.EvaluacionClient;
import com.grupo03.solicitudservice.dto.CuotaProyectadaDTO;
import com.grupo03.solicitudservice.dto.ResumenCrediticioClienteDTO;
import com.grupo03.solicitudservice.dto.ScoringRiesgoDTO;
import com.grupo03.solicitudservice.dto.SolicitudPrestamoRequestDTO;
import com.grupo03.solicitudservice.dto.SolicitudPrestamoResponseDTO;
import com.grupo03.solicitudservice.model.SolicitudPrestamo;
import com.grupo03.solicitudservice.repository.SolicitudPrestamoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RefreshScope
@Service
public class SolicitudPrestamoService {

    private final SolicitudPrestamoRepository solicitudPrestamoRepository;
    private final EvaluacionClient evaluacionClient;
    private final Double tasaInteresBase;

    public SolicitudPrestamoService(SolicitudPrestamoRepository solicitudPrestamoRepository,
            EvaluacionClient evaluacionClient,
            @Value("${fintech.tasa-interes-base:0.15}") Double tasaInteresBase) {
        this.solicitudPrestamoRepository = solicitudPrestamoRepository;
        this.evaluacionClient = evaluacionClient;
        this.tasaInteresBase = tasaInteresBase;
    }

    public SolicitudPrestamoResponseDTO obtenerSolicitud(Long idSolicitud) {
        SolicitudPrestamo solicitud = buscarSolicitud(idSolicitud);
        return construirResponse(solicitud);
    }

    public SolicitudPrestamoResponseDTO registrarSolicitud(SolicitudPrestamoRequestDTO solicitudRequest) {
        SolicitudPrestamo solicitud = new SolicitudPrestamo(
                solicitudPrestamoRepository.nextId(),
                solicitudRequest.clienteId(),
                solicitudRequest.nombreCliente(),
                solicitudRequest.dni(),
                solicitudRequest.montoSolicitado(),
                solicitudRequest.plazoMeses(),
                "PENDIENTE"
        );

        return construirResponse(solicitudPrestamoRepository.save(solicitud));
    }

    public SolicitudPrestamoResponseDTO evaluarYAprobar(Long idSolicitud) {
        SolicitudPrestamo solicitud = buscarSolicitud(idSolicitud);
        ScoringRiesgoDTO scoring = evaluacionClient.obtenerScoring(solicitud.getClienteId());

        boolean capacidadSuficiente = solicitud.getMontoSolicitado() <= scoring.capacidadMaxEndeudamiento();
        boolean riesgoAceptable = "BAJO".equalsIgnoreCase(scoring.nivelRiesgo())
                || "MEDIO".equalsIgnoreCase(scoring.nivelRiesgo());

        solicitud.setEstadoSolicitud(capacidadSuficiente && riesgoAceptable
                ? "APROBADO"
                : "RECHAZADO_POR_RIESGO");

        return construirResponse(solicitudPrestamoRepository.save(solicitud));
    }

    public ResumenCrediticioClienteDTO obtenerResumenCliente(Long clienteId) {
        List<SolicitudPrestamo> solicitudes = solicitudPrestamoRepository.findByClienteId(clienteId);
        if (solicitudes.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "No existen solicitudes para el cliente " + clienteId);
        }

        ScoringRiesgoDTO scoring = evaluacionClient.obtenerScoring(clienteId);
        List<SolicitudPrestamoResponseDTO> historial = solicitudes.stream()
                .map(this::construirResponse)
                .toList();

        String estadoCrediticioGlobal = esApto(scoring, solicitudes) ? "APTO" : "NO_APTO";
        SolicitudPrestamo primeraSolicitud = solicitudes.getFirst();

        return new ResumenCrediticioClienteDTO(
                clienteId,
                primeraSolicitud.getNombreCliente(),
                primeraSolicitud.getDni(),
                scoring,
                historial,
                estadoCrediticioGlobal
        );
    }

    private SolicitudPrestamo buscarSolicitud(Long idSolicitud) {
        return solicitudPrestamoRepository.findById(idSolicitud)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                        "No existe la solicitud " + idSolicitud));
    }

    private SolicitudPrestamoResponseDTO construirResponse(SolicitudPrestamo solicitud) {
        List<CuotaProyectadaDTO> cuotas = calcularCuotas(solicitud);
        Double montoTotalPagar = redondear(cuotas.stream()
                .mapToDouble(CuotaProyectadaDTO::montoCuota)
                .sum());

        return new SolicitudPrestamoResponseDTO(
                solicitud.getIdSolicitud(),
                solicitud.getClienteId(),
                solicitud.getNombreCliente(),
                solicitud.getDni(),
                solicitud.getMontoSolicitado(),
                solicitud.getPlazoMeses(),
                tasaInteresBase,
                solicitud.getEstadoSolicitud(),
                cuotas,
                montoTotalPagar
        );
    }

    private List<CuotaProyectadaDTO> calcularCuotas(SolicitudPrestamo solicitud) {
        double tasaMensual = tasaInteresBase / 12;
        int plazo = solicitud.getPlazoMeses();
        double saldo = solicitud.getMontoSolicitado();
        double cuota = tasaMensual == 0
                ? saldo / plazo
                : saldo * tasaMensual / (1 - Math.pow(1 + tasaMensual, -plazo));

        List<CuotaProyectadaDTO> cuotas = new java.util.ArrayList<>();
        for (int numeroCuota = 1; numeroCuota <= plazo; numeroCuota++) {
            double interes = saldo * tasaMensual;
            double amortizacion = cuota - interes;
            saldo = Math.max(0, saldo - amortizacion);
            cuotas.add(new CuotaProyectadaDTO(
                    numeroCuota,
                    LocalDate.now().plusMonths(numeroCuota),
                    redondear(cuota),
                    redondear(interes),
                    redondear(amortizacion),
                    redondear(saldo)
            ));
        }
        return cuotas;
    }

    private boolean esApto(ScoringRiesgoDTO scoring, List<SolicitudPrestamo> solicitudes) {
        double deudaSolicitada = solicitudes.stream()
                .mapToDouble(SolicitudPrestamo::getMontoSolicitado)
                .sum();
        boolean riesgoAceptable = "BAJO".equalsIgnoreCase(scoring.nivelRiesgo())
                || "MEDIO".equalsIgnoreCase(scoring.nivelRiesgo());
        return riesgoAceptable && deudaSolicitada <= scoring.capacidadMaxEndeudamiento();
    }

    private Double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
