package com.shravan;

public interface IMeasurable {

  double getConversionFactor();

  default double convertToBaseUnit(double value) {
    validateValue(value);
    return value * getConversionFactor();
  }

  default double convertFromBaseUnit(double baseValue) {
    validateValue(baseValue);
    return baseValue / getConversionFactor();
  }

  default String getUnitName() {
    return toString();
  }

  static void validateValue(double value) {
    if (!Double.isFinite(value)) {
      throw new IllegalArgumentException("Measurement value must be numeric");
    }
  }
}