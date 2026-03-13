package com.shravan;

public final class QuantityMeasurementApp {

	private QuantityMeasurementApp() {
	}

	public static <U extends IMeasurable> boolean demonstrateEquality(Quantity<U> first, Quantity<U> second) {
		validateGenericQuantity(first, "First quantity cannot be null");
		validateGenericQuantity(second, "Second quantity cannot be null");
		return first.equals(second);
	}

	public static <U extends IMeasurable> boolean demonstrateComparison(double value1, U unit1, double value2,
			U unit2) {
		return demonstrateEquality(new Quantity<>(value1, unit1), new Quantity<>(value2, unit2));
	}

	public static <U extends IMeasurable> String demonstrateGenericHandling(Quantity<U> first, Quantity<U> second) {
		validateGenericQuantity(first, "First quantity cannot be null");
		validateGenericQuantity(second, "Second quantity cannot be null");
		return "Unified handling";
	}

	public static <U extends IMeasurable> Quantity<U> demonstrateConversion(double value, U fromUnit, U toUnit) {
		return new Quantity<>(value, fromUnit).convertTo(toUnit);
	}

	public static <U extends IMeasurable> Quantity<U> demonstrateConversion(Quantity<U> source, U toUnit) {
		validateGenericQuantity(source, "Source quantity cannot be null");
		return source.convertTo(toUnit);
	}

