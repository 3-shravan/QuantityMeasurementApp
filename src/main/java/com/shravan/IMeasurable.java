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

  default SupportsArithmetic getSupportsArithmetic() {
    return () -> true;
  }

  default boolean supportsArithmetic() {
    return getSupportsArithmetic().isSupported();
  }

  default boolean supportsAddition() {
    return supportsArithmetic();
  }

  default boolean supportsSubtraction() {
    return supportsArithmetic();
  }

  default boolean supportsDivision() {
    return supportsArithmetic();
  }

  default void validateOperationSupport(String operation) {
    if (!supportsArithmetic()) {
      throw new UnsupportedOperationException(
          "Operation " + operation + " is not supported for " + getClass().getSimpleName());
    }
  }

  static void validateValue(double value) {
    if (!Double.isFinite(value)) {
      throw new IllegalArgumentException("Measurement value must be numeric");
    }
  }
}