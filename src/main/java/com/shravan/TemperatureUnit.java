package com.shravan;

import java.util.function.DoubleUnaryOperator;

public enum TemperatureUnit implements IMeasurable {
  CELSIUS(celsius -> celsius, celsiusBase -> celsiusBase),
  FAHRENHEIT(celsius -> (celsius * 9.0 / 5.0) + 32.0, fahrenheit -> (fahrenheit - 32.0) * 5.0 / 9.0),
  KELVIN(celsius -> celsius + 273.15, kelvin -> kelvin - 273.15);

  private static final SupportsArithmetic TEMPERATURE_ARITHMETIC = () -> false;

  private final DoubleUnaryOperator fromCelsius;
  private final DoubleUnaryOperator toCelsius;

  TemperatureUnit(DoubleUnaryOperator fromCelsius, DoubleUnaryOperator toCelsius) {
    this.fromCelsius = fromCelsius;
    this.toCelsius = toCelsius;
  }

  @Override
  public double getConversionFactor() {
    return 1.0;
  }

  @Override
  public double convertToBaseUnit(double value) {
    IMeasurable.validateValue(value);
    return toCelsius.applyAsDouble(value);
  }

  @Override
  public double convertFromBaseUnit(double baseValue) {
    IMeasurable.validateValue(baseValue);
    return fromCelsius.applyAsDouble(baseValue);
  }

  @Override
  public String getUnitName() {
    return name();
  }

  @Override
  public SupportsArithmetic getSupportsArithmetic() {
    return TEMPERATURE_ARITHMETIC;
  }

  @Override
  public boolean supportsAddition() {
    return false;
  }

  @Override
  public boolean supportsSubtraction() {
    return false;
  }

  @Override
  public boolean supportsDivision() {
    return false;
  }

  @Override
  public void validateOperationSupport(String operation) {
    if ("ADD".equalsIgnoreCase(operation) || "ADDITION".equalsIgnoreCase(operation)) {
      throw new UnsupportedOperationException("Temperature does not support addition of absolute values");
    }
    if ("SUBTRACT".equalsIgnoreCase(operation) || "SUBTRACTION".equalsIgnoreCase(operation)) {
      throw new UnsupportedOperationException("Temperature does not support subtraction of absolute values");
    }
    if ("DIVIDE".equalsIgnoreCase(operation) || "DIVISION".equalsIgnoreCase(operation)) {
      throw new UnsupportedOperationException("Temperature does not support division of absolute values");
    }
    throw new UnsupportedOperationException("Temperature does not support operation: " + operation);
  }
}
