# API Gateway

The central entry point for all client requests in the Quantity Measurement microservices architecture.

## Overview

The API Gateway routes requests to appropriate microservices, manages authentication, implements rate limiting, and provides resilience through retry logic and circuit breaker patterns.

**Default Port:** `8081`

## Tech Stack

- **Java:** 17+
- **Spring Boot:** 3.4.3
- **Spring Cloud Gateway:** Reactive API gateway with intelligent routing
- **Spring Cloud Eureka:** Service discovery and registration
- **Resilience4j:** Circuit breaker, retry, and rate limiting
- **Redis:** Distributed rate limiting and caching
- **JWT:** Token-based authentication relay

## Architecture

```
Clients
  │
  ├─ Login/Register (Public)
  │    ↓
  ├─ Auth Gateway Routes
  │    ↓ (Auth Service)
  │
  ├─ Protected Routes (JWT Required)
  │    ↓
  ├─ Quantity Routes
  │    ↓ (Quantity Service)
```

## Routing Configuration

### Authentication Routes (Public - No JWT Required)

| Endpoint | Method | Purpose | Rate Limit |
|----------|--------|---------|------------|
| `/api/v1/auth/register` | POST | User registration | 20/sec |
| `/api/v1/auth/login` | POST | User authentication | 20/sec |
| `/api/v1/auth/logout` | POST | User logout | Unlimited |
| `/api/v1/auth/oauth2/google` | POST | OAuth2 authentication | 20/sec |

### Quantity Routes (Protected - JWT Required)

| Endpoint | Method | Purpose | Rate Limit |
|----------|--------|---------|------------|
| `/api/v1/quantities` | GET | Fetch all quantities | 30/sec |
| `/api/v1/quantities/{id}` | GET | Fetch single quantity | 30/sec |
| `/api/v1/quantities` | POST | Create quantity | 30/sec |
| `/api/v1/quantities/{id}` | PUT | Update quantity | 30/sec |
| `/api/v1/quantities/{id}` | DELETE | Delete quantity | 30/sec |

## Configuration

Base configuration: `src/main/resources/application.yml`

### Environment Profiles

- **dev:** Local development (Redis-backed rate limiting, debug logging, relaxed rate limits)
- **prod:** Production settings (Redis-backed rate limiting, limited logging, strict rate limits)

Configuration files:
- `application-dev.yml` - Development overrides
- `application-prod.yml` - Production overrides

### Environment Variables

**Required for Production:**
```bash
EUREKA_DEFAULT_ZONE=<eureka-server-url>
REDIS_HOST=<redis-host>
REDIS_PASSWORD=<redis-password>
```

**Optional Configuration:**
```bash
SPRING_PROFILES_ACTIVE=dev|prod              # Default: dev
SERVER_PORT=8081                              # Default: 8081
GATEWAY_RETRY_RETRIES=2|3                     # Default: 2 (dev), 3 (prod)
GATEWAY_AUTH_RATE_PER_SEC=20|50              # Default: 20 (dev), 50 (prod)
GATEWAY_AUTH_BURST_CAPACITY=40|100           # Default: 40 (dev), 100 (prod)
GATEWAY_QUANTITY_RATE_PER_SEC=30|100         # Default: 30 (dev), 100 (prod)
GATEWAY_QUANTITY_BURST_CAPACITY=60|200       # Default: 60 (dev), 200 (prod)
```

## Features

### 1. Service Discovery
- Automatic service registration/discovery via Eureka
- Load balancing across service instances
- Dynamic route configuration

### 2. Request Routing
- Path-based routing to microservices
- Separate routes for public and protected endpoints
- Custom headers for tracking (X-Auth-Type, X-Gateway-Request-Id)

### 3. Security
- JWT token relay to downstream services
- Automatic token validation for protected routes
- Public endpoints accessible without authentication

### 4. Resilience
- **Retry:** Automatic retry on transient failures (up to 3 attempts)
- **Circuit Breaker:** Opens after 50% failure rate, recovers after 15s
- **Fallback:** Service unavailable responses when circuit is open

### 5. Rate Limiting
- Per-user rate limiting based on JWT token
- Per-IP fallback if no JWT token present
- Redis-backed distributed rate limiting
- Separate limits for auth and quantity services

