package com.grupo03.solicitudservice.health;

import java.time.Instant;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public Map<String, Object> root() {
        return Map.of(
                "service", "solicitud-service-grupo03",
                "status", "UP",
                "timestamp", Instant.now()
        );
    }
}
