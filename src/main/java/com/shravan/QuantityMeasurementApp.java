package com.shravan;

public class QuantityMeasurementApp {

	public static boolean demonstrateLengthEquality(Length length1, Length length2) {
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
	}
}