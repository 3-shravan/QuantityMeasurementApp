package com.shravan;

import java.util.Objects;

public final class Length {

  private static final double EPSILON = 1e-4;

  private final double value;
  private final LengthUnit unit;

  public enum LengthUnit {
    FEET(12.0),
    INCHES(1.0),
    YARDS(36.0),
    CENTIMETERS(0.393701);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
      this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
      return conversionFactor;
    }
  }

  public Length(double value, LengthUnit unit) {
    if (!Double.isFinite(value)) {
      throw new IllegalArgumentException("Length value must be numeric");
    }
    this.unit = Objects.requireNonNull(unit, "Length unit cannot be null");
    this.value = value;
  }

  private double convertToBaseUnit() {
    return value * unit.getConversionFactor();
  }

  public boolean compare(Length thatLength) {
    return Math.abs(this.convertToBaseUnit() - thatLength.convertToBaseUnit()) < EPSILON;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return compare((Length) o);
  }

  @Override
  public int hashCode() {
    return Double.valueOf(Math.round(convertToBaseUnit() / EPSILON) * EPSILON).hashCode();
  }
}