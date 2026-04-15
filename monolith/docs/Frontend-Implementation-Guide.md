# Frontend Implementation Guide

## 1) Purpose

This document is the frontend implementation reference for the Quantity Measurement App backend. It defines:

- End-to-end feature scope
- API contracts (request/response/error)
- Authentication and token usage
- UX and validation rules
- Recommended frontend architecture and quality checklist

Use this as the source of truth for implementing all frontend features professionally.

---

## 2) Environment and Base URLs

### Local backend

- Base URL: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### API prefixes

- Auth APIs: `/api/v1/auth`
- Quantity APIs: `/api/v1/quantities`

---

## 3) Feature Scope (Frontend)

Implement these modules:

1. Authentication
   - Register (username/email/password)
   - Login (username/password)
   - Logout
   - Google OAuth login using Google access token
2. Quantity operations
   - Compare
   - Convert
   - Add
   - Subtract
   - Divide
3. History and analytics
   - History by operation
   - History by measurement type
   - Error history
   - Operation count
4. UX and quality
   - Request/response loading states
   - Error handling and user-friendly messages
   - Validation before API calls
   - Token persistence and session restore

---

## 4) Authentication Model

## 4.1 Token type

Backend returns JWT with `type = "Bearer"`.

Store token and attach header:

`Authorization: Bearer <token>`

## 4.2 Important behavior

- Quantity operation endpoints are public and can be called without token.
- History and count data are user-specific in service logic.
  - Without valid token, history endpoints return empty list and count returns `0`.
- Therefore, frontend should always attach token for history/count features.

## 4.3 Token lifecycle

- Token expiration backend config defaults to 24 hours.
- No refresh-token endpoint exists.
- On 401, clear session and redirect to login.

---

## 5) Domain Values and Enums

## 5.1 Measurement types (exact values)

- `LengthUnit`
- `WeightUnit`
- `VolumeUnit`
- `TemperatureUnit`

## 5.2 Units by type

### LengthUnit

- `FEET`
- `INCHES`
- `YARDS`
- `CENTIMETERS`

### WeightUnit

- `KILOGRAM`
- `GRAM`
- `POUND`

### VolumeUnit

- `LITRE`
- `MILLILITRE`
- `GALLON`

### TemperatureUnit

- `CELSIUS`
- `FAHRENHEIT`
- `KELVIN`

## 5.3 Operation path enum values

Used in URL path variables:

- `ADD`
- `SUBTRACT`
- `MULTIPLY` (enum exists; API endpoint for multiply is not exposed)
- `DIVIDE`
- `COMPARE`
- `CONVERT`

Use uppercase values in URLs for operation-based endpoints.

---

## 6) Request/Response Contracts

## 6.1 Shared request DTO for quantity operations

```json
{
  "thisQuantityDTO": {
    "value": 1.0,
    "unit": "FEET",
    "measurementType": "LengthUnit"
  },
  "thatQuantityDTO": {
    "value": 12.0,
    "unit": "INCHES",
    "measurementType": "LengthUnit"
  }
}
```

### Validation expectations

- `value`: required numeric
- `unit`: required, non-empty
- `measurementType`: required and must be one of allowed values
- `unit` must belong to selected `measurementType`

## 6.2 Quantity operation response

All quantity operation endpoints return `QuantityMeasurementDTO`:

```json
{
  "id": 1,
  "thisValue": 1.0,
  "thisUnit": "FEET",
  "thisMeasurementType": "LengthUnit",
  "thatValue": 12.0,
  "thatUnit": "INCHES",
  "thatMeasurementType": "LengthUnit",
  "operation": "compare",
  "resultString": "true",
  "resultValue": null,
  "resultUnit": null,
  "resultMeasurementType": null,
  "errorMessage": null,
  "error": false,
  "createdAt": "2026-04-01T10:20:30"
}
```

### Result field behavior by operation

- Compare: `resultString` set (`"true"`/`"false"`), `resultValue` often null
- Convert: `resultValue` set, `resultUnit` generally null in persisted response
- Add/Subtract: `resultValue`, `resultUnit`, `resultMeasurementType` set
- Divide: `resultValue` set (dimensionless ratio)

## 6.3 Error response DTO

