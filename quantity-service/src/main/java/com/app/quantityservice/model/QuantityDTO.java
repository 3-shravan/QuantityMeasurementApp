package com.app.quantityservice.model;

import com.app.quantityservice.unit.LengthUnit;
import com.app.quantityservice.unit.TemperatureUnit;
import com.app.quantityservice.unit.VolumeUnit;
import com.app.quantityservice.unit.WeightUnit;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuantityDTO {

  @NotNull(message = "Value is required")
  private Double value;

  @NotBlank(message = "Unit is required")
  private String unit;

  @NotBlank(message = "Measurement type is required")
  @Pattern(regexp = "^(LengthUnit|VolumeUnit|WeightUnit|TemperatureUnit)$", message = "Measurement type must be LengthUnit, VolumeUnit, WeightUnit, or TemperatureUnit")
  private String measurementType;

  private OperationType operationType;

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

