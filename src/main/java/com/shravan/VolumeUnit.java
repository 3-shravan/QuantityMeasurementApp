package com.shravan;

public enum VolumeUnit implements IMeasurable {
  LITRE(1.0),
  MILLILITRE(0.001),
  GALLON(3.78541);

  private final double conversionFactor;
  private final SupportsArithmetic supportsArithmetic;

  VolumeUnit(double conversionFactor) {
    this.conversionFactor = conversionFactor;
    this.supportsArithmetic = () -> true;
  }

  @Override
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
