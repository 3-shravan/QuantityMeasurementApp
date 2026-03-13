package com.shravan;

public enum WeightUnit implements IMeasurable {
  KILOGRAM(1.0),
  GRAM(0.001),
  POUND(0.453592);

  private final double conversionFactor;
  private final SupportsArithmetic supportsArithmetic;

  WeightUnit(double conversionFactor) {
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
