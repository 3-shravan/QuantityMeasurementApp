package com.shravan;

public enum LengthUnit {
  FEET(1.0),
  INCHES(1.0 / 12.0),
  YARDS(3.0),
  CENTIMETERS(1.0 / 30.48);

  private final double conversionFactor;

  LengthUnit(double conversionFactor) {
    this.conversionFactor = conversionFactor;
  }

  public double getConversionFactor() {
    return conversionFactor;
  }

  public double convertToBaseUnit(double value) {
    validateValue(value);
    return value * conversionFactor;
  }

  public double convertFromBaseUnit(double baseValue) {
    validateValue(baseValue);
    return baseValue / conversionFactor;
  }

  private static void validateValue(double value) {
    if (!Double.isFinite(value)) {
      throw new IllegalArgumentException("Length value must be numeric");
    }
  }
}