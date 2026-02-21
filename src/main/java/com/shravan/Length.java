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

  /**
   * Creates a new immutable length value.
   *
   * @param value numeric length value (must be finite)
   * @param unit  unit of the value (must not be null)
   */
  public Length(double value, LengthUnit unit) {
    if (!Double.isFinite(value)) {
      throw new IllegalArgumentException("Length value must be numeric");
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

  private double convertToBaseUnit() {
    return value * unit.getConversionFactor();
  }

  private static void validateConversionInput(double inputValue, LengthUnit sourceUnit, LengthUnit targetUnit) {
    if (!Double.isFinite(inputValue)) {
      throw new IllegalArgumentException("Length value must be numeric");
    }
    Objects.requireNonNull(sourceUnit, "Source unit cannot be null");
    Objects.requireNonNull(targetUnit, "Target unit cannot be null");
  }

  /**
   * Converts this instance to the requested target unit.
   *
   * @param targetUnit target unit (must not be null)
   * @return a new {@code Length} in the target unit
   */
  public Length convertTo(LengthUnit targetUnit) {
    double convertedValue = convert(this.value, this.unit, targetUnit);
    return new Length(convertedValue, targetUnit);
  }

  /**
   * Converts a numeric value from one unit to another.
   *
   * @param inputValue value to convert
   * @param sourceUnit source unit
   * @param targetUnit target unit
   * @return converted numeric value in target unit
   */
  public static double convert(double inputValue, LengthUnit sourceUnit, LengthUnit targetUnit) {
    validateConversionInput(inputValue, sourceUnit, targetUnit);
    double baseInches = inputValue * sourceUnit.getConversionFactor();
    return baseInches / targetUnit.getConversionFactor();
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

  @Override
  public String toString() {
    return String.format("%.2f %s", value, unit);
  }
}