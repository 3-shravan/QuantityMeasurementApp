# Testing Guide

## 1) Test types in this project

### A) Web layer tests

File: `src/test/java/com/app/quantitymeasurement/controller/QuantityMeasurementControllerTest.java`

Uses:

- `@WebMvcTest`
- `MockMvc`
- mocked service dependency

Purpose:

- verify request mapping and validation behavior
- verify response status and JSON contract
- isolate controller from business and persistence layers

### B) Integration tests

File: `src/test/java/com/app/quantitymeasurement/integrationTests/QuantityMeasurementIntegrationTest.java`

Uses:

- `@SpringBootTest(webEnvironment = RANDOM_PORT)`
- `TestRestTemplate`

Purpose:

- run full app context
- test controller + service + repository + DB integration
- validate actual end-to-end behavior

### C) Context boot test

File: `src/test/java/com/app/quantitymeasurement/QuantityMeasurementApplicationTests.java`

Purpose:

- sanity check for Spring context startup

---

## 2) Run tests

```bash
mvn test
```

---

## 3) Generate rich test report

The project includes Maven Surefire Report plugin.

```bash
mvn surefire-report:report
```

Generated report (typical path):

- `target/site/surefire-report.html`

---

## 4) Why current pom has excludes/includes

This repository still has legacy UC16 files. The build config intentionally:

- excludes old classes from compilation
- includes specific UC17 test classes for surefire run

This keeps migration safe while preserving old artifacts for reference.

---

## 5) Suggested next test improvements

1. Add repository integration tests for custom query methods
2. Add actuator endpoint tests (`/actuator/health`)
3. Add Swagger/OpenAPI availability test (`/swagger-ui.html`, `/v3/api-docs`)
4. Add divide-by-zero assertion test for error shape
5. Add profile-specific tests (`dev` vs `prod`)
6. Add security tests once authentication is introduced

---

## 6) Test strategy summary for explanation

- `@WebMvcTest` for fast controller correctness
- `@SpringBootTest` for real integration confidence
- both together provide speed + realism
