# API Reference

Base URL: `http://localhost:8080/api/v1/quantities`

Content type: `application/json`

---

## Request body model

Most POST APIs use:

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

Allowed measurement types:

- `LengthUnit`
- `VolumeUnit`
- `WeightUnit`
- `TemperatureUnit`

---

## 1) Compare quantities

**POST** `/compare`

Sample response:

```json
{
  "operation": "compare",
  "resultString": "true",
  "error": false
}
```

---

## 2) Convert quantity

**POST** `/convert`

Use `thisQuantityDTO` as source and `thatQuantityDTO.unit` as target unit.

Sample response:

```json
{
  "operation": "convert",
  "resultValue": 12.0,
  "error": false
}
```

---

## 3) Add quantities

**POST** `/add`

Sample response:

```json
{
  "operation": "add",
  "resultValue": 2.0,
  "resultUnit": "FEET",
  "resultMeasurementType": "LengthUnit",
  "error": false
}
```

---

## 4) Subtract quantities

**POST** `/subtract`

---

## 5) Divide quantities

**POST** `/divide`

Possible runtime error for divide-by-zero path.

---

## 6) History by operation

**GET** `/history/operation/{operation}`

Example:
`/history/operation/ADD`

`operation` enum values:

- `ADD`
- `SUBTRACT`
- `MULTIPLY`
- `DIVIDE`
- `COMPARE`
- `CONVERT`

---

## 7) History by measurement type

**GET** `/history/type/{measurementType}`

Example:
`/history/type/LengthUnit`

---

## 8) Error history

**GET** `/history/errored`

Returns only operations where `error=true`.

---

## 9) Operation count

**GET** `/count/{operation}`

Returns successful operation count (where `error=false`).

---

## Error response format

```json
{
  "timestamp": "2026-03-18T16:00:00.000",
  "status": 400,
  "error": "Quantity Measurement Error",
  "message": "Unit must be valid for the specified measurement type",
  "path": "/api/v1/quantities/compare"
}
```

---

## Quick curl examples

```bash
curl -X POST http://localhost:8080/api/v1/quantities/compare \
  -H "Content-Type: application/json" \
  -d '{
    "thisQuantityDTO": {"value": 1.0, "unit": "FEET", "measurementType": "LengthUnit"},
    "thatQuantityDTO": {"value": 12.0, "unit": "INCHES", "measurementType": "LengthUnit"}
  }'
```

```bash
curl http://localhost:8080/api/v1/quantities/history/operation/COMPARE
```
