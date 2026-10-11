package com.grupo03.solicitudservice.client;

import com.grupo03.solicitudservice.dto.ScoringRiesgoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "${services.evaluacion.name:evaluacion-service-grupo03}",
        url = "${services.evaluacion.url:http://localhost:8082}",
        path = "/api/v1/evaluaciones"
)
public interface EvaluacionClient {

    @GetMapping("/scoring/{clienteId}")
    ScoringRiesgoDTO obtenerScoring(@PathVariable Long clienteId);
}
