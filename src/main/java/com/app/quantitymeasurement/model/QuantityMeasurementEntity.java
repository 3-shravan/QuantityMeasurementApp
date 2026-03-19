package com.app.quantitymeasurement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "quantity_measurement_entity", indexes = {
    @Index(name = "idx_qme_operation", columnList = "operation"),
    @Index(name = "idx_qme_this_measurement_type", columnList = "this_measurement_type"),
    @Index(name = "idx_qme_created_at", columnList = "created_at")
})
public class QuantityMeasurementEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "this_value", nullable = false)
  private Double thisValue;

  @Column(name = "this_unit", nullable = false, length = 64)
  private String thisUnit;

  @Column(name = "this_measurement_type", nullable = false, length = 64)
  private String thisMeasurementType;

  @Column(name = "that_value")
  private Double thatValue;

  @Column(name = "that_unit", length = 64)
  private String thatUnit;

  @Column(name = "that_measurement_type", length = 64)
  private String thatMeasurementType;

  @Column(name = "operation", nullable = false, length = 32)
  private String operation;

  @Column(name = "result_string")
  private String resultString;

  @Column(name = "result_value")
  private Double resultValue;

  @Column(name = "result_unit", length = 64)
  private String resultUnit;

  @Column(name = "result_measurement_type", length = 64)
  private String resultMeasurementType;

  @Column(name = "error_message", length = 500)
  private String errorMessage;

  @Column(name = "is_error", nullable = false)
  private boolean error;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public QuantityMeasurementEntity() {
  }

  public QuantityMeasurementEntity(Long id, Double thisValue, String thisUnit, String thisMeasurementType,
      Double thatValue, String thatUnit, String thatMeasurementType, String operation, String resultString,
      Double resultValue, String resultUnit, String resultMeasurementType, String errorMessage, boolean error,
      LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.thisValue = thisValue;
    this.thisUnit = thisUnit;
    this.thisMeasurementType = thisMeasurementType;
    this.thatValue = thatValue;
    this.thatUnit = thatUnit;
    this.thatMeasurementType = thatMeasurementType;
    this.operation = operation;
    this.resultString = resultString;
    this.resultValue = resultValue;
    this.resultUnit = resultUnit;
    this.resultMeasurementType = resultMeasurementType;
    this.errorMessage = errorMessage;
    this.error = error;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getThisValue() {
    return thisValue;
  }

  public void setThisValue(Double thisValue) {
    this.thisValue = thisValue;
  }

  public String getThisUnit() {
    return thisUnit;
  }

  public void setThisUnit(String thisUnit) {
    this.thisUnit = thisUnit;
  }

  public String getThisMeasurementType() {
    return thisMeasurementType;
  }

  public void setThisMeasurementType(String thisMeasurementType) {
    this.thisMeasurementType = thisMeasurementType;
  }

  public Double getThatValue() {
    return thatValue;
  }

  public void setThatValue(Double thatValue) {
    this.thatValue = thatValue;
  }

  public String getThatUnit() {
    return thatUnit;
  }

  public void setThatUnit(String thatUnit) {
    this.thatUnit = thatUnit;
  }

  public String getThatMeasurementType() {
    return thatMeasurementType;
  }

  public void setThatMeasurementType(String thatMeasurementType) {
    this.thatMeasurementType = thatMeasurementType;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public String getResultString() {
    return resultString;
  }

  public void setResultString(String resultString) {
    this.resultString = resultString;
  }

  public Double getResultValue() {
    return resultValue;
  }

  public void setResultValue(Double resultValue) {
    this.resultValue = resultValue;
  }

  public String getResultUnit() {
    return resultUnit;
  }

  public void setResultUnit(String resultUnit) {
    this.resultUnit = resultUnit;
  }

  public String getResultMeasurementType() {
    return resultMeasurementType;
  }

  public void setResultMeasurementType(String resultMeasurementType) {
    this.resultMeasurementType = resultMeasurementType;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public boolean isError() {
    return error;
  }

  public void setError(boolean error) {
    this.error = error;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @PrePersist
  public void prePersist() {
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
