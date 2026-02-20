package com.shravan;

public class QuantityMeasurementApp {

	public boolean demonstrateLengthEquality(Length firstLength, Length secondLength) {
		return firstLength.equals(secondLength);
	}

	public boolean areFeetEqual(double firstValueInFeet, double secondValueInFeet) {
		Length firstFeet = new Length(firstValueInFeet, Length.LengthUnit.FEET);
		Length secondFeet = new Length(secondValueInFeet, Length.LengthUnit.FEET);
		return demonstrateLengthEquality(firstFeet, secondFeet);
	}

	public boolean areInchesEqual(double firstValueInInches, double secondValueInInches) {
		Length firstInches = new Length(firstValueInInches, Length.LengthUnit.INCHES);
		Length secondInches = new Length(secondValueInInches, Length.LengthUnit.INCHES);
		return demonstrateLengthEquality(firstInches, secondInches);
	}

	public boolean areLengthsEqual(double firstValue, Length.LengthUnit firstUnit, double secondValue,
			Length.LengthUnit secondUnit) {
		Length firstLength = new Length(firstValue, firstUnit);
		Length secondLength = new Length(secondValue, secondUnit);
		return demonstrateLengthEquality(firstLength, secondLength);
	}

	public static void demonstrateFeetEquality() {
		QuantityMeasurementApp app = new QuantityMeasurementApp();
		double firstFeet = 1.0;
		double secondFeet = 1.0;

		boolean result = app.areFeetEqual(firstFeet, secondFeet);
		System.out.println("Input: " + firstFeet + " ft and " + secondFeet + " ft");
		System.out.println("Output: Equal (" + result + ")");
	}

	public static void demonstrateInchesEquality() {
		QuantityMeasurementApp app = new QuantityMeasurementApp();
		double firstInches = 1.0;
		double secondInches = 1.0;

		boolean result = app.areInchesEqual(firstInches, secondInches);
		System.out.println("Input: " + firstInches + " inch and " + secondInches + " inch");
		System.out.println("Output: Equal (" + result + ")");
	}

	public static void demonstrateFeetInchesComparison() {
		QuantityMeasurementApp app = new QuantityMeasurementApp();
		double firstFeet = 1.0;
		double secondInches = 12.0;

		boolean result = app.areLengthsEqual(firstFeet, Length.LengthUnit.FEET, secondInches, Length.LengthUnit.INCHES);
		System.out.println("Input: Quantity(" + firstFeet + ", feet) and Quantity(" + secondInches + ", inches)");
		System.out.println("Output: Equal (" + result + ")");
	}

	public static void main(String[] args) {
		demonstrateFeetEquality();
		demonstrateInchesEquality();
		demonstrateFeetInchesComparison();
	}
}