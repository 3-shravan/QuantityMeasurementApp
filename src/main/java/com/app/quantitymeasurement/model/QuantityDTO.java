package com.app.quantitymeasurement.model;

import com.app.quantitymeasurement.unit.LengthUnit;
import com.app.quantitymeasurement.unit.TemperatureUnit;
import com.app.quantitymeasurement.unit.VolumeUnit;
import com.app.quantitymeasurement.unit.WeightUnit;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class QuantityDTO {

  @NotNull(message = "Value is required")
  private Double value;

  @NotBlank(message = "Unit is required")
  private String unit;

  @NotBlank(message = "Measurement type is required")
  @Pattern(regexp = "^(LengthUnit|VolumeUnit|WeightUnit|TemperatureUnit)$", message = "Measurement type must be LengthUnit, VolumeUnit, WeightUnit, or TemperatureUnit")
  private String measurementType;

  private OperationType operationType;

  public QuantityDTO() {
  }

  public QuantityDTO(Double value, String unit, String measurementType, OperationType operationType) {
    this.value = value;
    this.unit = unit;
    this.measurementType = measurementType;
    this.operationType = operationType;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public String getMeasurementType() {
    return measurementType;
  }

  public void setMeasurementType(String measurementType) {
    this.measurementType = measurementType;
  }

  public OperationType getOperationType() {
    return operationType;
  }

  public void setOperationType(OperationType operationType) {
    this.operationType = operationType;
  }

  @AssertTrue(message = "Unit must be valid for the specified measurement type")
  public boolean isUnitValidForMeasurementType() {
    if (unit == null || measurementType == null) {
      return true;
    }

    String unitName = unit.trim().toUpperCase();
    return switch (measurementType) {
      case "LengthUnit" -> isEnumConstant(LengthUnit.class, unitName);
      case "VolumeUnit" -> isEnumConstant(VolumeUnit.class, unitName);
      case "WeightUnit" -> isEnumConstant(WeightUnit.class, unitName);
      case "TemperatureUnit" -> isEnumConstant(TemperatureUnit.class, unitName);
      default -> false;
    };
  }

  private static <E extends Enum<E>> boolean isEnumConstant(Class<E> enumClass, String value) {
    for (E constant : enumClass.getEnumConstants()) {
      if (constant.name().equalsIgnoreCase(value)) {
        return true;
      }
    }
    return false;
  }
}
