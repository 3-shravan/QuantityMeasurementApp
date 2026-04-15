# Quantity Measurement Backend Flow Guide

## 1. What this backend is

This project is a Spring Boot backend for measuring quantities across multiple domains:

- Length
- Weight
- Volume
- Temperature

It exposes REST APIs for:

- compare
- convert
- add
- subtract
- divide
- operation history
- measurement-type history
- error history
- operation count
- authentication with username/password
- Google OAuth2 login
- JWT-based session handling

The main goal is to let the client send two quantities, perform a valid operation, store the result, and later fetch user-specific history.

---

## 2. High-level architecture

The backend follows a layered flow:

1. HTTP request enters a controller
2. Controller validates request DTOs
3. Service converts DTOs into domain objects
4. Domain objects perform unit conversion and arithmetic
5. Service persists the operation result in the database
6. Controller returns a response DTO
7. Any validation or runtime errors are converted into a standard error response

The important layers are:

- Controller layer: request/response handling
- Service layer: business logic
- Domain layer: quantity math and unit conversion
- Repository layer: JPA persistence
- Security layer: authentication, JWT filter, current-user lookup
- Exception layer: consistent error responses

---

## 3. Application startup flow

### Main entry point

The application starts from [src/main/java/com/app/quantitymeasurement/QuantityMeasurementApplication.java](src/main/java/com/app/quantitymeasurement/QuantityMeasurementApplication.java).

On startup Spring Boot does the following:

- scans all components under `com.app.quantitymeasurement`
- wires controllers, services, repositories, filters, and configs
- configures OpenAPI metadata for Swagger UI
- connects to the active datasource
- builds the security filter chain

### Active profile

The runtime profile is controlled by `spring.profiles.active` in [src/main/resources/application.properties](src/main/resources/application.properties).

Common profiles:

- `dev` for local development
- `prod` for production-style deployment
- `test` for tests

---

## 4. Main packages and what they do

### 4.1 `controller`

Contains REST endpoints.

- [HomeController.java](src/main/java/com/app/quantitymeasurement/controller/HomeController.java)
- [QuantityMeasurementController.java](src/main/java/com/app/quantitymeasurement/controller/QuantityMeasurementController.java)
- [auth/controller/AuthController.java](src/main/java/com/app/quantitymeasurement/auth/controller/AuthController.java)

### 4.2 `service`

Contains the business rules.

- [QuantityMeasurementServiceImpl.java](src/main/java/com/app/quantitymeasurement/service/QuantityMeasurementServiceImpl.java)
- [auth/service/AuthService.java](src/main/java/com/app/quantitymeasurement/auth/service/AuthService.java)
- [auth/service/OAuth2Service.java](src/main/java/com/app/quantitymeasurement/auth/service/OAuth2Service.java)

### 4.3 `domain`

Contains reusable math logic.

- [domain/quantity/Quantity.java](src/main/java/com/app/quantitymeasurement/domain/quantity/Quantity.java)

### 4.4 `unit`

Contains measurement unit enums and conversion behavior.

- [IMeasurable.java](src/main/java/com/app/quantitymeasurement/unit/IMeasurable.java)
- [LengthUnit.java](src/main/java/com/app/quantitymeasurement/unit/LengthUnit.java)
- [WeightUnit.java](src/main/java/com/app/quantitymeasurement/unit/WeightUnit.java)
- [VolumeUnit.java](src/main/java/com/app/quantitymeasurement/unit/VolumeUnit.java)
- [TemperatureUnit.java](src/main/java/com/app/quantitymeasurement/unit/TemperatureUnit.java)

### 4.5 `model`

Contains DTOs and entity model for quantity history.

- [QuantityDTO.java](src/main/java/com/app/quantitymeasurement/model/QuantityDTO.java)
- [QuantityInputDTO.java](src/main/java/com/app/quantitymeasurement/model/QuantityInputDTO.java)
- [QuantityMeasurementDTO.java](src/main/java/com/app/quantitymeasurement/model/QuantityMeasurementDTO.java)
- [QuantityMeasurementEntity.java](src/main/java/com/app/quantitymeasurement/model/QuantityMeasurementEntity.java)

### 4.6 `auth`

Contains login, signup, JWT, and OAuth2 support.

- DTOs
- JPA user entity
- security filter
- JWT token provider
- user details service
- auth controller/service

### 4.7 `repository`

Contains JPA repositories.

- [QuantityMeasurementRepository.java](src/main/java/com/app/quantitymeasurement/repository/QuantityMeasurementRepository.java)
- [auth/repository/UserRepository.java](src/main/java/com/app/quantitymeasurement/auth/repository/UserRepository.java)

### 4.8 `exception`

