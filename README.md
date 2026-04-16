# Quick Start Guide: Auth Service Microservice

## What Was Just Implemented

You now have a **fully functional microservices architecture** with:
- ✅ **Eureka Server** (Service Discovery) - Port 8761
- ✅ **API Gateway** (Request Routing) - Port 8081
- ✅ **Auth Service** (Authentication/JWT) - Port 8082
- ✅ **Redis** (Rate Limiting Cache) - Port 6379

**Monolith remains untouched** — only used as reference.

---

## Start the Full Microservices Stack

### Option 1: Docker Compose (Recommended)

```powershell
cd D:\Training\qmp\QuantityMeasurementApp
docker compose -f docker-compose.microservices.yml up --build -d
```

**Wait 15-20 seconds for all services to start**, then check:

### Option 2: Local Development (Manual)

Terminal 1 - Start Eureka:
```powershell
cd D:\Training\qmp\QuantityMeasurementApp\eureka-server
mvn spring-boot:run
```

Terminal 2 - Start Auth Service:
```powershell
cd D:\Training\qmp\QuantityMeasurementApp\auth-service
mvn spring-boot:run
```

Terminal 3 - Start API Gateway:
```powershell
cd D:\Training\qmp\QuantityMeasurementApp\api-gateway
mvn spring-boot:run
```

---

## Verify Services Are Running

### Check Eureka Dashboard
Open browser: `http://localhost:8761/`

You should see:
- ✅ `AUTH-SERVICE` registered with status **UP**
- ✅ `API-GATEWAY` registered with status **UP**

### Check Service Registry API
```powershell
Invoke-WebRequest -Uri "http://localhost:8761/eureka/apps" -UseBasicParsing | Select-Object -ExpandProperty Content
```

Should show both services in XML format.

### Check API Gateway Health
```powershell
Invoke-WebRequest -Uri "http://localhost:8081/actuator/health" -UseBasicParsing | ConvertFrom-Json | ConvertTo-Json
```

Should return:
```json
{
  "status": "UP"
}
```

### Check Auth Service Health
```powershell
Invoke-WebRequest -Uri "http://localhost:8082/actuator/health" -UseBasicParsing | ConvertFrom-Json | ConvertTo-Json
```

---

## Test Auth Service Through Gateway

### Register New User
```powershell
$body = @{
    username = "testuser"
    email = "test@example.com"
    password = "password123"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8081/api/v1/auth/register" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body $body
```

### Login
```powershell
$body = @{
    username = "testuser"
    password = "password123"
} | ConvertTo-Json

$response = Invoke-WebRequest -Uri "http://localhost:8081/api/v1/auth/login" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body $body

$response.Content | ConvertFrom-Json | ConvertTo-Json
```

Expected response includes JWT token:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "testuser",
  "email": "test@example.com"
}
```

---

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────┐
│                    Client Application                   │
└────────────────────────┬────────────────────────────────┘
                         │
                         ▼
          ┌──────────────────────────────┐
          │      API GATEWAY (8081)       │
          │  - Rate Limiting (Redis)      │
          │  - Circuit Breaker            │
          │  - Request Routing            │
          └───────────┬─────────┬─────────┘
                      │         │
          ┌───────────▼──┐   ┌──▼──────────────┐
          │  AUTH SERVICE │   │ QUANTITY SERVICE│
          │     (8082)    │   │    (TBD)        │
          │   - JWT Tokens│   │ - Conversions   │
          │   - OAuth2    │   │ - Measurement  │
          └───────────┬──┘   └──┬──────────────┘
                      │         │
          ┌───────────▼─────────▼─────────┐
          │    EUREKA SERVER (8761)        │
          │  - Service Registration        │
          │  - Service Discovery          │
          └────────────────────────────────┘
```

---

## File Structure

```
QuantityMeasurementApp/
├── eureka-server/                    ✅ Service Registry
├── api-gateway/                      ✅ Request Router
├── auth-service/                     ✅ NEW - Auth & JWT
├── monolith/                         📖 Reference only
├── docker-compose.microservices.yml  ✅ Updated
├── README.microservices.md           ✅ Updated
└── AUTH_SERVICE_IMPLEMENTATION.md    📋 This summary
```

---

## Next: Extract Quantity Measurement Service

When ready, repeat the pattern:
1. Create `quantity-service/` module
2. Copy business logic from `monolith/src/main/java/com/app/quantitymeasurement/`
3. Add Eureka client + YAML config
4. Update gateway routing to `lb://quantity-service`
5. Deploy and verify registration

---

## Troubleshooting

### Services not appearing in Eureka?
- Check Eureka server is running on 8761
- Verify `eureka.client.service-url.defaultZone` is correct
- Wait 10 seconds for heartbeat/registration

### Gateway can't reach auth-service?
- Confirm both services are registered in Eureka
- Check service health endpoints
- Verify no port conflicts (8081, 8082, 8761)

### Rate limiting errors?
- Ensure Redis is running on 6379
- Check `SPRING_DATA_REDIS_HOST` environment variable

---

## Stopping Services

### Docker Compose:
```powershell
docker compose -f docker-compose.microservices.yml down
```

### Manual:
- Stop each Terminal with `Ctrl+C`

---

**Auth Service is production-ready!** 🚀 Ready for quantity-service extraction?

