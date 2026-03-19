# Quantity Measurement App (UC17)

A Spring Boot REST API for quantity operations (compare, convert, add, subtract, divide) with:

- validation
- centralized exception handling
- persistence using Spring Data JPA
- in-memory H2 database (dev default)
- optional MySQL profile (prod)
- Swagger/OpenAPI docs
- Actuator endpoints
- controller and integration tests

---

## 1) What this project does

This application accepts two quantity inputs and performs unit-safe operations:

- compare two quantities (example: `1 FEET` vs `12 INCHES`)
- convert one quantity to target unit
- add/subtract compatible quantities
- divide compatible quantities
- store operation history (including failures)
- fetch history by operation type / measurement type / errors
- return operation count by operation type

The API base path is:

`/api/v1/quantities`

---

## 2) High-level architecture

The project follows a standard layered Spring architecture:

1. **Controller layer**
   - receives HTTP requests
   - validates request DTOs
   - delegates business logic to service
   - returns structured response

2. **Service layer**
   - core business rules
   - unit compatibility checks
   - quantity math via domain class
   - maps operation results to entity/DTO
   - persists success/failure history

3. **Repository layer**
   - Spring Data JPA repository interface
   - query methods based on naming conventions
   - custom JPQL query where needed

4. **Model layer**
   - request/response DTOs
   - JPA entity (database mapped)
   - enums and wrappers

5. **Cross-cutting**
   - security config (allow-all in current UC)
   - global exception handling
   - validation
   - OpenAPI docs and actuator

See details in [docs/PROJECT_FLOW.md](docs/PROJECT_FLOW.md).

---

## 3) Tech stack

- Java 17
- Spring Boot 3.4.3
- Spring Web (REST)
- Spring Validation (Jakarta Validation)
- Spring Data JPA + Hibernate
- Spring Security (basic foundation config)
- H2 (dev), MySQL driver (prod)
- Springdoc OpenAPI (Swagger UI)
- Spring Boot Actuator
- JUnit 5 + MockMvc + TestRestTemplate + Mockito

Dependency details are in [docs/SPRING_THEORY_GUIDE.md](docs/SPRING_THEORY_GUIDE.md).

---

## 4) Run locally

### Prerequisites

- Java 17+
- Maven 3.9+

### Start app

```bash
mvn spring-boot:run
```

### Run tests

```bash
mvn test
```

### Build package

```bash
mvn clean package
```

---

## 5) Useful URLs

After startup:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- H2 Console: `http://localhost:8080/h2-console`
- Actuator Health: `http://localhost:8080/actuator/health`
- Actuator Metrics: `http://localhost:8080/actuator/metrics`

H2 default datasource from properties:

- JDBC URL: `jdbc:h2:mem:quantitymeasurementdb`
- username: `sa`
- password: _(blank)_

---

## 6) API endpoints summary

| Method | Endpoint                                            | Description                     |
| ------ | --------------------------------------------------- | ------------------------------- |
| POST   | `/api/v1/quantities/compare`                        | Compare two quantities          |
| POST   | `/api/v1/quantities/convert`                        | Convert quantity to target unit |
| POST   | `/api/v1/quantities/add`                            | Add two quantities              |
| POST   | `/api/v1/quantities/subtract`                       | Subtract two quantities         |
| POST   | `/api/v1/quantities/divide`                         | Divide two quantities           |
| GET    | `/api/v1/quantities/history/operation/{operation}`  | History by operation            |
| GET    | `/api/v1/quantities/history/type/{measurementType}` | History by measurement type     |
| GET    | `/api/v1/quantities/history/errored`                | Only failed operations          |
| GET    | `/api/v1/quantities/count/{operation}`              | Count successful operations     |

Full examples are in [docs/API_REFERENCE.md](docs/API_REFERENCE.md).

---

## 7) Project structure

```text
src/main/java/com/app/quantitymeasurement
  QuantityMeasurementApplication.java
  config/
    SecurityConfig.java
  controller/
    QuantityMeasurementController.java
  exception/
    ErrorResponse.java
    GlobalExceptionHandler.java
    QuantityMeasurementException.java
  model/
    OperationType.java
    QuantityDTO.java
    QuantityInputDTO.java
    QuantityMeasurementDTO.java
    QuantityMeasurementEntity.java
    QuantityModel.java
  quantity/
    Quantity.java
  repository/
    QuantityMeasurementRepository.java
  service/
    IQuantityMeasurementService.java
    QuantityMeasurementService.java
    QuantityMeasurementServiceImpl.java
  unit/
    IMeasurable.java
    LengthUnit.java
    VolumeUnit.java
    WeightUnit.java
    TemperatureUnit.java

src/main/resources
  application.properties
  application-prod.properties

src/test/java/com/app/quantitymeasurement
  QuantityMeasurementApplicationTests.java
  controller/QuantityMeasurementControllerTest.java
  integrationTests/QuantityMeasurementIntegrationTest.java
```

---

## 8) Notes about legacy UC16 files

This workspace still contains some UC16 source/test files for reference. In `pom.xml`, compiler/surefire exclusions are configured so the active UC17 Spring Boot build compiles and runs the intended code path.

---

## 9) Documentation map

- Architecture + execution flow: [docs/PROJECT_FLOW.md](docs/PROJECT_FLOW.md)
- Full Spring theory used here: [docs/SPRING_THEORY_GUIDE.md](docs/SPRING_THEORY_GUIDE.md)
- Endpoint examples and payloads: [docs/API_REFERENCE.md](docs/API_REFERENCE.md)
- Testing strategy and report generation: [docs/TESTING_GUIDE.md](docs/TESTING_GUIDE.md)

---

## 10) One-line explanation for interview/demo

> This is a layered Spring Boot REST application where validated quantity inputs flow through Controller → Service → JPA Repository, with domain-safe unit math, centralized error handling, operation auditing in DB, OpenAPI docs, and test coverage at web and integration levels.
