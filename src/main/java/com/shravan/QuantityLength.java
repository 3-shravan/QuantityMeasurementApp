package com.shravan;

import java.util.Objects;

public final class QuantityLength {

  private static final double EPSILON = 1e-4;

  private final double value;
  private final LengthUnit unit;

  public QuantityLength(double value, LengthUnit unit) {
    validateValue(value);
    this.unit = Objects.requireNonNull(unit, "Length unit cannot be null");
    this.value = value;
  }

  public boolean compare(QuantityLength thatLength) {
    Objects.requireNonNull(thatLength, "Length to compare cannot be null");
    return Math.abs(this.toBaseUnit() - thatLength.toBaseUnit()) < EPSILON;
  }

  public QuantityLength convertTo(LengthUnit targetUnit) {
    return new QuantityLength(convert(this.value, this.unit, targetUnit), targetUnit);
  }

  public QuantityLength add(QuantityLength thatLength) {
    return addInternal(thatLength, this.unit);
  }

  public QuantityLength add(QuantityLength thatLength, LengthUnit targetUnit) {
    return addInternal(thatLength, targetUnit);
  }

  /**
   * Convenience static add method: adds two raw values with units and returns
   * result in targetUnit.
   */
  public static QuantityLength add(double value1, LengthUnit unit1, double value2,
      LengthUnit unit2, LengthUnit targetUnit) {
    validateConversionInput(value1, unit1, targetUnit);
    validateConversionInput(value2, unit2, targetUnit);

    double sumInBaseUnit = unit1.convertToBaseUnit(value1) + unit2.convertToBaseUnit(value2);
    return new QuantityLength(targetUnit.convertFromBaseUnit(sumInBaseUnit), targetUnit);
  }

  public static double convert(double inputValue, LengthUnit sourceUnit,
      LengthUnit targetUnit) {
    validateConversionInput(inputValue, sourceUnit, targetUnit);
    return targetUnit.convertFromBaseUnit(sourceUnit.convertToBaseUnit(inputValue));
  }

  public double getValue() {
    return value;
  }

  public LengthUnit getUnit() {
    return unit;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    return compare((QuantityLength) other);
  }

  @Override
  public int hashCode() {
    return Double.hashCode(Math.round(toBaseUnit() / EPSILON) * EPSILON);
  }

  @Override
  public String toString() {
    return String.format("%.2f %s", value, unit);
  }

  private QuantityLength addInternal(QuantityLength thatLength, LengthUnit targetUnit) {
    Objects.requireNonNull(thatLength, "Operand length cannot be null");
    Objects.requireNonNull(targetUnit, "Target unit cannot be null");

    double sumInBaseUnit = this.toBaseUnit() + thatLength.toBaseUnit();
    return new QuantityLength(targetUnit.convertFromBaseUnit(sumInBaseUnit), targetUnit);
  }

  private double toBaseUnit() {
    return unit.convertToBaseUnit(value);
  }

  private static void validateConversionInput(double inputValue, LengthUnit sourceUnit,
      LengthUnit targetUnit) {
    validateValue(inputValue);
    Objects.requireNonNull(sourceUnit, "Source unit cannot be null");
    Objects.requireNonNull(targetUnit, "Target unit cannot be null");
  }

  private static void validateValue(double value) {
    if (!Double.isFinite(value)) {
      throw new IllegalArgumentException("Length value must be numeric");
    }
  }
}