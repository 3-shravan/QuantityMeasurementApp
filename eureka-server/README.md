# Eureka Server

This module provides service discovery for the Quantity Measurement microservices architecture using Spring Cloud Netflix Eureka.

## Tech Stack

- Java 17
- Spring Boot 3.4.3
- Spring Cloud Netflix Eureka Server

## Configuration

Base defaults are in `src/main/resources/application.yml`.

Profiles:
- `dev`: local defaults in `application-dev.yml`
- `prod`: production-safe overrides in `application-prod.yml`

Environment variables (optional):
- `SPRING_PROFILES_ACTIVE` (default: `dev`)
- `SERVER_PORT` (default: `8761`)
- `EUREKA_HOST` (default: `localhost`)
- `EUREKA_DEFAULT_ZONE` (default: `http://<host>:<port>/eureka/`)

## Run (PowerShell)

```powershell
Set-Location "D:\Training\qmp\QuantityMeasurementApp\eureka-server"
mvn spring-boot:run
```

Open:
- Eureka dashboard: `http://localhost:8761/`
- Health endpoint: `http://localhost:8761/actuator/health`

## Test (PowerShell)

```powershell
Set-Location "D:\Training\qmp\QuantityMeasurementApp\eureka-server"
mvn test
```

## Service Registration (for your future services)

Add this in each service (for example `api-gateway` or extracted modules from the monolith):

```yaml
spring:
  application:
	name: <service-name>
eureka:
  client:
	service-url:
	  defaultZone: ${EUREKA_DEFAULT_ZONE:http://localhost:8761/eureka/}
```

For containerized startup of `eureka-server` + `api-gateway` + `redis` while keeping `monolith/` separate, see `README.microservices.md`.