### 6. Monitoring & Metrics
- Health check endpoints
- Prometheus-compatible metrics export
- Gateway routes and filters inspection
- Circuit breaker state monitoring

## File Structure

```
api-gateway/
├── src/main/java/com/app/apigateway/
│   ├── ApiGatewayApplication.java
│   ├── config/
│   │   ├── GatewaySecurityAndRateLimitConfig.java
│   │   └── GatewayConfigProperties.java
│   ├── controller/
│   │   └── GatewayFallbackController.java
│   └── filter/
│       ├── JwtRelayGlobalFilter.java
│       └── AuthenticationGatewayFilter.java
├── src/main/resources/
│   ├── application.yml
│   ├── application-dev.yml
│   └── application-prod.yml
├── API_GATEWAY_CONFIGURATION.md    # Detailed configuration guide
├── QUICK_START.md                   # Quick testing guide
└── README.md                         # This file
```

## Quick Start

### 1. Start Dependencies

```powershell
# Terminal 1: Eureka Server
cd eureka-server
mvn spring-boot:run

# Terminal 2: Auth Service
cd auth-service
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Terminal 3: Quantity Service
cd monolith
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### 2. Run API Gateway

```powershell
cd api-gateway
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

Gateway will be available at: `http://localhost:8081`

### 3. Test the Gateway

```bash
# Register a user
curl -X POST http://localhost:8081/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","email":"john@example.com","password":"Pass123"}'

# Login
curl -X POST http://localhost:8081/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"Pass123"}'

# Use JWT token from response to call protected endpoints
curl -X GET http://localhost:8081/api/v1/quantities \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

See `QUICK_START.md` for detailed testing guide.

## Build & Test

```powershell
# Build
cd api-gateway
mvn clean package

# Run tests
mvn test

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

## Docker

```bash
# Build image
docker build -t api-gateway:1.0.0 .

# Run container
docker run -p 8081:8081 \
  -e EUREKA_DEFAULT_ZONE=http://eureka:8761/eureka/ \
  -e REDIS_HOST=redis \
  -e REDIS_PASSWORD=<redis-password> \
  api-gateway:1.0.0

# With Docker Compose
docker-compose -f docker-compose.microservices.yml up api-gateway
```

## Health & Monitoring

```bash
# Health check
curl http://localhost:8081/actuator/health

# View all routes
curl http://localhost:8081/actuator/gateway/routes | jq

# View route filters
curl http://localhost:8081/actuator/gateway/routefilters | jq

# View metrics
curl http://localhost:8081/actuator/metrics | jq

# Prometheus metrics
curl http://localhost:8081/actuator/prometheus
```

## Troubleshooting

| Issue | Solution |
|-------|----------|
| **404 Service Not Found** | Ensure Eureka is running and services are registered. Check `http://localhost:8761` |
| **401 Unauthorized** | Login first to get JWT token, then include it in `Authorization: Bearer <token>` header |
| **429 Too Many Requests** | You've hit rate limit. Wait for window to reset or increase limits in config |
| **503 Service Unavailable** | A service is down. Circuit breaker has opened. Wait 15s for recovery or restart service |
| **Connection timeout** | Check if services are running and accessible. Verify network/firewall settings |

See `API_GATEWAY_CONFIGURATION.md` for detailed troubleshooting.

## Documentation

- **API_GATEWAY_CONFIGURATION.md** - Comprehensive routing and configuration guide
- **QUICK_START.md** - Step-by-step testing guide with curl/Postman examples
- **../README.microservices.md** - Full microservices architecture guide
- **../SERVICES_INDEX.md** - Index of all microservices

## Next Steps

1. ✅ Configure authentication routes to Auth Service
2. ✅ Configure protected routes to downstream services
3. ✅ Implement JWT token relay
4. ✅ Set up rate limiting and circuit breaker
5. 📋 Extract additional services from monolith
6. 📋 Deploy to Docker/Kubernetes
7. 📋 Set up monitoring and alerting

## Support

- Check service logs: `mvn spring-boot:run -Dspring-boot.run.arguments="--debug"`
- View gateway routes: `curl http://localhost:8081/actuator/gateway/routes`
- Check service registration: `curl http://localhost:8761/eureka/apps`
- Review metrics: `curl http://localhost:8081/actuator/metrics`
