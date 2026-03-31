# Quantity Measurement App - Work Log (Date-wise)

This file is maintained as a personal progress log of the project, from day 1 to current state.

## Project Start

- **Start date:** 2026-02-18
- **Latest recorded work:** 2026-03-25
- **Primary branch:** `main`
- **Development style:** Use-case driven branch strategy (`feature/UCx-*`)

---

## Date-wise Development Log

### 2026-02-18

- Initialized repository and created base project structure.
- First commit completed (`a6dbbd9`).

### 2026-02-19

- Completed **UC1 (Feet Equality)** in branch `feature/UC1-FeetEquality`.
- Completed **UC2 (Inch Equality)** in branch `feature/UC2-InchEquality`.
- Established early equality behavior for length measurements.

### 2026-02-20

- Completed **UC3 (Generic Length / Generic Quantity Class)** in branch `feature/UC3-GenericLength`.
- Refactored toward reusable model using generic quantity concepts and DRY direction.

### 2026-02-21

- Completed **UC4 (Yard Equality)** in branch `feature/UC4-YardEquality`.
- Completed **UC5 (Unit Conversion)** in branch `feature/UC5-UnitConversion`.
- Extended coverage of length unit combinations and conversion rules.

### 2026-03-10

- Added **UC6 (Unit Addition)** implementation.
- Updated `.gitignore` in `dev` branch, then adjusted in `feature/UC6-UnitAddition`.

### 2026-03-11

- Completed **UC7 (Target Unit Addition)** in `feature/UC7-TargetUnitAddition`.
- Completed **UC8 (Standalone Unit Refactor)** in `feature/UC8-StandaloneUnit`.
- Completed **UC9 (Weight Equality)** in `feature/UC9-WeightEquality`.
- Completed **UC10 (Generic Quantity Extension)** in `feature/UC10-GenericQuantity`.
- Completed **UC11 (Volume Equality)** in `feature/UC11-VolumeEquality`.
- Completed **UC12 (Quantity Operations)** in `feature/UC12-QuantityOperations`.
- This day was a major feature expansion day adding multi-domain measurement support.

### 2026-03-13

- Completed **UC13 (Enforce DRY)** in `feature/UC13-EnforceDRY`.
- Completed **UC14 (Temperature Measurement)** in `feature/UC14-TemperatureMeasurement`.
- Improved architecture reuse and introduced temperature conversion handling.

### 2026-03-17

- Completed **UC15 (N-Tier Architecture)** in `feature/UC15-N-Tier-Architecture`.
- Added/refined tests for N-tier restructuring.
- Completed **UC16 (JDBC Integration)** in `feature/UC16-JDBC`.
- Updated persistence dependencies/configuration via `pom.xml` refactor.

### 2026-03-19

- Started **UC17 (Spring Backend)** implementation.
- Began Spring Boot API-oriented backend version of Quantity Measurement App.

### 2026-03-24

- Refactored backend structure for cleaner layered organization.
- Improved package layout and implementation flow.

### 2026-03-25

- Fixed entity field mismatch issue in UC17 backend.
- Added/standardized Surefire report support and testing setup.
- Brought final UC17 work to `main` (`02c4450`).

---

## What Is Implemented in Current Codebase

- Spring Boot backend with REST endpoints for:
  - compare, convert, add, subtract, divide
  - operation/type/error history retrieval
  - operation count endpoint
- Domain-level generic quantity handling with reusable unit contracts.
- Measurement categories implemented:
  - Length, Weight, Volume, Temperature
- Persistence layer with JPA repository and MySQL/H2 support.
- Exception handling with standardized error response body.
- OpenAPI/Swagger enabled documentation.
- Security configuration allowing API and docs access.
- Test setup includes:
  - MVC controller tests
  - Spring Boot integration tests
  - Surefire reporting setup

---
