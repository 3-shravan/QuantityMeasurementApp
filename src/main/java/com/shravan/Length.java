package com.shravan;

import java.util.Locale;
import java.util.Objects;

public final class Length {

  private final double value;
  private final LengthUnit unit;

  public Length(double value, LengthUnit unit) {
    if (!Double.isFinite(value)) {
      throw new IllegalArgumentException("Length value must be a finite number");
    }
    this.unit = Objects.requireNonNull(unit, "Length unit cannot be null");
    this.value = value;
  }

  public double getValue() {
    return value;
  }

  public LengthUnit getUnit() {
    return unit;
  }

  private double toBaseUnitFeet() {
    return value * unit.getConversionFactorToFeet();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Length other = (Length) obj;
    return Double.compare(this.toBaseUnitFeet(), other.toBaseUnitFeet()) == 0;
  }

  @Override
  public int hashCode() {
    return Double.hashCode(toBaseUnitFeet());
  }

  public enum LengthUnit {
    FEET(1.0),
    INCHES(1.0 / 12.0);

    private final double conversionFactorToFeet;

    LengthUnit(double conversionFactorToFeet) {
      this.conversionFactorToFeet = conversionFactorToFeet;
    }

    public double getConversionFactorToFeet() {
      return conversionFactorToFeet;
    }

    public static LengthUnit from(String unitText) {
      if (unitText == null) {
        throw new IllegalArgumentException("Unit text cannot be null");
      }

      String normalized = unitText.trim().toLowerCase(Locale.ROOT);
      switch (normalized) {
        case "foot":
        case "feet":
          return FEET;
        case "inch":
        case "inches":
          return INCHES;
        default:
          throw new IllegalArgumentException("Unsupported unit: " + unitText);
      }
    }
  }
}