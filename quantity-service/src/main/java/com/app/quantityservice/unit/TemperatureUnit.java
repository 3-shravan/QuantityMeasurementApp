package com.app.quantityservice.unit;

public enum TemperatureUnit implements IMeasurable {
  CELSIUS,
  FAHRENHEIT,
  KELVIN;

  @Override
  public double getConversionFactor() {
    return 1.0;
  }

  @Override
  public boolean supportsArithmetic() {
    return false;
  }

  @Override
  public double convertToBaseUnit(double value) {
    IMeasurable.validate(value);
    return switch (this) {
      case CELSIUS -> value;
      case FAHRENHEIT -> (value - 32.0) * 5.0 / 9.0;
      case KELVIN -> value - 273.15;
    };
  }

  @Override
  public double convertFromBaseUnit(double baseValue) {
    IMeasurable.validate(baseValue);
    return switch (this) {
      case CELSIUS -> baseValue;
      case FAHRENHEIT -> baseValue * 9.0 / 5.0 + 32.0;
      case KELVIN -> baseValue + 273.15;
    };
  }
}

