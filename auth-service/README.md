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
- `LOCAL_SERVER_PORT` / `SERVER_PORT` (default: `8081`)
- `LOCAL_EUREKA_DEFAULT_ZONE` / `EUREKA_DEFAULT_ZONE` (default: `http://localhost:8761/eureka/`)
- `JWT_SECRET` (default: built-in secret for dev only)
- `JWT_EXPIRATION` or `JWT_EXPIRATION_MS` (default: `86400000` = 24 hours)
- `LOCAL_DB_URL` / `DB_URL`
- `LOCAL_DB_USERNAME` / `DB_USERNAME`
- `LOCAL_DB_PASSWORD` / `DB_PASSWORD`
- `LOCAL_DB_DDL_AUTO` / `DB_DDL_AUTO`
- `LOCAL_GOOGLE_CLIENT_ID` / `GOOGLE_CLIENT_ID`
- `LOCAL_GOOGLE_CLIENT_SECRET` / `GOOGLE_CLIENT_SECRET`
- `LOCAL_GOOGLE_REDIRECT_URI` / `GOOGLE_REDIRECT_URI`

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
- `POST /api/v1/auth/oauth2/google` expects a Google access token issued to the frontend app; the backend validates it against Google userinfo API and then issues your app JWT.
- If frontend Google login is used, valid Google OAuth client credentials are still required on the frontend side to obtain the access token.

