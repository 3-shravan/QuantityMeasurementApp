package com.shravan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

public final class Quantity<U extends IMeasurable> {

  private static final int OUTPUT_SCALE = 6;
  private static final int BASE_COMPARISON_SCALE = 4;

  private final double value;
  private final U unit;

  public Quantity(double value, U unit) {
    IMeasurable.validateValue(value);
    if (unit == null) {
      throw new IllegalArgumentException("Unit cannot be null");
    }
    this.unit = unit;
    this.value = value;
  }

  public double getValue() {
    return value;
  }

  public U getUnit() {
    return unit;
  }

  public boolean compare(Quantity<U> other) {
    validateCompatibleQuantity(other, "Quantity to compare cannot be null");
    return normalizedBaseValue() == other.normalizedBaseValue();
  }

  public Quantity<U> convertTo(U targetUnit) {
    validateCompatibleUnit(targetUnit, "Target unit cannot be null");
    double convertedValue = targetUnit.convertFromBaseUnit(toBaseUnit());
    return new Quantity<>(round(convertedValue, OUTPUT_SCALE), targetUnit);
  }

  public Quantity<U> add(Quantity<U> other) {
    return add(other, unit);
  }

  public Quantity<U> add(Quantity<U> other, U targetUnit) {
    double sumInBaseUnit = performBaseArithmetic(other, targetUnit, ArithmeticOperation.ADD,
        "Quantity to add cannot be null");
    double convertedValue = targetUnit.convertFromBaseUnit(sumInBaseUnit);
    return new Quantity<>(round(convertedValue, OUTPUT_SCALE), targetUnit);
  }

  public Quantity<U> subtract(Quantity<U> other) {
    return subtract(other, unit);
  }

  public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
    double differenceInBaseUnit = performBaseArithmetic(other, targetUnit, ArithmeticOperation.SUBTRACT,
        "Quantity to subtract cannot be null");
    double convertedValue = targetUnit.convertFromBaseUnit(differenceInBaseUnit);
    return new Quantity<>(round(convertedValue, OUTPUT_SCALE), targetUnit);
  }

  public double divide(Quantity<U> other) {
    double quotientInBaseUnit = performBaseArithmetic(other, null, ArithmeticOperation.DIVIDE,
        "Quantity to divide cannot be null");
    return round(quotientInBaseUnit, OUTPUT_SCALE);
  }

  public static <U extends IMeasurable> Quantity<U> add(double firstValue, U firstUnit,
      double secondValue, U secondUnit, U targetUnit) {
    return new Quantity<>(firstValue, firstUnit).add(new Quantity<>(secondValue, secondUnit), targetUnit);
  }

  public static <U extends IMeasurable> Quantity<U> subtract(double firstValue, U firstUnit,
      double secondValue, U secondUnit, U targetUnit) {
    return new Quantity<>(firstValue, firstUnit).subtract(new Quantity<>(secondValue, secondUnit), targetUnit);
  }

  public static <U extends IMeasurable> double convert(double value, U sourceUnit, U targetUnit) {
    return new Quantity<>(value, sourceUnit).convertTo(targetUnit).getValue();
  }

  public static <U extends IMeasurable> double divide(double firstValue, U firstUnit,
      double secondValue, U secondUnit) {
    return new Quantity<>(firstValue, firstUnit).divide(new Quantity<>(secondValue, secondUnit));
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Quantity<?>)) {
      return false;
    }

    Quantity<?> that = (Quantity<?>) other;
    if (this.unit.getClass() != that.unit.getClass()) {
      return false;
    }

    return Double.compare(this.normalizedBaseValue(), that.normalizedBaseValue()) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(unit.getClass(), normalizedBaseValue());
  }

  @Override
  public String toString() {
    return "Quantity(" + formatValue(value) + ", " + unit.getUnitName() + ")";
  }

  private double toBaseUnit() {
    return unit.convertToBaseUnit(value);
  }

  private double normalizedBaseValue() {
    return round(toBaseUnit(), BASE_COMPARISON_SCALE);
  }

  private double performBaseArithmetic(Quantity<U> other, U targetUnit, ArithmeticOperation operation,
      String nullOperandMessage) {
    boolean targetUnitRequired = operation != ArithmeticOperation.DIVIDE;
    validateArithmeticOperands(other, targetUnit, targetUnitRequired, nullOperandMessage);
    unit.validateOperationSupport(operation.name());
    other.unit.validateOperationSupport(operation.name());
    double thisBaseValue = toBaseUnit();
    double otherBaseValue = other.toBaseUnit();
    return operation.compute(thisBaseValue, otherBaseValue);
  }

  private void validateArithmeticOperands(Quantity<U> other, U targetUnit, boolean targetUnitRequired,
      String nullOperandMessage) {
    validateCompatibleQuantity(other, nullOperandMessage);
    IMeasurable.validateValue(value);
    IMeasurable.validateValue(other.value);

    if (targetUnitRequired) {
      validateCompatibleUnit(targetUnit, "Target unit cannot be null");
    }
  }

  private void validateCompatibleQuantity(Quantity<?> other, String nullMessage) {
    if (other == null) {
      throw new IllegalArgumentException(nullMessage);
    }
    if (unit.getClass() != other.unit.getClass()) {
      throw new IllegalArgumentException("Quantities must belong to the same measurement category");
    }
  }

  private void validateCompatibleUnit(IMeasurable targetUnit, String nullMessage) {
    if (targetUnit == null) {
      throw new IllegalArgumentException(nullMessage);
    }
    if (unit.getClass() != targetUnit.getClass()) {
      throw new IllegalArgumentException("Target unit must belong to the same measurement category");
    }
  }

  private static double round(double value, int scale) {
    return BigDecimal.valueOf(value)
        .setScale(scale, RoundingMode.HALF_UP)
        .doubleValue();
  }

  private enum ArithmeticOperation {
    ADD((a, b) -> a + b),
    SUBTRACT((a, b) -> a - b),
    DIVIDE((a, b) -> {
      if (Double.compare(b, 0.0) == 0) {
        throw new ArithmeticException("Cannot divide by zero quantity");
      }
      return a / b;
    });

    private final DoubleBinaryOperator operation;

    ArithmeticOperation(DoubleBinaryOperator operation) {
      this.operation = operation;
    }

    double compute(double thisBase, double otherBase) {
      return operation.applyAsDouble(thisBase, otherBase);
    }
  }

  private static String formatValue(double value) {
    BigDecimal formattedValue = BigDecimal.valueOf(value).stripTrailingZeros();
    if (formattedValue.scale() < 1) {
      formattedValue = formattedValue.setScale(1, RoundingMode.UNNECESSARY);
    }
    return formattedValue.toPlainString();
  }
}