```json
{
  "timestamp": "2026-04-01T10:22:11",
  "status": 400,
  "error": "Quantity Measurement Error",
  "message": "Meaningful error text",
  "path": "/api/v1/quantities/add"
}
```

Common status codes:

- `200` success
- `201` register success
- `400` validation/domain/business error
- `401` unauthorized (token missing/invalid for protected context)
- `404` path/resource not found
- `500` unexpected server error

---

## 7) Auth APIs

## 7.1 Register

- Method: `POST`
- URL: `/api/v1/auth/register`

Request:

```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "secret123"
}
```

Response (`201`):

```json
{
  "token": "<jwt>",
  "type": "Bearer",
  "username": "john_doe",
  "userId": 10
}
```

Validation rules:

- username: required, 3..50 chars
- email: required, valid format
- password: required, 6..100 chars

## 7.2 Login

- Method: `POST`
- URL: `/api/v1/auth/login`

Request:

```json
{
  "username": "john_doe",
  "password": "secret123"
}
```

Response (`200`): same as register response DTO.

## 7.3 Logout

- Method: `POST`
- URL: `/api/v1/auth/logout`
- Header: `Authorization: Bearer <token>`

Response (`200`):

```json
{
  "message": "Successfully logged out. Please remove the JWT token from client side.",
  "success": true,
  "username": "john_doe"
}
```

Frontend must always clear local token/session state after logout response.

## 7.4 Google OAuth login

- Method: `POST`
- URL: `/api/v1/auth/oauth2/google`

Request:

```json
{
  "provider": "google",
  "accessToken": "<google_access_token>"
}
```

Response (`200`): same JWT response DTO.

---

## 8) Quantity APIs

## 8.1 Compare

- `POST /api/v1/quantities/compare`
- Body: shared quantity request DTO
- Returns boolean-like result in `resultString`

## 8.2 Convert

- `POST /api/v1/quantities/convert`
- Body: shared quantity request DTO
- `thisQuantityDTO` = source quantity
- `thatQuantityDTO.unit` = target unit
- `thatQuantityDTO.value` is not used for conversion math (set to 0 in frontend for clarity)

## 8.3 Add

- `POST /api/v1/quantities/add`
- Body: shared DTO
- Result unit follows the first operand unit (`thisQuantityDTO.unit`)

## 8.4 Subtract

- `POST /api/v1/quantities/subtract`
- Body: shared DTO
- Result unit follows first operand unit

## 8.5 Divide

- `POST /api/v1/quantities/divide`
- Body: shared DTO
- Returns numeric ratio in `resultValue`

## 8.6 History by operation

- `GET /api/v1/quantities/history/operation/{operation}`
- Example: `/history/operation/COMPARE`
- Returns `QuantityMeasurementDTO[]`

## 8.7 History by measurement type

- `GET /api/v1/quantities/history/type/{measurementType}`
- Example: `/history/type/LengthUnit`
- Returns `QuantityMeasurementDTO[]`

## 8.8 Errored history

- `GET /api/v1/quantities/history/errored`
- Returns `QuantityMeasurementDTO[]` with `error = true`

## 8.9 Operation count

- `GET /api/v1/quantities/count/{operation}`
- Example: `/count/DIVIDE`
- Returns number (`long`)

---

## 9) Business Rules and UX Constraints

1. Measurement category compatibility is mandatory.
   - You cannot add Length to Weight, etc.
2. Temperature units do not support arithmetic add/subtract/divide.
   - Compare and convert are valid.
3. Divide by zero returns error.
4. Numeric value must be finite (no NaN, no Infinity).
5. Unit text should be sent uppercase to avoid avoidable user errors.
6. For convert screen:
   - Keep source and target selectors within same measurement type.
7. For add/subtract/divide screens:
   - Disable submit when selected measurement type is `TemperatureUnit` for arithmetic operations.

---

## 10) Professional Frontend Architecture (Recommended)

## 10.1 Suggested module structure

- `core/`
  - `apiClient` (axios/fetch wrapper)
  - `authStore` (token/session)
  - interceptors (attach token, global error handling)
- `modules/auth/`
  - pages: Login, Register
  - services: auth service
