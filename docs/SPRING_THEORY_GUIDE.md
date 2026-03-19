# Spring Theory Guide (Used in This Project)

This document explains the major Spring concepts used in this codebase in interview-friendly language.

---

## 1) Spring Boot fundamentals

## What is Spring Boot?

Spring Boot is opinionated Spring that reduces setup boilerplate. It provides:

- auto-configuration
- embedded server (Tomcat)
- starter dependencies
- production-ready features (Actuator)

## In this project

- `@SpringBootApplication` in `QuantityMeasurementApplication` enables:
  - `@Configuration`
  - `@EnableAutoConfiguration`
  - `@ComponentScan`

---

## 2) Dependency Injection (DI)

## Theory

DI means objects do not create their own dependencies; Spring injects them.
Benefits:

- loose coupling
- easier testing
- cleaner architecture

## In this project

- controller gets service via constructor injection
- service gets repository via `@Autowired` field injection
- Spring manages lifecycles in application context

---

## 3) REST API in Spring MVC

## Theory

Spring MVC maps HTTP requests to Java methods.
Core annotations:

- `@RestController`
- `@RequestMapping`
- `@GetMapping`, `@PostMapping`
- `@RequestBody`
- `@PathVariable`

## In this project

All endpoints are in `QuantityMeasurementController` under `/api/v1/quantities`.

---

## 4) Validation (Jakarta Bean Validation)

## Theory

Validation annotations enforce input constraints before business logic runs.
Examples:

- `@NotNull`
- `@NotBlank`
- `@Pattern`
- `@AssertTrue` for custom rule methods

## In this project

`QuantityDTO` enforces:

- required value/unit/measurementType
- measurement type whitelist
- unit must belong to selected measurement type

`@Valid` in controller triggers validation automatically.

---

## 5) Exception handling with `@ControllerAdvice`

## Theory

Instead of per-method try/catch in controllers, global handlers centralize error mapping.

## In this project

`GlobalExceptionHandler` handles:

- validation exceptions -> 400
- `QuantityMeasurementException` -> 400
- generic exceptions -> 500

Error response shape is standardized via `ErrorResponse`.

---

## 6) Spring Data JPA + Hibernate

## Theory

JPA is the Java ORM standard; Hibernate is a common implementation.
Spring Data JPA removes boilerplate DAO code by generating queries from method names.

## In this project

- entity: `QuantityMeasurementEntity`
- repository: `QuantityMeasurementRepository extends JpaRepository`
- derived query examples:
  - `findByOperation(...)`
  - `findByThisMeasurementType(...)`
  - `findByErrorTrue()`
- custom query example:
  - `@Query("SELECT q FROM QuantityMeasurementEntity q WHERE q.operation = ?1 AND q.error = false")`

---

## 7) Entity mapping theory

`QuantityMeasurementEntity` uses:

- `@Entity` for ORM mapping
- `@Table` to define table and indexes
- `@Id` + `@GeneratedValue` for PK
- `@Column` for column control
- `@PrePersist` and `@PreUpdate` for automatic timestamps

Why indexes?

- faster queries for frequent filters (operation, measurement type, created time)

---

## 8) Spring Security basics in this project

## Theory

Spring Security installs a filter chain in front of controllers.
Even with permit-all, requests still pass through the security infrastructure.

## In this project

`SecurityConfig`:

- disables CSRF (dev/testing + H2 console convenience)
- allows `/api/**`, `/swagger-ui/**`, `/v3/api-docs/**`, `/actuator/**`, `/h2-console/**`
- keeps foundation ready for future auth integration

---

## 9) OpenAPI / Swagger

## Theory

OpenAPI describes API contracts in machine-readable format.
Swagger UI visualizes and lets you test endpoints interactively.

## In this project

- springdoc dependency generates docs automatically
- controller methods are annotated with `@Operation`
- app metadata via `@OpenAPIDefinition`

URLs:

- `/v3/api-docs`
- `/swagger-ui.html`

---

## 10) Actuator

## Theory

Actuator exposes operational endpoints for health and metrics.
Useful for monitoring and deployment pipelines.

## In this project

Exposed endpoints: health, metrics, info

---

## 11) Profiles and configuration

## Theory

Spring supports environment-specific property files.
`application.properties` = default
`application-prod.properties` = production override

## In this project

- default: H2 in-memory DB
- prod: MySQL connection properties

---

## 12) Testing theory used here

## Controller test (`@WebMvcTest`)

- loads only web layer
- service is mocked
- verifies status codes and JSON shape

## Integration test (`@SpringBootTest` + random port)

- loads full application context
- uses `TestRestTemplate`
- validates behavior end-to-end

## Why both?

- fast unit-like web tests + realistic end-to-end confidence

---

## 13) Design principles reflected

- **Separation of concerns**: controller/service/repository/model split
- **Single responsibility**: each class has focused purpose
- **Fail fast**: validation catches bad input early
- **Auditability**: both success and error operations are persisted
- **Extensibility**: easy to add new units/operations/endpoints
