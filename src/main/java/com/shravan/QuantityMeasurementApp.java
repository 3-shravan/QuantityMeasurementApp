package com.shravan;

import java.text.DecimalFormat;

public final class QuantityMeasurementApp {

	private static final DecimalFormat VALUE_FORMAT = new DecimalFormat("0.0###");

	private QuantityMeasurementApp() {
	}

	/** Compares two already-created length objects. */
	public static boolean demonstrateLengthEquality(QuantityLength length1, QuantityLength length2) {
		validateLength(length1, "First length cannot be null");
		validateLength(length2, "Second length cannot be null");
		return length1.equals(length2);
	}

	/** Creates two lengths and compares them. */
	public static boolean demonstrateLengthComparison(double value1, LengthUnit unit1, double value2,
			LengthUnit unit2) {
		QuantityLength firstLength = new QuantityLength(value1, unit1);
		QuantityLength secondLength = new QuantityLength(value2, unit2);
		return demonstrateLengthEquality(firstLength, secondLength);
	}

	/** Creates a length and converts it to another unit. */
	public static QuantityLength demonstrateLengthConversion(double value, LengthUnit fromUnit, LengthUnit toUnit) {
		QuantityLength sourceLength = new QuantityLength(value, fromUnit);
		return sourceLength.convertTo(toUnit);
	}

	/** Converts an existing length to another unit. */
	public static QuantityLength demonstrateLengthConversion(QuantityLength sourceLength, LengthUnit toUnit) {
		validateLength(sourceLength, "Source length cannot be null");
		return sourceLength.convertTo(toUnit);
	}

	/** Adds two existing lengths and returns the result in the first unit. */
	public static QuantityLength demonstrateLengthAddition(QuantityLength length1, QuantityLength length2) {
		validateLength(length1, "First length cannot be null");
		validateLength(length2, "Second length cannot be null");
		return length1.add(length2);
	}

	/** Creates two lengths and adds them. */
	public static QuantityLength demonstrateLengthAddition(double value1, LengthUnit unit1, double value2,
			LengthUnit unit2) {
		QuantityLength firstLength = new QuantityLength(value1, unit1);
		QuantityLength secondLength = new QuantityLength(value2, unit2);
		return demonstrateLengthAddition(firstLength, secondLength);
	}

	/** Adds two existing lengths and returns the result in the target unit. */
	public static QuantityLength demonstrateLengthAddition(QuantityLength length1, QuantityLength length2,
			LengthUnit targetUnit) {
		validateLength(length1, "First length cannot be null");
		validateLength(length2, "Second length cannot be null");
		return length1.add(length2, targetUnit);
	}

	/** Creates two lengths, adds them and returns the result in the target unit. */
	public static QuantityLength demonstrateLengthAddition(double value1, LengthUnit unit1, double value2,
			LengthUnit unit2, LengthUnit targetUnit) {
		QuantityLength firstLength = new QuantityLength(value1, unit1);
		QuantityLength secondLength = new QuantityLength(value2, unit2);
		return demonstrateLengthAddition(firstLength, secondLength, targetUnit);
	}

	// Earlier use-case demonstration methods are kept for reference.

	public static void demonstrateFeetEquality() {
		printComparisonDemo("Feet equality", 1.0, LengthUnit.FEET, 1.0, LengthUnit.FEET);
	}

	public static void demonstrateInchesEquality() {
		printComparisonDemo("Inches equality", 1.0, LengthUnit.INCHES, 1.0, LengthUnit.INCHES);
	}

	public static void demonstrateFeetAndInchesComparison() {
		printComparisonDemo("Feet and Inches comparison", 1.0, LengthUnit.FEET, 12.0, LengthUnit.INCHES);
	}

	public static void demonstrateYardsAndInchesComparison() {
		printComparisonDemo("Yards and Inches comparison", 1.0, LengthUnit.YARDS, 36.0, LengthUnit.INCHES);
	}

	public static void demonstrateCentimetersAndInchesComparison() {
		printComparisonDemo("Centimeters and Inches comparison", 100.0, LengthUnit.CENTIMETERS, 39.3701,
				LengthUnit.INCHES);
	}

	public static void demonstrateFeetAndYardsComparison() {
		printComparisonDemo("Feet and Yards comparison", 3.0, LengthUnit.FEET, 1.0, LengthUnit.YARDS);
	}