- `modules/quantity/`
  - pages: Compare, Convert, Add, Subtract, Divide
  - components: QuantityForm, ResultCard
  - services: quantity service
- `modules/history/`
  - pages: OperationHistory, TypeHistory, ErrorHistory, Analytics

## 10.2 API client standards

- Set base URL via environment variable.
- Add request timeout and retry (only for safe idempotent GETs).
- Map backend errors to user-friendly messages.
- Centralized auth interceptor:
  - request: inject bearer token
  - response: on 401 -> clear session -> redirect login

## 10.3 State management

- Use React Query/TanStack Query (or equivalent):
  - mutations for auth and quantity operations
  - queries for history/count
  - cache keys scoped by user ID + filters

## 10.4 Form validation

Use schema validation (zod/yup/class-validator equivalent) to mirror backend rules before submit.

---

## 11) TypeScript Contracts (Frontend)

```ts
export type MeasurementType =
  | "LengthUnit"
  | "WeightUnit"
  | "VolumeUnit"
  | "TemperatureUnit";

export interface QuantityDTO {
  value: number;
  unit: string;
  measurementType: MeasurementType;
  operationType?:
    | "ADD"
    | "SUBTRACT"
    | "MULTIPLY"
    | "DIVIDE"
    | "COMPARE"
    | "CONVERT";
}

export interface QuantityInputDTO {
  thisQuantityDTO: QuantityDTO;
  thatQuantityDTO: QuantityDTO;
}

export interface QuantityMeasurementDTO {
  id: number;
  thisValue: number;
  thisUnit: string;
  thisMeasurementType: string;
  thatValue: number | null;
  thatUnit: string | null;
  thatMeasurementType: string | null;
  operation: string;
  resultString: string | null;
  resultValue: number | null;
  resultUnit: string | null;
  resultMeasurementType: string | null;
  errorMessage: string | null;
  error: boolean;
  createdAt: string;
}

export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface SignUpRequest {
  username: string;
  email: string;
  password: string;
}

export interface OAuth2LoginRequest {
  provider: string;
  accessToken: string;
}

export interface JwtAuthenticationResponse {
  token: string;
  type: "Bearer";
  username: string;
  userId: number;
}

export interface LogoutResponse {
  message: string;
  success: boolean;
  username: string;
}
```

---

## 12) UI Implementation Checklist

## 12.1 Auth

- [ ] Register form with client validation
- [ ] Login form
- [ ] Save token securely (memory + persisted storage policy)
- [ ] Logout action clears state/storage
- [ ] Google login integration with access token submission

## 12.2 Quantity operations

- [ ] Shared reusable quantity input component
- [ ] Dynamic unit dropdown by measurement type
- [ ] Action tabs/buttons: Compare, Convert, Add, Subtract, Divide
- [ ] Operation-specific result rendering
- [ ] Disable invalid operations for temperature arithmetic

## 12.3 History & analytics

- [ ] Operation history page/filter
- [ ] Measurement type history page/filter
- [ ] Error history page
- [ ] Count widgets/cards
- [ ] Empty-state UI for unauthenticated or no-data scenario

## 12.4 Quality

- [ ] Loading skeleton/spinner states
- [ ] API error banners/toasts with backend message fallback
- [ ] Accessibility labels and keyboard navigation
- [ ] Responsive layout
- [ ] Unit and integration tests for forms/services

---

## 13) QA Test Cases (Minimum)

1. Register success + token persisted
2. Register duplicate username/email -> displays server validation
3. Login success/failure
4. Compare equivalent units (1 FEET vs 12 INCHES) -> true
5. Convert GALLON to LITRE -> numeric result
6. Add KILOGRAM + GRAM -> result in first unit
7. Subtract with mixed units same category -> valid result
8. Divide by zero -> proper error handling
9. Add with TemperatureUnit -> operation blocked or handled error
10. History without token -> empty list/0 shown correctly
11. History with token -> user-specific data rendered
12. Token expiration -> forced re-auth flow

---

## 14) Notes for Team Alignment

- Keep request enums and strings exact; backend validation is strict.
- Prefer deriving types directly from OpenAPI in CI to prevent drift.
- If backend introduces sorting/pagination later, make history UI query-parameter ready now.

This guide is ready for direct frontend implementation planning, estimation, and delivery.
