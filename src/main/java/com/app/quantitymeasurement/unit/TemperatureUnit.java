package com.app.quantitymeasurement.unit;

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
    switch (this) {
      case CELSIUS:
        return value;
      case FAHRENHEIT:
        return (value - 32.0) * 5.0 / 9.0;
      case KELVIN:
        return value - 273.15;
      default:
        throw new IllegalStateException("Unexpected value: " + this);
    }
  }

  @Override
  public double convertFromBaseUnit(double baseValue) {
    IMeasurable.validate(baseValue);
    switch (this) {
      case CELSIUS:
        return baseValue;
      case FAHRENHEIT:
        return baseValue * 9.0 / 5.0 + 32.0;
      case KELVIN:
        return baseValue + 273.15;
      default:
        throw new IllegalStateException("Unexpected value: " + this);
    }
  }
}