Contains centralized error handling.

- [GlobalExceptionHandler.java](src/main/java/com/app/quantitymeasurement/exception/GlobalExceptionHandler.java)
- [ErrorResponse.java](src/main/java/com/app/quantitymeasurement/exception/ErrorResponse.java)
- custom exception classes

---

## 5. Security model in simple words

This backend uses JWT for authentication.

### What the client does

1. Register or login
2. Receive a JWT token
3. Store the token on the frontend
4. Send it in the `Authorization` header as:

```http
Authorization: Bearer <token>
```

### What the backend does

1. Reads the token from the request header
2. Validates the JWT signature and expiration
3. Extracts the username
4. Loads the user from the database
5. Places the authenticated user in the Spring Security context

### Important note

The quantity operation endpoints are publicly accessible in security config, but history and count data are user-specific in the service layer. If no valid authenticated user is found, those endpoints return:

- empty list for history
- `0` for count

---

## 6. Authentication flow

## 6.1 Register

Endpoint: `POST /api/v1/auth/register`

Controller: [AuthController.java](src/main/java/com/app/quantitymeasurement/auth/controller/AuthController.java)

Service: [AuthService.java](src/main/java/com/app/quantitymeasurement/auth/service/AuthService.java)

Flow:

1. Client sends username, email, and password
2. Request is validated by Bean Validation
3. Service checks whether username already exists
4. Service checks whether email already exists
5. Password is encoded using BCrypt
6. New `User` entity is saved in `users`
7. JWT token is generated from the username
8. Response returns token, username, and userId

### Result

The user is created and immediately authenticated.

---

## 6.2 Login

Endpoint: `POST /api/v1/auth/login`

Flow:

1. Client sends username and password
2. `AuthenticationManager` authenticates the credentials
3. `CustomUserDetailsService` loads the stored user
4. `JwtTokenProvider` generates a JWT token
5. Response returns the token, username, and userId

If credentials are invalid, the exception handler returns an unauthorized error.

---

## 6.3 Logout

Endpoint: `POST /api/v1/auth/logout`

Flow:

1. Backend reads the current authentication from `SecurityContextHolder`
2. Security context is cleared
3. A success response is returned

Important: JWT is stateless, so logout does not delete the token from the server. The client must remove the token locally.

---

## 6.4 Google OAuth2 login

Endpoint: `POST /api/v1/auth/oauth2/google`

Flow:

1. Client sends Google access token
2. `OAuth2Service.getGoogleUserInfo()` calls Google userinfo API using `RestTemplate`
3. Backend extracts Google user identity data
4. If a user with that email already exists, reuse it
5. If not, create a new local user
6. Generate JWT token for the local account
7. Return token to the client

This means Google acts as identity provider, while the app still stores its own local user record.

---

## 7. Quantity operation flow

All quantity operations use the same request wrapper:

- `thisQuantityDTO`
- `thatQuantityDTO`

This wrapper is [QuantityInputDTO.java](src/main/java/com/app/quantitymeasurement/model/QuantityInputDTO.java).

### Shared request shape

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

---

## 8. Quantity operation request lifecycle

The flow is the same for compare, convert, add, subtract, and divide.

### Step 1: Request enters controller

`QuantityMeasurementController` receives the request.

### Step 2: DTO validation

The following checks are applied:

- value must be present
- unit must not be blank
- measurement type must not be blank
- measurement type must be one of:
  - `LengthUnit`
  - `WeightUnit`
  - `VolumeUnit`
  - `TemperatureUnit`
- the unit must belong to the selected measurement type

### Step 3: Service layer transforms DTOs

The service converts each `QuantityDTO` into a `QuantityModel<IMeasurable>`.

### Step 4: Unit resolution

The unit string is mapped to the correct enum:

- `LengthUnit`
- `WeightUnit`
- `VolumeUnit`
- `TemperatureUnit`

### Step 5: Compatibility check

The service ensures both quantities belong to the same measurement category.

If one quantity is length and the other is weight, the request is rejected.

### Step 6: Domain operation

A `Quantity<IMeasurable>` object performs the actual conversion or arithmetic.

### Step 7: Persistence

The service stores the operation in `quantity_measurement_entity`.

### Step 8: Response

The stored entity is mapped back to `QuantityMeasurementDTO` and returned.

---

## 9. Operation-by-operation flow

## 9.1 Compare

Endpoint: `POST /api/v1/quantities/compare`

Service method: `compare()`

Flow:

1. Convert both quantities into domain objects
2. Check same measurement category
3. Convert both values to their base unit
4. Compare using `Quantity.equals()`
5. Save result in history

Behavior:

- returns `true` or `false`
- result is stored as `resultString`

