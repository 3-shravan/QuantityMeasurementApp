package com.app.quantityservice.controller;

import com.app.quantityservice.model.OperationType;
import com.app.quantityservice.model.QuantityInputDTO;
import com.app.quantityservice.model.QuantityMeasurementDTO;
import com.app.quantityservice.service.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/quantities")
@Tag(name = "Quantity Measurements", description = "REST API for quantity measurement operations")
@RequiredArgsConstructor
public class QuantityMeasurementController {

  private final IQuantityMeasurementService service;

  @PostMapping("/compare")
  @Operation(summary = "Compare two quantities")
  public ResponseEntity<QuantityMeasurementDTO> compareQuantities(@Valid @RequestBody QuantityInputDTO inputDTO) {
    return ResponseEntity.ok(service.compare(inputDTO.getThisQuantityDTO(), inputDTO.getThatQuantityDTO()));
  }

  @PostMapping("/convert")
  @Operation(summary = "Convert quantity to target unit")
  public ResponseEntity<QuantityMeasurementDTO> convertQuantity(@Valid @RequestBody QuantityInputDTO inputDTO) {
    return ResponseEntity.ok(service.convert(inputDTO.getThisQuantityDTO(), inputDTO.getThatQuantityDTO()));
  }

  @PostMapping("/add")
  @Operation(summary = "Add two quantities")
  public ResponseEntity<QuantityMeasurementDTO> addQuantities(@Valid @RequestBody QuantityInputDTO inputDTO) {
    return ResponseEntity.ok(service.add(inputDTO.getThisQuantityDTO(), inputDTO.getThatQuantityDTO()));
  }

  @PostMapping("/subtract")
  @Operation(summary = "Subtract two quantities")
  public ResponseEntity<QuantityMeasurementDTO> subtractQuantities(@Valid @RequestBody QuantityInputDTO inputDTO) {
    return ResponseEntity.ok(service.subtract(inputDTO.getThisQuantityDTO(), inputDTO.getThatQuantityDTO()));
  }

  @PostMapping("/divide")
  @Operation(summary = "Divide two quantities")
  public ResponseEntity<QuantityMeasurementDTO> divideQuantities(@Valid @RequestBody QuantityInputDTO inputDTO) {
    return ResponseEntity.ok(service.divide(inputDTO.getThisQuantityDTO(), inputDTO.getThatQuantityDTO()));
  }

  @GetMapping("/history/operation/{operation}")
  @Operation(summary = "Get operation history by operation type")
  public ResponseEntity<List<QuantityMeasurementDTO>> getOperationHistory(
      @PathVariable("operation") OperationType operation) {
    return ResponseEntity.ok(service.getOperationHistory(operation.name().toLowerCase()));
  }

  @GetMapping("/history/type/{measurementType}")
  @Operation(summary = "Get operation history by measurement type")
  public ResponseEntity<List<QuantityMeasurementDTO>> getMeasurementTypeHistory(
      @PathVariable("measurementType") String measurementType) {
    return ResponseEntity.ok(service.getMeasurementsByType(measurementType));
  }

  @GetMapping("/history/errored")
  @Operation(summary = "Get operation history that has errors")
  public ResponseEntity<List<QuantityMeasurementDTO>> getErroredHistory() {
    return ResponseEntity.ok(service.getErrorHistory());
  }

  @GetMapping("/count/{operation}")
  @Operation(summary = "Get operation count by operation type")
  public ResponseEntity<Long> getOperationCount(@PathVariable("operation") OperationType operation) {
    return ResponseEntity.ok(service.getOperationCount(operation.name()));
  }
}

