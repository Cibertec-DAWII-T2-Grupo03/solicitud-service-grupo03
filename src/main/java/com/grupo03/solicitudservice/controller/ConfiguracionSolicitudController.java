package com.grupo03.solicitudservice.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@RequestMapping("/api/v1/solicitudes")
public class ConfiguracionSolicitudController {

    @Value("${fintech.tasa-interes-base}")
    private Double tasaInteresBase;

    @Value("${fintech.monto-maximo-prestamo}")
    private Double montoMaximoPrestamo;

    @Value("${fintech.mensaje-comercial}")
    private String mensajeComercial;

    @Value("${fintech.comision-desembolso}")
    private Double comisionDesembolso;

    @Value("${spring.datasource.url:sin-configurar}")
    private String datasourceUrl;

    @GetMapping("/configuracion")
    public Map<String, Object> obtenerConfiguracion() {
        return Map.of(
                "tasa-interes-base", tasaInteresBase,
                "monto-maximo-prestamo", montoMaximoPrestamo,
                "mensaje-comercial", mensajeComercial,
                "comision-desembolso", comisionDesembolso,
                "datasource.url", datasourceUrl
        );
    }
}
