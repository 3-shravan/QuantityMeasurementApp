package com.shravan;

import java.util.Objects;

public final class QuantityWeight {
  private static final double EPSILON = 1e-6;

  private final double value;
  private final WeightUnit unit;

  public QuantityWeight(double value, WeightUnit unit) {
    validateValue(value);
    this.unit = Objects.requireNonNull(unit, "Weight unit cannot be null");
    this.value = value;
  }

  public boolean compare(QuantityWeight otherWeight) {
    Objects.requireNonNull(otherWeight, "Weight to compare cannot be null");
    return Math.abs(this.toBaseUnit() - otherWeight.toBaseUnit()) < EPSILON;
  }

  public QuantityWeight convertTo(WeightUnit targetUnit) {
    return new QuantityWeight(convert(this.value, this.unit, targetUnit), targetUnit);
  }

  public QuantityWeight add(QuantityWeight otherWeight) {
    return addInternal(otherWeight, this.unit);
  }

  public QuantityWeight add(QuantityWeight otherWeight, WeightUnit targetUnit) {
    return addInternal(otherWeight, targetUnit);
  }

  public static QuantityWeight add(double value1, WeightUnit unit1, double value2,
      WeightUnit unit2, WeightUnit targetUnit) {
    validateConversionInput(value1, unit1, targetUnit);
    validateConversionInput(value2, unit2, targetUnit);

    double sumInBaseUnit = unit1.convertToBaseUnit(value1) + unit2.convertToBaseUnit(value2);
    return new QuantityWeight(targetUnit.convertFromBaseUnit(sumInBaseUnit), targetUnit);
  }

  public static double convert(double inputValue, WeightUnit sourceUnit, WeightUnit targetUnit) {
    validateConversionInput(inputValue, sourceUnit, targetUnit);
    return targetUnit.convertFromBaseUnit(sourceUnit.convertToBaseUnit(inputValue));
  }

  public double getValue() {
    return value;
  }

  public WeightUnit getUnit() {
    return unit;
  }

  public double toBaseUnit() {
    return unit.convertToBaseUnit(value);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    return compare((QuantityWeight) other);
  }

  @Override
  public int hashCode() {
    return Double.hashCode(Math.round(toBaseUnit() / EPSILON) * EPSILON);
  }

  @Override
  public String toString() {
    return String.format("%.2f %s", value, unit);
  }

  private QuantityWeight addInternal(QuantityWeight otherWeight, WeightUnit targetUnit) {
    Objects.requireNonNull(otherWeight, "Operand weight cannot be null");
    Objects.requireNonNull(targetUnit, "Target unit cannot be null");

    double sumInBaseUnit = this.toBaseUnit() + otherWeight.toBaseUnit();
    return new QuantityWeight(targetUnit.convertFromBaseUnit(sumInBaseUnit), targetUnit);
  }

  private static void validateConversionInput(double inputValue, WeightUnit sourceUnit,
      WeightUnit targetUnit) {
    validateValue(inputValue);
    Objects.requireNonNull(sourceUnit, "Source unit cannot be null");
    Objects.requireNonNull(targetUnit, "Target unit cannot be null");
  }

  private static void validateValue(double value) {
    if (!Double.isFinite(value)) {
      throw new IllegalArgumentException("Weight value must be numeric");
    }
  }
}