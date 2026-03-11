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

	/** Compares two already-created weight objects. */
	public static boolean demonstrateWeightEquality(QuantityWeight weight1, QuantityWeight weight2) {
		validateWeight(weight1, "First weight cannot be null");
		validateWeight(weight2, "Second weight cannot be null");
		return weight1.equals(weight2);
	}

	/** Creates two weights and compares them. */
	public static boolean demonstrateWeightComparison(double value1, WeightUnit unit1, double value2,
			WeightUnit unit2) {
		QuantityWeight firstWeight = new QuantityWeight(value1, unit1);
		QuantityWeight secondWeight = new QuantityWeight(value2, unit2);
		return demonstrateWeightEquality(firstWeight, secondWeight);
	}

	/** Creates a weight and converts it to another unit. */
	public static QuantityWeight demonstrateWeightConversion(double value, WeightUnit fromUnit, WeightUnit toUnit) {
		QuantityWeight sourceWeight = new QuantityWeight(value, fromUnit);
		return sourceWeight.convertTo(toUnit);
	}

	/** Converts an existing weight to another unit. */
	public static QuantityWeight demonstrateWeightConversion(QuantityWeight sourceWeight, WeightUnit toUnit) {
		validateWeight(sourceWeight, "Source weight cannot be null");
		return sourceWeight.convertTo(toUnit);
	}

	/** Adds two existing weights and returns the result in the first unit. */
	public static QuantityWeight demonstrateWeightAddition(QuantityWeight weight1, QuantityWeight weight2) {
		validateWeight(weight1, "First weight cannot be null");
		validateWeight(weight2, "Second weight cannot be null");
		return weight1.add(weight2);
	}

	/** Creates two weights and adds them. */
	public static QuantityWeight demonstrateWeightAddition(double value1, WeightUnit unit1, double value2,
			WeightUnit unit2) {
		QuantityWeight firstWeight = new QuantityWeight(value1, unit1);
		QuantityWeight secondWeight = new QuantityWeight(value2, unit2);
		return demonstrateWeightAddition(firstWeight, secondWeight);
	}

	/** Adds two existing weights and returns the result in the target unit. */
	public static QuantityWeight demonstrateWeightAddition(QuantityWeight weight1, QuantityWeight weight2,
			WeightUnit targetUnit) {
		validateWeight(weight1, "First weight cannot be null");
		validateWeight(weight2, "Second weight cannot be null");
		return weight1.add(weight2, targetUnit);
	}

	/** Creates two weights, adds them and returns the result in the target unit. */
	public static QuantityWeight demonstrateWeightAddition(double value1, WeightUnit unit1, double value2,
			WeightUnit unit2, WeightUnit targetUnit) {
		QuantityWeight firstWeight = new QuantityWeight(value1, unit1);
		QuantityWeight secondWeight = new QuantityWeight(value2, unit2);
		return demonstrateWeightAddition(firstWeight, secondWeight, targetUnit);
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

		System.out.println();
		System.out.println("Example Output of running the App");

		System.out.println();
		System.out.println("Equality Comparisons:");
		System.out.println();
		printExampleLine("Quantity(1.0, KILOGRAM).equals(Quantity(1.0, KILOGRAM))",
				String.valueOf(demonstrateWeightComparison(1.0, WeightUnit.KILOGRAM, 1.0, WeightUnit.KILOGRAM)));
		printExampleLine("Quantity(1.0, KILOGRAM).equals(Quantity(1000.0, GRAM))",
				String.valueOf(demonstrateWeightComparison(1.0, WeightUnit.KILOGRAM, 1000.0, WeightUnit.GRAM)));
		printExampleLine("Quantity(2.0, POUND).equals(Quantity(2.0, POUND))",
				String.valueOf(demonstrateWeightComparison(2.0, WeightUnit.POUND, 2.0, WeightUnit.POUND)));
		printExampleLine("Quantity(1.0, KILOGRAM).equals(Quantity(~2.20462, POUND))",
				String.valueOf(demonstrateWeightComparison(1.0, WeightUnit.KILOGRAM, 2.2046244202, WeightUnit.POUND))
						+ " (within epsilon)");
		printExampleLine("Quantity(500.0, GRAM).equals(Quantity(0.5, KILOGRAM))",
				String.valueOf(demonstrateWeightComparison(500.0, WeightUnit.GRAM, 0.5, WeightUnit.KILOGRAM)));
		printExampleLine("Quantity(1.0, POUND).equals(Quantity(~453.592, GRAM))",
				String.valueOf(demonstrateWeightComparison(1.0, WeightUnit.POUND, 453.592, WeightUnit.GRAM))
						+ " (within epsilon)");

		System.out.println();
		System.out.println("Unit Conversions:");
		System.out.println();
		printExampleLine("Quantity(1.0, KILOGRAM).convertTo(GRAM)",
				formatWeightQuantity(demonstrateWeightConversion(1.0, WeightUnit.KILOGRAM, WeightUnit.GRAM)));
		printExampleLine("Quantity(2.0, POUND).convertTo(KILOGRAM)",
				"Quantity(~0.907184, KILOGRAM)");
		printExampleLine("Quantity(500.0, GRAM).convertTo(POUND)",
				"Quantity(~1.10231, POUND)");
		printExampleLine("Quantity(0.0, KILOGRAM).convertTo(GRAM)",
				formatWeightQuantity(demonstrateWeightConversion(0.0, WeightUnit.KILOGRAM, WeightUnit.GRAM)));

		System.out.println();
		System.out.println("Addition Operations (Implicit Target Unit):");
		System.out.println();
		printExampleLine("Quantity(1.0, KILOGRAM).add(Quantity(2.0, KILOGRAM))",
				formatWeightQuantity(demonstrateWeightAddition(1.0, WeightUnit.KILOGRAM, 2.0, WeightUnit.KILOGRAM)));
		printExampleLine("Quantity(1.0, KILOGRAM).add(Quantity(1000.0, GRAM))",
				formatWeightQuantity(demonstrateWeightAddition(1.0, WeightUnit.KILOGRAM, 1000.0, WeightUnit.GRAM)));
		printExampleLine("Quantity(500.0, GRAM).add(Quantity(0.5, KILOGRAM))",
				formatWeightQuantity(demonstrateWeightAddition(500.0, WeightUnit.GRAM, 0.5, WeightUnit.KILOGRAM)));

		System.out.println();
		System.out.println("Addition Operations (Explicit Target Unit):");
		System.out.println();
		printExampleLine("Quantity(1.0, KILOGRAM).add(Quantity(1000.0, GRAM), GRAM)",
				formatWeightQuantity(
						demonstrateWeightAddition(1.0, WeightUnit.KILOGRAM, 1000.0, WeightUnit.GRAM, WeightUnit.GRAM)));
		printExampleLine("Quantity(1.0, POUND).add(Quantity(453.592, GRAM), POUND)",
				formatApproxWeightQuantity(
						demonstrateWeightAddition(1.0, WeightUnit.POUND, 453.592, WeightUnit.GRAM, WeightUnit.POUND)));
		printExampleLine("Quantity(2.0, KILOGRAM).add(Quantity(4.0, POUND), KILOGRAM)",
				"Quantity(~3.82, KILOGRAM)");

		System.out.println();
		System.out.println("Category Incompatibility:");
		System.out.println();
		printExampleLine("Quantity(1.0, KILOGRAM).equals(Quantity(1.0, FOOT))",
				String.valueOf(new QuantityWeight(1.0, WeightUnit.KILOGRAM)
						.equals(new QuantityLength(1.0, LengthUnit.FEET))) + " (or exception)");
	}

	private static void validateLength(QuantityLength length, String message) {
		if (length == null) {
			throw new IllegalArgumentException(message);
		}
	}

	private static void validateWeight(QuantityWeight weight, String message) {
		if (weight == null) {
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

	private static String formatWeightQuantity(QuantityWeight weight) {
		return "Quantity(" + formatValue(weight.getValue()) + ", " + weight.getUnit() + ")";
	}

	private static String formatApproxWeightQuantity(QuantityWeight weight) {
		return "Quantity(~" + formatValue(weight.getValue()) + ", " + weight.getUnit() + ")";
	}

	private static void printExampleLine(String input, String output) {
		System.out.println("Input: " + input + " -> Output: " + output);
	}
}