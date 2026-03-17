package com.app.quantitymeasurement.entity;

import java.io.Serializable;

public class QuantityMeasurementEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  private long id;
  private double firstValue;
  private String firstUnit;
  private Double secondValue;
  private String secondUnit;
  private String operation;
  private String measurementType;
  private String result;
  private boolean error;
  private String errorMessage;

  public QuantityMeasurementEntity() {
  }

  public QuantityMeasurementEntity(double firstValue, String firstUnit,
      Double secondValue, String secondUnit,
      String operation, String measurementType, String result) {
    this.firstValue = firstValue;
    this.firstUnit = firstUnit;
    this.secondValue = secondValue;
    this.secondUnit = secondUnit;
    this.operation = operation;
    this.measurementType = measurementType;
    this.result = result;
    this.error = false;
  }

  public QuantityMeasurementEntity(double firstValue, String firstUnit,
      Double secondValue, String secondUnit,
      String operation, String measurementType,
      boolean error, String errorMessage) {
    this.firstValue = firstValue;
    this.firstUnit = firstUnit;
    this.secondValue = secondValue;
    this.secondUnit = secondUnit;
    this.operation = operation;
    this.measurementType = measurementType;
    this.result = "ERROR";
    this.error = error;
    this.errorMessage = errorMessage;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public double getFirstValue() {
    return firstValue;
  }

  public String getFirstUnit() {
    return firstUnit;
  }

  public Double getSecondValue() {
    return secondValue;
  }

  public String getSecondUnit() {
    return secondUnit;
  }

  public String getOperation() {
    return operation;
  }

  public String getMeasurementType() {
    return measurementType;
  }

  public String getResult() {
    return result;
  }

  public boolean hasError() {
    return error;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  @Override
  public String toString() {
    if (error) {
      return "QuantityMeasurementEntity{operation=" + operation + ", error='" + errorMessage + "'}";
    }
    return "QuantityMeasurementEntity{operation=" + operation + ", result='" + result + "'}";
  }
}
