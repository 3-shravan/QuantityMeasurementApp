package com.shravan;

public class QuantityMeasurementApp {

	public static boolean demonstrateLengthEquality(Length length1, Length length2) {
		if (length1 == null || length2 == null) {
			throw new IllegalArgumentException("Lengths cannot be null");
		}
		return length1.equals(length2);
	}

	public static boolean demonstrateLengthComparison(double value1, Length.LengthUnit unit1, double value2,
			Length.LengthUnit unit2) {
		Length length1 = new Length(value1, unit1);
		Length length2 = new Length(value2, unit2);
		return demonstrateLengthEquality(length1, length2);
	}

	public static Length demonstrateLengthConversion(double value, Length.LengthUnit fromUnit,
			Length.LengthUnit toUnit) {
		Length sourceLength = new Length(value, fromUnit);
		return sourceLength.convertTo(toUnit);
	}

	public static Length demonstrateLengthConversion(Length sourceLength, Length.LengthUnit toUnit) {
		if (sourceLength == null) {
			throw new IllegalArgumentException("Source length cannot be null");
		}
		return sourceLength.convertTo(toUnit);
	}

	public static Length demonstrateLengthAddition(Length length1, Length length2) {
		if (length1 == null || length2 == null) {
			throw new IllegalArgumentException("Lengths to add cannot be null");
		}
		return length1.add(length2);
	}

	public static Length demonstrateLengthAddition(double value1, Length.LengthUnit unit1, double value2,
			Length.LengthUnit unit2) {
		Length length1 = new Length(value1, unit1);
		Length length2 = new Length(value2, unit2);
		return demonstrateLengthAddition(length1, length2);
	}

	private static void printDemo(String title, double value1, Length.LengthUnit unit1, double value2,
			Length.LengthUnit unit2) {
		boolean result = demonstrateLengthComparison(value1, unit1, value2, unit2);
		System.out.println(title + " -> Quantity(" + value1 + ", " + unit1 + ") and Quantity(" + value2 + ", "
				+ unit2 + ") = " + result);
	}

	private static void printConversionDemo(String title, double value, Length.LengthUnit fromUnit,
			Length.LengthUnit toUnit) {
		Length converted = demonstrateLengthConversion(value, fromUnit, toUnit);
		System.out.println(title + " -> Quantity(" + value + ", " + fromUnit + ") to " + toUnit + " = "
				+ converted);
	}

	public static void demonstrateFeetEquality() {
		printDemo("Feet equality", 1.0, Length.LengthUnit.FEET, 1.0, Length.LengthUnit.FEET);
	}

	public static void demonstrateInchesEquality() {
		printDemo("Inches equality", 1.0, Length.LengthUnit.INCHES, 1.0, Length.LengthUnit.INCHES);
	}

	public static void demonstrateFeetAndInchesComparison() {
		printDemo("Feet and Inches comparison", 1.0, Length.LengthUnit.FEET, 12.0, Length.LengthUnit.INCHES);
	}

	public static void demonstrateYardsAndInchesComparison() {
		printDemo("Yards and Inches comparison", 1.0, Length.LengthUnit.YARDS, 36.0, Length.LengthUnit.INCHES);
	}

	public static void demonstrateCentimetersAndInchesComparison() {
		printDemo("Centimeters and Inches comparison", 100.0, Length.LengthUnit.CENTIMETERS, 39.3701,
				Length.LengthUnit.INCHES);
	}

	public static void demonstrateFeetAndYardsComparison() {
		printDemo("Feet and Yards comparison", 3.0, Length.LengthUnit.FEET, 1.0, Length.LengthUnit.YARDS);
	}

	public static void demonstrateCentimetersAndFeetComparison() {
		printDemo("Centimeters and Feet comparison", 30.48, Length.LengthUnit.CENTIMETERS, 1.0,
				Length.LengthUnit.FEET);
	}

	public static void demonstrateConversionFeetToInches() {
		printConversionDemo("Conversion Feet to Inches", 3.0, Length.LengthUnit.FEET, Length.LengthUnit.INCHES);
	}

	public static void demonstrateConversionYardsToInches() {
		printConversionDemo("Conversion Yards to Inches", 2.0, Length.LengthUnit.YARDS, Length.LengthUnit.INCHES);
	}

	public static void demonstrateConversionCentimetersToFeet() {
		printConversionDemo("Conversion Centimeters to Feet", 30.48, Length.LengthUnit.CENTIMETERS,
				Length.LengthUnit.FEET);
	}