	public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> first, Quantity<U> second) {
		validateGenericQuantity(first, "First quantity cannot be null");
		validateGenericQuantity(second, "Second quantity cannot be null");
		return first.add(second);
	}

	public static <U extends IMeasurable> Quantity<U> demonstrateAddition(double value1, U unit1, double value2,
			U unit2) {
		return demonstrateAddition(new Quantity<>(value1, unit1), new Quantity<>(value2, unit2));
	}

	public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> first, Quantity<U> second,
			U targetUnit) {
		validateGenericQuantity(first, "First quantity cannot be null");
		validateGenericQuantity(second, "Second quantity cannot be null");
		return first.add(second, targetUnit);
	}

	public static <U extends IMeasurable> Quantity<U> demonstrateAddition(double value1, U unit1, double value2,
			U unit2, U targetUnit) {
		return demonstrateAddition(new Quantity<>(value1, unit1), new Quantity<>(value2, unit2), targetUnit);
	}

	public static <U extends IMeasurable> Quantity<U> demonstrateSubtraction(Quantity<U> first, Quantity<U> second) {
		validateGenericQuantity(first, "First quantity cannot be null");
		validateGenericQuantity(second, "Second quantity cannot be null");
		return first.subtract(second);
	}

	public static <U extends IMeasurable> Quantity<U> demonstrateSubtraction(double value1, U unit1, double value2,
			U unit2) {
		return demonstrateSubtraction(new Quantity<>(value1, unit1), new Quantity<>(value2, unit2));
	}

	public static <U extends IMeasurable> Quantity<U> demonstrateSubtraction(Quantity<U> first, Quantity<U> second,
			U targetUnit) {
		validateGenericQuantity(first, "First quantity cannot be null");
		validateGenericQuantity(second, "Second quantity cannot be null");
		return first.subtract(second, targetUnit);
	}

	public static <U extends IMeasurable> Quantity<U> demonstrateSubtraction(double value1, U unit1, double value2,
			U unit2, U targetUnit) {
		return demonstrateSubtraction(new Quantity<>(value1, unit1), new Quantity<>(value2, unit2), targetUnit);
	}

	public static <U extends IMeasurable> double demonstrateDivision(Quantity<U> first, Quantity<U> second) {
		validateGenericQuantity(first, "First quantity cannot be null");
		validateGenericQuantity(second, "Second quantity cannot be null");
		return first.divide(second);
	}

	public static <U extends IMeasurable> double demonstrateDivision(double value1, U unit1, double value2,
			U unit2) {
		return demonstrateDivision(new Quantity<>(value1, unit1), new Quantity<>(value2, unit2));
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
		System.out.println("Example Output of running the App");
		System.out.println();
		System.out.println("Addition Operations (Behavior Unchanged from UC12):");
		System.out.println();
		printExampleLine("new Quantity<>(1.0, FEET).add(new Quantity<>(12.0, INCHES))",
				new Quantity<>(1.0, LengthUnit.FEET).add(new Quantity<>(12.0, LengthUnit.INCHES)).toString());
		printExampleLine("new Quantity<>(10.0, KILOGRAM).add(new Quantity<>(5000.0, GRAM), GRAM)",
				new Quantity<>(10.0, WeightUnit.KILOGRAM).add(new Quantity<>(5000.0, WeightUnit.GRAM), WeightUnit.GRAM)
						.toString());
		System.out.println("Internal: Calls performBaseArithmetic(other, ADD) after validation");

		System.out.println();
		System.out.println("Subtraction Operations (Behavior Unchanged from UC12):");
		System.out.println();
		printExampleLine("new Quantity<>(10.0, FEET).subtract(new Quantity<>(6.0, INCHES))",
				new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(6.0, LengthUnit.INCHES)).toString());
		printExampleLine("new Quantity<>(5.0, LITRE).subtract(new Quantity<>(2.0, LITRE), MILLILITRE)",
				new Quantity<>(5.0, VolumeUnit.LITRE)
						.subtract(new Quantity<>(2.0, VolumeUnit.LITRE), VolumeUnit.MILLILITRE).toString());
		System.out.println("Internal: Calls performBaseArithmetic(other, SUBTRACT) after validation");

		System.out.println();
		System.out.println("Division Operations (Behavior Unchanged from UC12):");
		System.out.println();
		printExampleLine("new Quantity<>(10.0, FEET).divide(new Quantity<>(2.0, FEET))",
				String.valueOf(new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(2.0, LengthUnit.FEET))));
		printExampleLine("new Quantity<>(24.0, INCHES).divide(new Quantity<>(2.0, FEET))",
				String.valueOf(new Quantity<>(24.0, LengthUnit.INCHES).divide(new Quantity<>(2.0, LengthUnit.FEET))));
		System.out.println("Internal: Calls performBaseArithmetic(other, DIVIDE) and returns scalar");

		System.out.println();
		System.out.println("Error Cases (Consistent Across All Operations):");
		System.out.println();
		printExceptionExample("new Quantity<>(10.0, FEET).add(null)", new Operation() {
			@Override
			public Object execute() {
				return new Quantity<>(10.0, LengthUnit.FEET).add(null);
			}
		}, "");
		printExceptionExample("new Quantity<>(10.0, FEET).subtract(new Quantity<>(5.0, KILOGRAM))", new Operation() {
			@Override
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Object execute() {
				Quantity first = new Quantity<>(10.0, LengthUnit.FEET);
				Quantity second = new Quantity<>(5.0, WeightUnit.KILOGRAM);
				return first.subtract(second);
			}
		}, " (cross-category)");
		printExceptionExample("new Quantity<>(10.0, FEET).divide(new Quantity<>(0.0, FEET))", new Operation() {
			@Override
			public Object execute() {
				return new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(0.0, LengthUnit.FEET));
			}
		}, "");

		System.out.println();
		System.out.println("Internal Flow Example:");
		System.out.println();
		System.out.println("q1.subtract(q2, FEET)");
		System.out.println("  ↓");
		System.out.println("validateArithmeticOperands(q2, FEET, true)");
		System.out.println("  ↓");
		System.out.println("performBaseArithmetic(q2, SUBTRACT)");
		System.out.println("  ↓");
		System.out.println("SUBTRACT.compute(q1.baseValue, q2.baseValue)");
		System.out.println("  ↓");
		System.out.println("Convert result to FEET");
		System.out.println("  ↓");
		System.out.println("Return new Quantity<>(..., FEET)");

		System.out.println();
		System.out.println("Temperature Equality Comparisons:");
		System.out.println();
		printExampleLine("new Quantity<>(0.0, CELSIUS).equals(new Quantity<>(32.0, FAHRENHEIT))",
				String.valueOf(new Quantity<>(0.0, TemperatureUnit.CELSIUS)
						.equals(new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT))));
		printExampleLine("new Quantity<>(273.15, KELVIN).equals(new Quantity<>(0.0, CELSIUS))",
				String.valueOf(new Quantity<>(273.15, TemperatureUnit.KELVIN)
						.equals(new Quantity<>(0.0, TemperatureUnit.CELSIUS))));
		printExampleLine("new Quantity<>(212.0, FAHRENHEIT).equals(new Quantity<>(100.0, CELSIUS))",
				String.valueOf(new Quantity<>(212.0, TemperatureUnit.FAHRENHEIT)
						.equals(new Quantity<>(100.0, TemperatureUnit.CELSIUS))));
		printExampleLine("new Quantity<>(100.0, CELSIUS).equals(new Quantity<>(373.15, KELVIN))",
				String.valueOf(new Quantity<>(100.0, TemperatureUnit.CELSIUS)
						.equals(new Quantity<>(373.15, TemperatureUnit.KELVIN))));
		printExampleLine("new Quantity<>(50.0, CELSIUS).equals(new Quantity<>(122.0, FAHRENHEIT))",
				String.valueOf(new Quantity<>(50.0, TemperatureUnit.CELSIUS)
						.equals(new Quantity<>(122.0, TemperatureUnit.FAHRENHEIT))) + " (within epsilon)");

		System.out.println();
		System.out.println("Temperature Conversions:");
		System.out.println();
		printExampleLine("new Quantity<>(100.0, CELSIUS).convertTo(FAHRENHEIT)",
				new Quantity<>(100.0, TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.FAHRENHEIT).toString());
		printExampleLine("new Quantity<>(32.0, FAHRENHEIT).convertTo(CELSIUS)",
				new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT).convertTo(TemperatureUnit.CELSIUS).toString());
		printExampleLine("new Quantity<>(273.15, KELVIN).convertTo(CELSIUS)",
				new Quantity<>(273.15, TemperatureUnit.KELVIN).convertTo(TemperatureUnit.CELSIUS).toString());
		printExampleLine("new Quantity<>(0.0, CELSIUS).convertTo(KELVIN)",
				new Quantity<>(0.0, TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.KELVIN).toString());
		printExampleLine("new Quantity<>(-40.0, CELSIUS).convertTo(FAHRENHEIT)",
				new Quantity<>(-40.0, TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.FAHRENHEIT).toString()
						+ " (equal point)");

		System.out.println();
		System.out.println("Unsupported Operations (Error Handling):");
		System.out.println();
		printExceptionExample("new Quantity<>(100.0, CELSIUS).add(new Quantity<>(50.0, CELSIUS))", new Operation() {
			@Override
			public Object execute() {
				return new Quantity<>(100.0, TemperatureUnit.CELSIUS)
						.add(new Quantity<>(50.0, TemperatureUnit.CELSIUS));
			}
		}, "");
		printExceptionExample("new Quantity<>(100.0, CELSIUS).subtract(new Quantity<>(50.0, CELSIUS))", new Operation() {
			@Override
			public Object execute() {
				return new Quantity<>(100.0, TemperatureUnit.CELSIUS)
						.subtract(new Quantity<>(50.0, TemperatureUnit.CELSIUS));
			}
		}, "");
		printExceptionExample("new Quantity<>(100.0, CELSIUS).divide(new Quantity<>(50.0, CELSIUS))", new Operation() {
			@Override
			public Object execute() {
				return new Quantity<>(100.0, TemperatureUnit.CELSIUS)
						.divide(new Quantity<>(50.0, TemperatureUnit.CELSIUS));
			}
		}, "");

		System.out.println();
		System.out.println("Cross-Category Prevention:");
		System.out.println();
		printExampleLine("new Quantity<>(100.0, CELSIUS).equals(new Quantity<>(100.0, FEET))",
				crossCategoryEquals(new Quantity<>(100.0, TemperatureUnit.CELSIUS),
						new Quantity<>(100.0, LengthUnit.FEET)) + " (different categories)");

		System.out.println();
		System.out.println("Temperature Comparisons with Other Categories:");
		System.out.println();
		printExampleLine("new Quantity<>(50.0, CELSIUS).equals(new Quantity<>(50.0, KILOGRAM))",
				String.valueOf(crossCategoryEquals(new Quantity<>(50.0, TemperatureUnit.CELSIUS),
						new Quantity<>(50.0, WeightUnit.KILOGRAM))));
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

	private static void validateGenericQuantity(Quantity<? extends IMeasurable> quantity, String message) {
		if (quantity == null) {
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

	private static String formatQuantity(QuantityLength length) {
		return new Quantity<>(length.getValue(), length.getUnit()).toString();
	}

	private static void printExampleLine(String input, String output) {
		System.out.println("Input: " + input + " → Output: " + output);
	}

	private static void printExceptionExample(String input, Operation operation, String suffix) {
		try {
			operation.execute();
			System.out.println("Input: " + input + " → Output: No exception thrown");
		} catch (RuntimeException exception) {
			System.out.println("Input: " + input + " → Output: Throws " + exception.getClass().getSimpleName() + suffix
					+ " with message: " + exception.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static boolean crossCategoryEquals(Quantity first, Quantity second) {
		return first.equals(second);
	}

	private interface Operation {
		Object execute();
	}
}