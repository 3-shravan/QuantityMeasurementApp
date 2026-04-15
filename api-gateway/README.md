# API Gateway

This module acts as the API Gateway for Quantity Measurement microservices.
It is configured as a Eureka client and can route using service discovery.

## Tech Stack

- Java 17
- Spring Boot 3.4.3
- Spring Cloud Gateway
- Spring Cloud Netflix Eureka Client

## Configuration

Base defaults are in `src/main/resources/application.yml`.

Profiles:
- `dev`: local defaults in `application-dev.yml`
- `prod`: production-safe overrides in `application-prod.yml`

Environment variables (optional):
- `SPRING_PROFILES_ACTIVE` (default: `dev`)
- `SERVER_PORT` (default: `8081`)
- `EUREKA_DEFAULT_ZONE` (default: `http://localhost:8761/eureka/`)
- `QUANTITY_SERVICE_NAME` (default: `quantity-measurement-app`)
- `GATEWAY_RETRY_RETRIES` (default: `2`)
- `GATEWAY_AUTH_RATE_PER_SEC` (default: `20`)
- `GATEWAY_AUTH_BURST_CAPACITY` (default: `40`)
- `GATEWAY_QUANTITY_RATE_PER_SEC` (default: `30`)
- `GATEWAY_QUANTITY_BURST_CAPACITY` (default: `60`)

## Default Routes

- `/api/v1/auth/**` -> `lb://quantity-measurement-app`
- `/api/v1/quantities/**` -> `lb://quantity-measurement-app`

These are explicit routes for your current monolith API paths, so clients can call the gateway now and keep the same URLs while services are split out.

## Production Filters Enabled

- JWT relay: forwards incoming bearer token downstream.
- Retry: retries transient gateway/downstream failures.
- Circuit breaker: opens on repeated failures and forwards to fallback handlers.
- Rate limiting: per-user (bearer token) or per-IP with Redis-backed `RequestRateLimiter`.

Fallback endpoints used by circuit breakers:
- `/fallback/auth`
- `/fallback/quantity`

## Run (PowerShell)

```powershell
Set-Location "D:\Training\qmp\QuantityMeasurementApp\api-gateway"
mvn spring-boot:run
```

## Test (PowerShell)

```powershell
Set-Location "D:\Training\qmp\QuantityMeasurementApp\api-gateway"
mvn test
```

## Notes

- Start Eureka server before running gateway.
- If your monolith (or extracted service) registers with another service id, set `QUANTITY_SERVICE_NAME`.
- Discovery locator is enabled, so services can be routed with `lb://<service-id>`.
- Route list endpoint: `http://localhost:8081/actuator/gateway/routes`
- Rate limiting requires a reachable Redis instance in non-test runtime.
- For containerized infra-only startup (without modifying monolith), see `README.microservices.md`.