	public static void demonstrateCentimetersAndFeetComparison() {
		printComparisonDemo("Centimeters and Feet comparison", 30.48, LengthUnit.CENTIMETERS, 1.0,
				LengthUnit.FEET);
	}

	public static void demonstrateConversionFeetToInches() {
		printConversionDemo("Conversion Feet to Inches", 3.0, LengthUnit.FEET, LengthUnit.INCHES);
	}

	public static void demonstrateConversionYardsToInches() {
		printConversionDemo("Conversion Yards to Inches", 2.0, LengthUnit.YARDS, LengthUnit.INCHES);
	}

	public static void demonstrateConversionCentimetersToFeet() {
		printConversionDemo("Conversion Centimeters to Feet", 30.48, LengthUnit.CENTIMETERS, LengthUnit.FEET);
	}

	public static void demonstrateAdditionUseCases() {
		printAdditionDemo(1.0, LengthUnit.FEET, 2.0, LengthUnit.FEET);
		printAdditionDemo(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCHES);
		printAdditionDemo(12.0, LengthUnit.INCHES, 1.0, LengthUnit.FEET);
		printAdditionDemo(1.0, LengthUnit.YARDS, 3.0, LengthUnit.FEET);
		printAdditionDemo(36.0, LengthUnit.INCHES, 1.0, LengthUnit.YARDS);
		printAdditionDemo(2.54, LengthUnit.CENTIMETERS, 1.0, LengthUnit.INCHES);
		printAdditionDemo(5.0, LengthUnit.FEET, 0.0, LengthUnit.INCHES);
		printAdditionDemo(5.0, LengthUnit.FEET, -2.0, LengthUnit.FEET);
	}

	public static void demonstrateAdditionWithTargetUnitUseCases() {
		printAdditionDemo(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCHES, LengthUnit.FEET);
		printAdditionDemo(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCHES, LengthUnit.INCHES);
		printAdditionDemo(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCHES, LengthUnit.YARDS);
		printAdditionDemo(1.0, LengthUnit.YARDS, 3.0, LengthUnit.FEET, LengthUnit.YARDS);
		printAdditionDemo(36.0, LengthUnit.INCHES, 1.0, LengthUnit.YARDS, LengthUnit.FEET);
		printAdditionDemo(2.54, LengthUnit.CENTIMETERS, 1.0, LengthUnit.INCHES, LengthUnit.CENTIMETERS);
		printAdditionDemo(5.0, LengthUnit.FEET, 0.0, LengthUnit.INCHES, LengthUnit.YARDS);
		printAdditionDemo(5.0, LengthUnit.FEET, -2.0, LengthUnit.FEET, LengthUnit.INCHES);
	}

