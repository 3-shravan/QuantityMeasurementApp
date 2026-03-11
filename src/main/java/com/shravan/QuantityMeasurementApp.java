package com.shravan;

import java.text.DecimalFormat;

public final class QuantityMeasurementApp {

	private static final DecimalFormat VALUE_FORMAT = new DecimalFormat("0.0###");

	private QuantityMeasurementApp() {
	}

	public static boolean demonstrateLengthEquality(Length length1, Length length2) {
		validateLength(length1, "First length cannot be null");
		validateLength(length2, "Second length cannot be null");
		return length1.equals(length2);
	}

	public static boolean demonstrateLengthComparison(double value1, LengthUnit unit1, double value2,
			LengthUnit unit2) {
		return demonstrateLengthEquality(createLength(value1, unit1), createLength(value2, unit2));
	}

	public static Length demonstrateLengthConversion(double value, LengthUnit fromUnit, LengthUnit toUnit) {
		return createLength(value, fromUnit).convertTo(toUnit);
	}

	public static Length demonstrateLengthConversion(Length sourceLength, LengthUnit toUnit) {
		validateLength(sourceLength, "Source length cannot be null");
		return sourceLength.convertTo(toUnit);
	}

	public static Length demonstrateLengthAddition(Length length1, Length length2) {
		validateLength(length1, "First length cannot be null");
		validateLength(length2, "Second length cannot be null");
		return length1.add(length2);
	}

	public static Length demonstrateLengthAddition(double value1, LengthUnit unit1, double value2,
			LengthUnit unit2) {
		return demonstrateLengthAddition(createLength(value1, unit1), createLength(value2, unit2));
	}

	public static Length demonstrateLengthAddition(Length length1, Length length2, LengthUnit targetUnit) {
		validateLength(length1, "First length cannot be null");
		validateLength(length2, "Second length cannot be null");
		return length1.add(length2, targetUnit);
	}

	public static Length demonstrateLengthAddition(double value1, LengthUnit unit1, double value2,
			LengthUnit unit2, LengthUnit targetUnit) {
		return demonstrateLengthAddition(createLength(value1, unit1), createLength(value2, unit2), targetUnit);
	}

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
		printLengthUnitConversions();
		printFromBaseUnitConversions();
		printRefactoredLengthOperations();
	}

	private static Length createLength(double value, LengthUnit unit) {
		return new Length(value, unit);
	}

	private static void validateLength(Length length, String message) {
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
		Length converted = demonstrateLengthConversion(value, fromUnit, toUnit);
		System.out.println(title + " -> Quantity(" + value + ", " + fromUnit + ") to " + toUnit + " = "
				+ converted);
	}

	private static void printAdditionDemo(double value1, LengthUnit unit1, double value2, LengthUnit unit2) {
		Length result = demonstrateLengthAddition(value1, unit1, value2, unit2);
		System.out.println("Input: add(Quantity(" + value1 + ", " + unit1 + "), Quantity(" + value2 + ", " + unit2
				+ "))");
		System.out.println("Output: " + formatQuantity(result));
	}

	private static void printAdditionDemo(double value1, LengthUnit unit1, double value2, LengthUnit unit2,
			LengthUnit targetUnit) {
		Length result = demonstrateLengthAddition(value1, unit1, value2, unit2, targetUnit);
		System.out.println("Input: add(Quantity(" + value1 + ", " + unit1 + "), Quantity(" + value2 + ", " + unit2
				+ "), " + targetUnit + ")");
		System.out.println("Output: " + formatQuantity(result));
	}

	private static String formatValue(double value) {
		return VALUE_FORMAT.format(value);
	}

	private static String formatQuantity(Length length) {
		return "Quantity(" + formatValue(length.getValue()) + ", " + length.getUnit() + ")";
	}

	private static void printSection(String title) {
		System.out.println();
		System.out.println("--- " + title + " ---");
	}

	private static void printBaseUnitConversion(String unitName, double inputValue, LengthUnit unit,
			String resultUnitName) {
		System.out.printf("LengthUnit.%s.convertToBaseUnit(%.2f) = %.2f %s%n", unitName, inputValue,
				unit.convertToBaseUnit(inputValue), resultUnitName);
	}

	private static void printFromBaseUnitConversion(String unitName, double inputValue, LengthUnit unit,
			String resultUnitName) {
		System.out.printf("LengthUnit.%s.convertFromBaseUnit(%.2f) = %.2f %s%n", unitName, inputValue,
				unit.convertFromBaseUnit(inputValue), resultUnitName);
	}

	private static void printQuantityOperation(String description, Object result) {
		System.out.printf("%s = %s%n", description, result);
	}

	private static void printLengthUnitConversions() {
		printSection("LengthUnit Conversion Methods");
		printBaseUnitConversion("FEET", 12.0, LengthUnit.FEET, "feet");
		printBaseUnitConversion("INCHES", 12.0, LengthUnit.INCHES, "feet");
		printBaseUnitConversion("YARDS", 1.0, LengthUnit.YARDS, "feet");
		printBaseUnitConversion("CENTIMETERS", 30.48, LengthUnit.CENTIMETERS, "feet");
	}

	private static void printFromBaseUnitConversions() {
		printSection("From Base Unit Conversions");
		printFromBaseUnitConversion("FEET", 2.0, LengthUnit.FEET, "feet");
		printFromBaseUnitConversion("INCHES", 1.0, LengthUnit.INCHES, "inches");
		printFromBaseUnitConversion("YARDS", 3.0, LengthUnit.YARDS, "yards");
		printFromBaseUnitConversion("CENTIMETERS", 1.0, LengthUnit.CENTIMETERS, "centimeters");
	}

	private static void printRefactoredLengthOperations() {
		printSection("Refactored Length Operations");

		Length feet = createLength(1.0, LengthUnit.FEET);
		Length inches = createLength(12.0, LengthUnit.INCHES);

		printQuantityOperation("Quantity(1.0, FEET).equals(Quantity(12.0, INCHES))", feet.equals(inches));
		printQuantityOperation("Quantity(1.0, FEET).convertTo(INCHES)", formatQuantity(feet.convertTo(LengthUnit.INCHES)));
		printQuantityOperation("Quantity(1.0, FEET).add(Quantity(12.0, INCHES), FEET)",
				formatQuantity(feet.add(inches, LengthUnit.FEET)));
		printQuantityOperation("Quantity(1.0, FEET).add(Quantity(12.0, INCHES), YARDS)",
				formatQuantity(feet.add(inches, LengthUnit.YARDS)));
	}

}