	private static void printAdditionDemo(double value1, Length.LengthUnit unit1, double value2,
			Length.LengthUnit unit2) {
		Length result = demonstrateLengthAddition(value1, unit1, value2, unit2);
		System.out.println("Input: add(Quantity(" + value1 + ", " + unit1 + "), Quantity(" + value2 + ", " + unit2
				+ "))");
		System.out.println("Output: Quantity(" + result.getValue() + ", " + result.getUnit() + ")");
	}

	public static void demonstrateAdditionUseCases() {
		printAdditionDemo(1.0, Length.LengthUnit.FEET, 2.0, Length.LengthUnit.FEET);
		printAdditionDemo(1.0, Length.LengthUnit.FEET, 12.0, Length.LengthUnit.INCHES);
		printAdditionDemo(12.0, Length.LengthUnit.INCHES, 1.0, Length.LengthUnit.FEET);
		printAdditionDemo(1.0, Length.LengthUnit.YARDS, 3.0, Length.LengthUnit.FEET);
		printAdditionDemo(36.0, Length.LengthUnit.INCHES, 1.0, Length.LengthUnit.YARDS);
		printAdditionDemo(2.54, Length.LengthUnit.CENTIMETERS, 1.0, Length.LengthUnit.INCHES);
		printAdditionDemo(5.0, Length.LengthUnit.FEET, 0.0, Length.LengthUnit.INCHES);
		printAdditionDemo(5.0, Length.LengthUnit.FEET, -2.0, Length.LengthUnit.FEET);
	}

	public static Length demonstrateLengthAddition(Length length1, Length length2, Length.LengthUnit targetUnit) {
		if (length1 == null || length2 == null) {
			throw new IllegalArgumentException("Lengths to add cannot be null");
		}
		return length1.add(length2, targetUnit);
	}

	public static Length demonstrateLengthAddition(double value1, Length.LengthUnit unit1, double value2,
			Length.LengthUnit unit2, Length.LengthUnit targetUnit) {
		Length length1 = new Length(value1, unit1);
		Length length2 = new Length(value2, unit2);
		return demonstrateLengthAddition(length1, length2, targetUnit);
	}

	private static void printAdditionDemo(double value1, Length.LengthUnit unit1, double value2,
			Length.LengthUnit unit2, Length.LengthUnit targetUnit) {
		Length result = demonstrateLengthAddition(value1, unit1, value2, unit2, targetUnit);
		System.out.println("Input: add(Quantity(" + value1 + ", " + unit1 + "), Quantity(" + value2 + ", " + unit2
				+ "), " + targetUnit + ")");
		System.out.println("Output: Quantity(" + result.getValue() + ", " + result.getUnit() + ")");
	}

	public static void demonstrateAdditionWithTargetUnitUseCases() {
		printAdditionDemo(1.0, Length.LengthUnit.FEET, 12.0, Length.LengthUnit.INCHES, Length.LengthUnit.FEET);
		printAdditionDemo(1.0, Length.LengthUnit.FEET, 12.0, Length.LengthUnit.INCHES, Length.LengthUnit.INCHES);
		printAdditionDemo(1.0, Length.LengthUnit.FEET, 12.0, Length.LengthUnit.INCHES, Length.LengthUnit.YARDS);
		printAdditionDemo(1.0, Length.LengthUnit.YARDS, 3.0, Length.LengthUnit.FEET, Length.LengthUnit.YARDS);
		printAdditionDemo(36.0, Length.LengthUnit.INCHES, 1.0, Length.LengthUnit.YARDS, Length.LengthUnit.FEET);
		printAdditionDemo(2.54, Length.LengthUnit.CENTIMETERS, 1.0, Length.LengthUnit.INCHES,
				Length.LengthUnit.CENTIMETERS);
		printAdditionDemo(5.0, Length.LengthUnit.FEET, 0.0, Length.LengthUnit.INCHES, Length.LengthUnit.YARDS);
		printAdditionDemo(5.0, Length.LengthUnit.FEET, -2.0, Length.LengthUnit.FEET, Length.LengthUnit.INCHES);
	}

	public static void main(String[] args) {
		demonstrateFeetEquality();
		demonstrateInchesEquality();
		demonstrateFeetAndInchesComparison();
		demonstrateYardsAndInchesComparison();
		demonstrateCentimetersAndInchesComparison();
		demonstrateFeetAndYardsComparison();
		demonstrateCentimetersAndFeetComparison();

		demonstrateConversionFeetToInches();
		demonstrateConversionYardsToInches();
		demonstrateConversionCentimetersToFeet();

		// uc-6
		demonstrateAdditionUseCases();

		// uc-7
		demonstrateAdditionWithTargetUnitUseCases();
	}
}