	public static void main(String[] args) {
		// demonstrateFeetEquality();
		// demonstrateInchesEquality();
		// demonstrateFeetAndInchesComparison();
		// demonstrateYardsAndInchesComparison();
		// demonstrateCentimetersAndInchesComparison();
		// demonstrateFeetAndYardsComparison();
		// demonstrateCentimetersAndFeetComparison();
		// demonstrateConversionFeetToInches();
		// demonstrateConversionYardsToInches();
		// demonstrateConversionCentimetersToFeet();
		// demonstrateAdditionUseCases();
		// demonstrateAdditionWithTargetUnitUseCases();

		System.out.println();
		System.out.println("========================================");
		System.out.println("   QUANTITY MEASUREMENT APPLICATION");
		System.out.println("========================================");

		System.out.println();
		System.out.println("===== UC8: REFACTORED DESIGN - UNIT RESPONSIBILITY =====");

		// Direct conversion using methods defined inside LengthUnit.
		System.out.println();
		System.out.println("--- LengthUnit Conversion Methods ---");
		System.out.printf("LengthUnit.FEET.convertToBaseUnit(12.0) = %.2f feet%n",
				LengthUnit.FEET.convertToBaseUnit(12.0));
		System.out.printf("LengthUnit.INCHES.convertToBaseUnit(12.0) = %.2f feet%n",
				LengthUnit.INCHES.convertToBaseUnit(12.0));
		System.out.printf("LengthUnit.YARDS.convertToBaseUnit(1.0) = %.2f feet%n",
				LengthUnit.YARDS.convertToBaseUnit(1.0));
		System.out.printf("LengthUnit.CENTIMETERS.convertToBaseUnit(30.48) = %.2f feet%n",
				LengthUnit.CENTIMETERS.convertToBaseUnit(30.48));

		// Conversion from the base unit back to each unit.
		System.out.println();
		System.out.println("--- From Base Unit Conversions ---");
		System.out.printf("LengthUnit.FEET.convertFromBaseUnit(2.0) = %.2f feet%n",
				LengthUnit.FEET.convertFromBaseUnit(2.0));
		System.out.printf("LengthUnit.INCHES.convertFromBaseUnit(1.0) = %.2f inches%n",
				LengthUnit.INCHES.convertFromBaseUnit(1.0));
		System.out.printf("LengthUnit.YARDS.convertFromBaseUnit(3.0) = %.2f yards%n",
				LengthUnit.YARDS.convertFromBaseUnit(3.0));
		System.out.printf("LengthUnit.CENTIMETERS.convertFromBaseUnit(1.0) = %.2f centimeters%n",
				LengthUnit.CENTIMETERS.convertFromBaseUnit(1.0));

		// Real operations using the refactored Length class.
		System.out.println();
		System.out.println("--- Refactored Length Operations ---");

		QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
		QuantityLength inches = new QuantityLength(12.0, LengthUnit.INCHES);

		System.out.printf("Quantity(1.0, FEET).equals(Quantity(12.0, INCHES)) = %b%n", feet.equals(inches));
		System.out.printf("Quantity(1.0, FEET).convertTo(INCHES) = %s%n",
				formatQuantity(feet.convertTo(LengthUnit.INCHES)));
		System.out.printf("Quantity(1.0, FEET).add(Quantity(12.0, INCHES), FEET) = %s%n",
				formatQuantity(feet.add(inches, LengthUnit.FEET)));
		System.out.printf("Quantity(1.0, FEET).add(Quantity(12.0, INCHES), YARDS) = %s%n",
				formatQuantity(feet.add(inches, LengthUnit.YARDS)));

		System.out.println();
		System.out.println("========================================");
		System.out.println("     Application Execution Complete");
		System.out.println("========================================");
	}

	private static void validateLength(QuantityLength length, String message) {
		if (length == null) {
			throw new IllegalArgumentException(message);
		}
	}

	private static void printComparisonDemo(String title, double value1, LengthUnit unit1, double value2,
			LengthUnit unit2) {
		boolean result = demonstrateLengthComparison(value1, unit1, value2, unit2);
		System.out.println(title + " -> Quantity(" + value1 + ", " + unit1 + ") and Quantity(" + value2 + ", "
				+ unit2 + ") = " + result);
	}

	private static void printConversionDemo(String title, double value, LengthUnit fromUnit, LengthUnit toUnit) {
		QuantityLength converted = demonstrateLengthConversion(value, fromUnit, toUnit);
		System.out.println(title + " -> Quantity(" + value + ", " + fromUnit + ") to " + toUnit + " = "
				+ converted);
	}

	private static void printAdditionDemo(double value1, LengthUnit unit1, double value2, LengthUnit unit2) {
		QuantityLength result = demonstrateLengthAddition(value1, unit1, value2, unit2);
		System.out.println("Input: add(Quantity(" + value1 + ", " + unit1 + "), Quantity(" + value2 + ", " + unit2
				+ "))");
		System.out.println("Output: " + formatQuantity(result));
	}

	private static void printAdditionDemo(double value1, LengthUnit unit1, double value2, LengthUnit unit2,
			LengthUnit targetUnit) {
		QuantityLength result = demonstrateLengthAddition(value1, unit1, value2, unit2, targetUnit);
		System.out.println("Input: add(Quantity(" + value1 + ", " + unit1 + "), Quantity(" + value2 + ", " + unit2
				+ "), " + targetUnit + ")");
		System.out.println("Output: " + formatQuantity(result));
	}

	private static String formatValue(double value) {
		return VALUE_FORMAT.format(value);
	}

	private static String formatQuantity(QuantityLength length) {
		return "Quantity(" + formatValue(length.getValue()) + ", " + length.getUnit() + ")";
	}
}