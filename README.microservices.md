# Microservices Local Stack (Monolith Untouched)

This stack runs only microservices infrastructure components and keeps `monolith/` unchanged.

Included services:
- `eureka-server`
- `api-gateway`
- `redis` (required for gateway `RequestRateLimiter`)

## Why Separate From Monolith

- `monolith/` is used only as reference for endpoint/service extraction.
- No changes are required in `monolith/` to run this stack.

## Start (PowerShell)

```powershell
Set-Location "D:\Training\qmp\QuantityMeasurementApp"
docker compose -f docker-compose.microservices.yml up --build -d
```

## Stop (PowerShell)

```powershell
Set-Location "D:\Training\qmp\QuantityMeasurementApp"
docker compose -f docker-compose.microservices.yml down
```

## Check Endpoints

- Eureka dashboard: `http://localhost:8761/`
- Eureka apps API: `http://localhost:8761/eureka/apps`
- Gateway health: `http://localhost:8081/actuator/health`
- Gateway routes: `http://localhost:8081/actuator/gateway/routes`

## Notes

- `api-gateway` currently routes `/api/v1/auth/**` and `/api/v1/quantities/**` to service id `quantity-measurement-app` by default.
- Set `QUANTITY_SERVICE_NAME` if your extracted downstream service registers with a different id.
- If no downstream quantity/auth service is registered yet, gateway routes will exist but upstream calls will fail until service registration happens.

