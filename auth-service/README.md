# Auth Service

This module provides authentication and authorization for Quantity Measurement microservices.
It is configured as a Eureka client and registers with the service discovery server.

## Tech Stack

- Java 17
- Spring Boot 3.4.3
- Spring Cloud Netflix Eureka Client
- Spring Security
- JWT (jjwt)
- OAuth2 (Google)
- JPA/Hibernate with MySQL in dev and production

## Configuration

Base defaults are in `src/main/resources/application.yml`.

Profiles:
- `dev`: local defaults in `application-dev.yml`
- `prod`: production-safe overrides in `application-prod.yml`

Environment variables (optional):
- `SPRING_PROFILES_ACTIVE` (default: `dev`)
- `SERVER_PORT` (default: `8082`)
- `EUREKA_DEFAULT_ZONE` (default: `http://localhost:8761/eureka/`)
- `JWT_SECRET` (default: built-in secret for dev only)
- `JWT_EXPIRATION_MS` (default: `86400000` = 24 hours)
- `DB_URL` (dev default: `jdbc:mysql://127.0.0.1:3306/auth_service?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`)
- `DB_USERNAME` (dev default: `root`)
- `DB_PASSWORD` (dev default: `root`)
- `DB_DDL_AUTO` (dev default: `update`, prod default: `validate`)

## API Endpoints

- `POST /api/v1/auth/login` — User authentication
- `POST /api/v1/auth/register` — User sign-up
- `POST /api/v1/auth/logout` — Session logout
- `POST /api/v1/auth/oauth2/google` — Google OAuth2 login

## Run (PowerShell)

```powershell
Set-Location "D:\Training\qmp\QuantityMeasurementApp\auth-service"
mvn spring-boot:run
```

## Test (PowerShell)

```powershell
Set-Location "D:\Training\qmp\QuantityMeasurementApp\auth-service"
mvn test
```

## Notes

- Start Eureka server before running this service.
- This service registers with Eureka on startup under the name `AUTH-SERVICE`.
- For containerized infra-only startup (without modifying monolith), see `README.microservices.md`.
- In development, MySQL is used locally or via Docker Compose.
- In production, configure a cloud-hosted MySQL connection string via `DB_URL` and credentials via `DB_USERNAME` / `DB_PASSWORD`.

