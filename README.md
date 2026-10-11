# solicitud-service-grupo03

Gestion de prestamos, calculo de cuotas y aprobaciones.

## Parametros

- Java 25
- Spring Boot 4.1.1
- Spring Cloud 2025.1.3

## Ejecutar

```bash
mvn spring-boot:run
```

El servicio queda disponible en `http://localhost:8070`.

## Alcance

Este repositorio implementa solo `solicitud-service-grupo03`.

El servicio de evaluacion se consume como dependencia externa mediante FeignClient:

- `GET /api/v1/evaluaciones/scoring/{clienteId}`

## Endpoints principales

- `POST /api/v1/solicitudes`
- `GET /api/v1/solicitudes/{idSolicitud}`
- `GET /api/v1/solicitudes/evaluar-y-aprobar/{idSolicitud}`
- `GET /api/v1/solicitudes/resumen-cliente/{clienteId}`
- `GET /api/v1/solicitudes/configuracion`

### Crear una solicitud

```bash
curl -X POST http://localhost:8070/api/v1/solicitudes \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "nombreCliente": "Ana Torres",
    "dni": "45678912",
    "montoSolicitado": 1200.00,
    "plazoMeses": 12
  }'
```

## Refresh de configuracion

El endpoint actuator de refresh queda expuesto en el puerto de management `8083`:

```bash
curl -X POST http://localhost:8083/actuator/refresh
```
