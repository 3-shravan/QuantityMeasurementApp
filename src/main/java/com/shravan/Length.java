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

  public Length convertTo(LengthUnit targetUnit) {
    double convertedValue = convert(this.value, this.unit, targetUnit);
    return new Length(convertedValue, targetUnit);
  }

  public Length add(Length thatLength) {
    Objects.requireNonNull(thatLength, "Operand length cannot be null");
    if (!Double.isFinite(thatLength.value)) {
      throw new IllegalArgumentException("Length value must be numeric");
    }

    // Sum in base unit (inches)
    double sumInBase = this.convertToBaseUnit() + thatLength.convertToBaseUnit();

    // Convert sum back to this instance's unit
    double sumInThisUnit = sumInBase / this.unit.getConversionFactor();

    return new Length(sumInThisUnit, this.unit);
  }

  /**
   * Convenience static add method: adds two raw values with units and returns result in targetUnit.
   */
  public static Length add(double value1, LengthUnit unit1, double value2, LengthUnit unit2, LengthUnit targetUnit) {
    validateConversionInput(value1, unit1, targetUnit);
    validateConversionInput(value2, unit2, targetUnit);

    double base1 = value1 * unit1.getConversionFactor();
    double base2 = value2 * unit2.getConversionFactor();
    double sumBase = base1 + base2;

    double sumInTarget = sumBase / targetUnit.getConversionFactor();
    return new Length(sumInTarget, targetUnit);
  }

  private static void validateConversionInput(double inputValue, LengthUnit sourceUnit, LengthUnit targetUnit) {
    if (!Double.isFinite(inputValue)) {
      throw new IllegalArgumentException("Length value must be numeric");
    }
    Objects.requireNonNull(sourceUnit, "Source unit cannot be null");
    Objects.requireNonNull(targetUnit, "Target unit cannot be null");
  }

  public static double convert(double inputValue, LengthUnit sourceUnit, LengthUnit targetUnit) {
    validateConversionInput(inputValue, sourceUnit, targetUnit);
    double baseInches = inputValue * sourceUnit.getConversionFactor();
    return baseInches / targetUnit.getConversionFactor();
  }

  public double getValue() {
    return value;
  }

  public LengthUnit getUnit() {
    return unit;
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

  @Override
  public String toString() {
    return String.format("%.2f %s", value, unit);
  }
}