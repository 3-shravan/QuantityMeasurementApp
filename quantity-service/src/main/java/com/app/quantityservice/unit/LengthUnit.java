package com.app.quantityservice.unit;

public enum LengthUnit implements IMeasurable {
  FEET(1.0),
  INCHES(1.0 / 12.0),
  YARDS(3.0),
  CENTIMETERS(1.0 / 30.48);

  private final double conversionFactor;

  LengthUnit(double conversionFactor) {
    this.conversionFactor = conversionFactor;
  }

  @Override
  public double getConversionFactor() {
    return conversionFactor;
  }
}