Example:

- `1 FEET` vs `12 INCHES` → `true`

---

## 9.2 Convert

Endpoint: `POST /api/v1/quantities/convert`

Service method: `convert()`

Flow:

1. Convert source and target DTOs into domain objects
2. Validate same category
3. Convert source value to base unit
4. Convert base unit into target unit
5. Save result

Behavior:

- result is returned in `resultValue`
- the target unit defines the output conversion target

Example:

- `1 FEET` to `INCHES` → `12.0`

---

## 9.3 Add

Endpoint: `POST /api/v1/quantities/add`

Service method: `add()`

Flow:

1. Convert both quantities
2. Validate same category
3. Convert both to base unit
4. Add the base values
5. Convert the sum back to the left-hand unit
6. Save result

Behavior:

- result value is returned
- result unit is usually the left quantity unit
- result measurement type is stored

Example:

- `1 FEET + 12 INCHES` → `2 FEET`

---

## 9.4 Subtract

Endpoint: `POST /api/v1/quantities/subtract`

Service method: `subtract()`

Flow:

1. Convert both quantities
2. Validate same category
3. Convert both to base unit
4. Subtract right-hand side from left-hand side
5. Convert the difference back to the left-hand unit
6. Save result

Example:

- `2 FEET - 12 INCHES` → `1 FEET`

---

## 9.5 Divide

Endpoint: `POST /api/v1/quantities/divide`

Service method: `divide()`

Flow:

1. Convert both quantities
2. Validate same category
3. Convert both to base unit
4. Divide first by second
5. Save result

Behavior:

- returns a dimensionless ratio
- division by zero is rejected

Example:

- `2 FEET / 1 FEET` → `2.0`

---

## 10. Domain layer: how quantity math works

The core math lives in [domain/quantity/Quantity.java](src/main/java/com/app/quantitymeasurement/domain/quantity/Quantity.java).

### Quantity object

A `Quantity<U>` stores:

- numeric value
- unit enum

### Why this is important

This keeps arithmetic behavior out of the controller and service code.

### Main responsibilities

- validate numeric values
- validate unit compatibility
- convert to base unit
- convert from base unit
- add quantities
- subtract quantities
- divide quantities
- compare quantities

### Equality logic

Two quantities are considered equal if:

- they belong to the same measurement category
- their base-unit values are almost equal
- the difference is less than `1e-3`

This small tolerance prevents floating-point noise.

---

## 11. Unit conversion model

Each unit enum implements [IMeasurable.java](src/main/java/com/app/quantitymeasurement/unit/IMeasurable.java).

### Base-unit idea

Each measurement category uses a base unit internally.

Examples:

- Length base unit: feet
- Weight base unit: kilogram
- Volume base unit: litre
- Temperature base unit: celsius

### Length units

Defined in [LengthUnit.java](src/main/java/com/app/quantitymeasurement/unit/LengthUnit.java):

- FEET
- INCHES
- YARDS
- CENTIMETERS

### Weight units

Defined in [WeightUnit.java](src/main/java/com/app/quantitymeasurement/unit/WeightUnit.java):

- KILOGRAM
- GRAM
- POUND

### Volume units

Defined in [VolumeUnit.java](src/main/java/com/app/quantitymeasurement/unit/VolumeUnit.java):

- LITRE
- MILLILITRE
- GALLON

### Temperature units

Defined in [TemperatureUnit.java](src/main/java/com/app/quantitymeasurement/unit/TemperatureUnit.java):

- CELSIUS
- FAHRENHEIT
- KELVIN

### Special case: temperature

Temperature does not behave like the other units.

- arithmetic is disabled
- conversion uses real formulas
- Celsius is the internal base scale

That is why `supportsArithmetic()` returns `false` for temperature.

---

## 12. Persistence flow

### Quantity history table

The entity is [QuantityMeasurementEntity.java](src/main/java/com/app/quantitymeasurement/model/QuantityMeasurementEntity.java).

It stores:

- both input values and units
- both measurement types
- operation name
- result string or result value
- result unit and measurement type
- error flag
- error message
- userId
- createdAt timestamp

### User table

Authentication users are stored in [auth/entity/User.java](src/main/java/com/app/quantitymeasurement/auth/entity/User.java).

It stores:

- username
- email
- password
- enabled flag

### Repository layer

`QuantityMeasurementRepository` provides queries for:

- operation history
- measurement-type history
- error history
- successful operation counts
- user-specific lookups

### User-specific history

The service uses `SecurityUtil.getCurrentUserId()` to attach history to the authenticated user.

This means:

- history is isolated per user
- unauthenticated requests do not get user history

---

## 13. How history endpoints work

Available endpoints:

