package com.grupo03.solicitudservice.controller;

import com.grupo03.solicitudservice.dto.ResumenCrediticioClienteDTO;
import com.grupo03.solicitudservice.dto.SolicitudPrestamoRequestDTO;
import com.grupo03.solicitudservice.dto.SolicitudPrestamoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import com.grupo03.solicitudservice.service.SolicitudPrestamoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/solicitudes")
public class SolicitudPrestamoController {

    private final SolicitudPrestamoService solicitudPrestamoService;

    public SolicitudPrestamoController(SolicitudPrestamoService solicitudPrestamoService) {
        this.solicitudPrestamoService = solicitudPrestamoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitudPrestamoResponseDTO registrarSolicitud(
            @Valid @RequestBody SolicitudPrestamoRequestDTO solicitudRequest) {
        return solicitudPrestamoService.registrarSolicitud(solicitudRequest);
    }

    @GetMapping("/{idSolicitud}")
    public SolicitudPrestamoResponseDTO obtenerSolicitud(@PathVariable Long idSolicitud) {
        return solicitudPrestamoService.obtenerSolicitud(idSolicitud);
    }

    @GetMapping("/evaluar-y-aprobar/{idSolicitud}")
    public SolicitudPrestamoResponseDTO evaluarYAprobar(@PathVariable Long idSolicitud) {
        return solicitudPrestamoService.evaluarYAprobar(idSolicitud);
    }

    @GetMapping("/resumen-cliente/{clienteId}")
    public ResumenCrediticioClienteDTO obtenerResumenCliente(@PathVariable Long clienteId) {
        return solicitudPrestamoService.obtenerResumenCliente(clienteId);
    }
}
