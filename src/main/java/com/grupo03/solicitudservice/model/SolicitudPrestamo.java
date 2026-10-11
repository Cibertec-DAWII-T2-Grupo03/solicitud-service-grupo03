package com.grupo03.solicitudservice.model;

public class SolicitudPrestamo {

    private final Long idSolicitud;
    private final Long clienteId;
    private final String nombreCliente;
    private final String dni;
    private final Double montoSolicitado;
    private final Integer plazoMeses;
    private String estadoSolicitud;

    public SolicitudPrestamo(Long idSolicitud, Long clienteId, String nombreCliente, String dni,
            Double montoSolicitado, Integer plazoMeses, String estadoSolicitud) {
        this.idSolicitud = idSolicitud;
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
        this.dni = dni;
        this.montoSolicitado = montoSolicitado;
        this.plazoMeses = plazoMeses;
        this.estadoSolicitud = estadoSolicitud;
    }

    public Long getIdSolicitud() {
        return idSolicitud;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getDni() {
        return dni;
    }

    public Double getMontoSolicitado() {
        return montoSolicitado;
    }

    public Integer getPlazoMeses() {
        return plazoMeses;
    }

    public String getEstadoSolicitud() {
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(String estadoSolicitud) {
        this.estadoSolicitud = estadoSolicitud;
    }
}
