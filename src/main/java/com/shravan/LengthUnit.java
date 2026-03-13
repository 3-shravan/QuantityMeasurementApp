package com.shravan;

public enum LengthUnit implements IMeasurable {
  FEET(1.0),
  INCHES(1.0 / 12.0),
  YARDS(3.0),
  CENTIMETERS(1.0 / 30.48);

  private final double conversionFactor;
  private final SupportsArithmetic supportsArithmetic;

  LengthUnit(double conversionFactor) {
    this.conversionFactor = conversionFactor;
    this.supportsArithmetic = () -> true;
  }

  public double getConversionFactor() {
    return conversionFactor;
  }

  @Override
  public String getUnitName() {
    return name();
  }

  @Override
  public SupportsArithmetic getSupportsArithmetic() {
    return supportsArithmetic;
  }
}