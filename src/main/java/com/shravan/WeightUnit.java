package com.shravan;

public enum WeightUnit {
  KILOGRAM(1.0),
  GRAM(0.001),
  POUND(0.453592);

  private final double conversionFactor;

  WeightUnit(double conversionFactor) {
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
      throw new IllegalArgumentException("Weight value must be numeric");
    }
  }
}
