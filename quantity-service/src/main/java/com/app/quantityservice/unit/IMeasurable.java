package com.app.quantityservice.unit;

public interface IMeasurable {

  double getConversionFactor();

  default double convertToBaseUnit(double value) {
    validate(value);
    return value * getConversionFactor();
  }

  default double convertFromBaseUnit(double baseValue) {
    validate(baseValue);
    return baseValue / getConversionFactor();
  }

  default String getMeasurementType() {
    return getClass().getSimpleName();
  }

  default boolean supportsArithmetic() {
    return true;
  }

  default void validateOperationSupport(String operation) {
    if (!supportsArithmetic()) {
      throw new UnsupportedOperationException(this + " does not support " + operation + " operations");
    }
  }

  static void validate(double value) {
    if (!Double.isFinite(value)) {
      throw new IllegalArgumentException("Measurement value must be numeric");
    }
  }
}

