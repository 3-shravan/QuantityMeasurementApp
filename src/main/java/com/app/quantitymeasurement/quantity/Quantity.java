package com.app.quantitymeasurement.quantity;

import com.app.quantitymeasurement.unit.IMeasurable;

public class Quantity<U extends IMeasurable> {

  private final double value;
  private final U unit;

  public Quantity(double value, U unit) {
    IMeasurable.validate(value);
    if (unit == null) {
      throw new IllegalArgumentException("Unit cannot be null");
    }
    this.value = value;
    this.unit = unit;
  }

  public double getValue() {
    return value;
  }

  public U getUnit() {
    return unit;
  }

  public Quantity<U> convertTo(U targetUnit) {
    if (targetUnit == null) {
      throw new IllegalArgumentException("Target unit cannot be null");
    }
    validateCompatibleUnit(targetUnit);
    double base = unit.convertToBaseUnit(value);
    return new Quantity<>(targetUnit.convertFromBaseUnit(base), targetUnit);
  }

  public Quantity<U> add(Quantity<U> other) {
    return add(other, unit);
  }

  public Quantity<U> add(Quantity<U> other, U targetUnit) {
    if (other == null) {
      throw new IllegalArgumentException("Operand quantity cannot be null");
    }
    validateCompatibleQuantity(other);
    validateCompatibleUnit(targetUnit);
    unit.validateOperationSupport("ADD");
    other.unit.validateOperationSupport("ADD");

    double base = unit.convertToBaseUnit(value) + other.unit.convertToBaseUnit(other.value);
    return new Quantity<>(targetUnit.convertFromBaseUnit(base), targetUnit);
  }

  public Quantity<U> subtract(Quantity<U> other) {
    return subtract(other, unit);
  }

  public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
    if (other == null) {
      throw new IllegalArgumentException("Operand quantity cannot be null");
    }
    validateCompatibleQuantity(other);
    validateCompatibleUnit(targetUnit);
    unit.validateOperationSupport("SUBTRACT");
    other.unit.validateOperationSupport("SUBTRACT");

    double base = unit.convertToBaseUnit(value) - other.unit.convertToBaseUnit(other.value);
    return new Quantity<>(targetUnit.convertFromBaseUnit(base), targetUnit);
  }

  public double divide(Quantity<U> other) {
    if (other == null) {
      throw new IllegalArgumentException("Operand quantity cannot be null");
    }
    validateCompatibleQuantity(other);
    unit.validateOperationSupport("DIVIDE");
    other.unit.validateOperationSupport("DIVIDE");

    double denominator = other.unit.convertToBaseUnit(other.value);
    if (Double.compare(denominator, 0.0) == 0) {
      throw new ArithmeticException("Cannot divide by zero quantity");
    }
    return unit.convertToBaseUnit(value) / denominator;
  }

  private void validateCompatibleQuantity(Quantity<?> other) {
    if (unit.getClass() != other.unit.getClass()) {
      throw new IllegalArgumentException("Quantities must belong to the same measurement category");
    }
  }

  private void validateCompatibleUnit(IMeasurable targetUnit) {
    if (targetUnit == null) {
      throw new IllegalArgumentException("Target unit cannot be null");
    }
    if (unit.getClass() != targetUnit.getClass()) {
      throw new IllegalArgumentException("Target unit must belong to the same measurement category");
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Quantity<?>)) {
      return false;
    }
    Quantity<?> other = (Quantity<?>) obj;
    if (unit.getClass() != other.unit.getClass()) {
      return false;
    }
    double left = unit.convertToBaseUnit(value);
    double right = other.unit.convertToBaseUnit(other.value);
    return Math.abs(left - right) < 1e-3;
  }

  @Override
  public int hashCode() {
    return Double.hashCode(unit.convertToBaseUnit(value));
  }

  @Override
  public String toString() {
    return "Quantity(" + value + ", " + unit + ")";
  }
}
