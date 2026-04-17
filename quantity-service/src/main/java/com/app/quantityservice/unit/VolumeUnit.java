package com.app.quantityservice.unit;

public enum VolumeUnit implements IMeasurable {
  LITRE(1.0),
  MILLILITRE(1.0 / 1000.0),
  GALLON(3.78541);

  private final double conversionFactor;

  VolumeUnit(double conversionFactor) {
    this.conversionFactor = conversionFactor;
  }

  @Override
  public double getConversionFactor() {
    return conversionFactor;
  }
}