- `GET /api/v1/quantities/history/operation/{operation}`
- `GET /api/v1/quantities/history/type/{measurementType}`
- `GET /api/v1/quantities/history/errored`
- `GET /api/v1/quantities/count/{operation}`

### Operation history

Returns saved operations for the current user.

### Measurement type history

Returns saved operations for the current user filtered by measurement type.

### Error history

Returns failed operations for the current user.

### Operation count

Returns count of successful operations for the current user.

### Important behavior if not authenticated

- history endpoints return an empty list
- count endpoint returns `0`

---

## 14. Error handling flow

All exceptions go through [GlobalExceptionHandler.java](src/main/java/com/app/quantitymeasurement/exception/GlobalExceptionHandler.java).

### Common cases handled

- invalid DTO fields
- malformed JSON
- invalid path variable types
- domain validation failures
- custom application errors
- bad credentials
- resource not found
- unexpected exceptions

### Standard error response

The backend returns [ErrorResponse.java](src/main/java/com/app/quantitymeasurement/exception/ErrorResponse.java):

```json
{
  "timestamp": "2026-04-08T10:20:30",
  "status": 400,
  "error": "Quantity Measurement Error",
  "message": "Meaningful error text",
  "path": "/api/v1/quantities/add"
}
```

### Why this matters

The client always gets a predictable error body instead of random stack traces.

---

## 15. Security configuration flow

Security is configured in [config/SecurityConfig.java](src/main/java/com/app/quantitymeasurement/config/SecurityConfig.java).

### What is allowed publicly

- `/`
- `/error`
- `/swagger-ui/**`
- `/swagger-ui.html`
- `/v3/api-docs/**`
- `/h2-console/**`
- `/actuator/**`
- `/api/v1/auth/**`
- `/api/v1/quantities/**`

### Session policy

The app is stateless:

- no HTTP session is kept for authentication
- JWT carries the identity

### CORS

Allowed origins are configured from `app.cors.allowed-origins`.

This enables frontend apps to call the backend from localhost or deployed domains.

---

## 16. Important config values

Found in [src/main/resources/application.properties](src/main/resources/application.properties), [src/main/resources/application-dev.properties](src/main/resources/application-dev.properties), and [src/main/resources/application-prod.properties](src/main/resources/application-prod.properties).

### Common settings

- app name
- JWT secret
- JWT expiration
- Swagger path
- CORS allowed origins
- database connection
- Hibernate DDL mode
- OAuth2 Google client settings

### Dev profile

Typically used with local MySQL and verbose logging.

### Prod profile

Uses safer logging, production DB settings, and stricter startup defaults.

---

## 17. End-to-end examples

## 17.1 Example: register and add quantities

1. User registers via `/api/v1/auth/register`
2. Backend returns JWT token
3. Client stores token
4. Client sends request to `/api/v1/quantities/add`
5. Backend validates input
6. Backend adds the quantities
7. Result is saved in the history table
8. Client receives the computed result

## 17.2 Example: history lookup

1. Client sends request to `/api/v1/quantities/history/operation/add`
2. JWT filter authenticates the user
3. Service reads current userId
4. Repository fetches only that user’s saved add operations
5. Controller returns the list

## 17.3 Example: invalid request

1. Client sends `LengthUnit` with `KILOGRAM`
2. DTO validation fails
3. Global exception handler returns a 400 error response
4. No valid operation is persisted

## 17.4 Example: temperature math

1. Client sends `temperature` quantities
2. Units are resolved to `TemperatureUnit`
3. Conversion formulas are used
4. Arithmetic is blocked if unsupported
5. Result is returned or rejected based on the operation

---

## 18. Request lifecycle summary

A request typically goes through this path:

```text
HTTP request
  -> Controller
  -> Bean Validation
  -> Service
  -> DTO to domain conversion
  -> Unit validation
  -> Quantity math
  -> Repository save or query
  -> DTO response
```

For authenticated requests:

```text
HTTP request
  -> JWT filter
  -> SecurityContext
  -> Service reads current userId
  -> User-specific persistence/query
```

---

## 19. Mental model to remember

If you want to understand the app quickly, remember these rules:

1. Controllers are thin
2. Services contain the business rules
3. `Quantity` does the math
4. Unit enums define conversion behavior
5. JPA stores every operation
6. JWT identifies the user
7. History is user-specific
8. Errors always return a standard JSON body

---

## 20. Short conclusion

This backend is a layered Spring Boot system where:

- auth creates the user identity
- JWT carries identity across requests
- quantity endpoints perform domain-safe measurement operations
- operations are persisted for history and analytics
- exception handling keeps responses consistent

If you understand the path from controller to service to domain to repository, you understand the whole app.
