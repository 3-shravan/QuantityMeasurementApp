# Project Flow (End-to-End)

## 1) Request lifecycle

For a request like `POST /api/v1/quantities/add`:

1. **HTTP request arrives at Controller**
   - `QuantityMeasurementController.addQuantities(...)`
   - body mapped into `QuantityInputDTO`
   - nested `QuantityDTO` objects validated via `@Valid`

2. **Validation phase (Jakarta Validation)**
   - checks for null/blank fields
   - checks measurement type pattern
   - custom `@AssertTrue` verifies unit belongs to specified measurement type
   - on failure: `MethodArgumentNotValidException` thrown

3. **Global exception handler intercepts errors**
   - `GlobalExceptionHandler.handleMethodArgumentNotValidException(...)`
   - returns HTTP 400 with `ErrorResponse`

4. **Service executes business logic**
   - converts DTOs to `QuantityModel<IMeasurable>`
   - resolves unit enum (`LengthUnit`, `VolumeUnit`, etc.)
   - validates compatibility (same measurement category)
   - computes result using `Quantity` domain class

5. **Persistence**
   - success and error operations are persisted using `QuantityMeasurementRepository`
   - `QuantityMeasurementEntity` stores inputs, output, operation type, error info, timestamps

6. **Response mapping**
   - entity converted to `QuantityMeasurementDTO`
   - returned as JSON with HTTP 200 (or 400/500 on errors)

---

## 2) Class-by-class role mapping

## Entry point

- `QuantityMeasurementApplication`
  - bootstraps Spring context
  - enables component scanning
  - enables auto-configuration

## Controller

- `QuantityMeasurementController`
  - REST endpoints under `/api/v1/quantities`
  - receives and validates request payloads
  - delegates to `IQuantityMeasurementService`

## Service contract and implementation

- `IQuantityMeasurementService`
  - defines business operations and history APIs
- `QuantityMeasurementServiceImpl`
  - core business logic
  - conversion and arithmetic orchestration
  - error recording and exception wrapping

## Domain math

- `Quantity<U extends IMeasurable>`
  - generic value + unit wrapper
  - supports compare/convert/add/subtract/divide
  - converts to base unit for compatibility-safe math

## Unit model

- `IMeasurable`
  - conversion abstraction
  - default conversion to/from base unit
  - operation support checks (e.g., temperature arithmetic disabled)
- `LengthUnit`, `WeightUnit`, `VolumeUnit`, `TemperatureUnit`
  - enum-based unit systems

## Persistence

- `QuantityMeasurementEntity`
  - JPA entity mapped to table `quantity_measurement_entity`
  - indexed by operation, measurement type, createdAt
- `QuantityMeasurementRepository`
  - extends `JpaRepository<QuantityMeasurementEntity, Long>`
  - derived query methods + custom JPQL method

## DTOs

- `QuantityInputDTO`
  - wraps `thisQuantityDTO` + `thatQuantityDTO`
- `QuantityDTO`
  - input quantity
  - validation constraints + custom unit/type consistency check
- `QuantityMeasurementDTO`
  - output history/result payload

## Exceptions

- `QuantityMeasurementException`
  - domain-specific exception
- `GlobalExceptionHandler`
  - transforms exceptions to consistent API response
- `ErrorResponse`
  - standardized error payload

## Security

- `SecurityConfig`
  - defines `SecurityFilterChain`
  - allows API, docs, actuator, H2 console in current UC
  - CSRF disabled (required for H2 console and dev API testing)

---

## 3) Data model semantics

Stored operation row includes:

- input side (`this*`)
- second input (`that*`)
- operation (`compare`, `convert`, `add`, `subtract`, `divide`)
- result fields (`resultString`, `resultValue`, `resultUnit`, `resultMeasurementType`)
- error metadata (`error`, `errorMessage`)
- timestamps (`createdAt`, `updatedAt`)

This design supports both:

- result retrieval
- audit/history analysis
- error history reporting

---

## 4) Why both DTO and Entity?

- **Entity** represents DB shape and persistence concerns
- **DTO** represents API contract
- this separation avoids leaking persistence model into API and keeps future refactoring safer

---

## 5) Operation-specific flow

## compare

- converts both quantities into same-category model
- equality computed with tolerance in `Quantity.equals`
- saves `resultString = true|false`

## convert

- first quantity is source
- second quantity provides target unit
- result numeric stored in `resultValue`

## add/subtract

- validates same category
- performs base-unit operation and converts to chosen unit
- stores `resultValue` + result unit + result measurement type

## divide

- validates same category
- denominator zero => arithmetic exception path
- global handler returns 500 for unexpected runtime

---

## 6) Read APIs flow

- `GET /history/operation/{operation}` -> repository `findByOperation`
- `GET /history/type/{measurementType}` -> repository `findByThisMeasurementType`
- `GET /history/errored` -> repository `findByErrorTrue`
- `GET /count/{operation}` -> repository `countByOperationAndErrorFalse`
