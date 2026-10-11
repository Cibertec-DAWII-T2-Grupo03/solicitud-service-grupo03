package com.grupo03.solicitudservice.repository;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import com.grupo03.solicitudservice.model.SolicitudPrestamo;
import org.springframework.stereotype.Repository;

@Repository
public class SolicitudPrestamoRepository {

    private final Map<Long, SolicitudPrestamo> solicitudes = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public Optional<SolicitudPrestamo> findById(Long idSolicitud) {
        return Optional.ofNullable(solicitudes.get(idSolicitud));
    }

    public List<SolicitudPrestamo> findByClienteId(Long clienteId) {
        return solicitudes.values().stream()
                .filter(solicitud -> solicitud.getClienteId().equals(clienteId))
                .toList();
    }

    public Collection<SolicitudPrestamo> findAll() {
        return solicitudes.values();
    }

    public SolicitudPrestamo save(SolicitudPrestamo solicitudPrestamo) {
        solicitudes.put(solicitudPrestamo.getIdSolicitud(), solicitudPrestamo);
        return solicitudPrestamo;
    }

    public Long nextId() {
        return sequence.getAndIncrement();
    }
}
