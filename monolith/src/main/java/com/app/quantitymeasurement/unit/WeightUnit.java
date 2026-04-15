package com.app.quantitymeasurement.unit;

public enum WeightUnit implements IMeasurable {
  KILOGRAM(1.0),
  GRAM(1.0 / 1000.0),
  POUND(0.453592);

  private final double conversionFactor;

  WeightUnit(double conversionFactor) {
    this.conversionFactor = conversionFactor;
  }

  @Override
  public double getConversionFactor() {
    return conversionFactor;
  }
}
