package com.shravan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class QuantityMeasurementAppTest {

    private enum WeightUnit {
        GRAMS,
        KILOGRAMS
    }

    private static final double EPS = 1e-3;

    @Test
    public void testLengthUnitEnum_FeetConstant() {
        assertEquals(1.0, com.shravan.LengthUnit.FEET.getConversionFactor(), EPS);
    }

    @Test
    public void testLengthUnitEnum_InchesConstant() {
        assertEquals(1.0 / 12.0, com.shravan.LengthUnit.INCHES.getConversionFactor(), EPS);
    }

    @Test
    public void testLengthUnitEnum_YardsConstant() {
        assertEquals(3.0, com.shravan.LengthUnit.YARDS.getConversionFactor(), EPS);
    }

    @Test
    public void testLengthUnitEnum_CentimetersConstant() {
        assertEquals(1.0 / 30.48, com.shravan.LengthUnit.CENTIMETERS.getConversionFactor(), EPS);
    }

    @Test
    public void testConvertToBaseUnit_InchesToFeet() {
        assertEquals(1.0, com.shravan.LengthUnit.INCHES.convertToBaseUnit(12.0), EPS);
    }

    @Test
    public void testConvertToBaseUnit_YardsToFeet() {
        assertEquals(3.0, com.shravan.LengthUnit.YARDS.convertToBaseUnit(1.0), EPS);
    }

    @Test
    public void testConvertToBaseUnit_FeetToFeet() {
        assertEquals(5.0, com.shravan.LengthUnit.FEET.convertToBaseUnit(5.0), EPS);
    }

    @Test
    public void testConvertToBaseUnit_CentimetersToFeet() {
        assertEquals(1.0, com.shravan.LengthUnit.CENTIMETERS.convertToBaseUnit(30.48), EPS);
    }

    @Test
    public void testConvertFromBaseUnit_FeetToInches() {
        assertEquals(12.0, com.shravan.LengthUnit.INCHES.convertFromBaseUnit(1.0), EPS);
    }

    @Test
    public void testConvertFromBaseUnit_FeetToFeet() {
        assertEquals(2.0, com.shravan.LengthUnit.FEET.convertFromBaseUnit(2.0), EPS);
    }

    @Test
    public void testConvertFromBaseUnit_FeetToYards() {
        assertEquals(1.0, com.shravan.LengthUnit.YARDS.convertFromBaseUnit(3.0), EPS);
    }

    @Test
    public void testConvertFromBaseUnit_FeetToCentimeters() {
        assertEquals(30.48, com.shravan.LengthUnit.CENTIMETERS.convertFromBaseUnit(1.0), 1e-2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQuantityLengthRefactored_InvalidValue() {
        new Length(Double.NaN, LengthUnit.FEET);
    }

    @Test
    public void testQuantityLengthRefactored_ConvertTo() {
        Length converted = new Length(1.0, LengthUnit.FEET).convertTo(LengthUnit.INCHES);
        assertEquals(12.0, converted.getValue(), EPS);
        assertEquals(LengthUnit.INCHES, converted.getUnit());
    }

    @Test
    public void testQuantityLengthRefactored_Equality() {
        assertTrue(new Length(1.0, LengthUnit.FEET).equals(new Length(12.0, LengthUnit.INCHES)));
    }

    @Test
    public void testQuantityLengthRefactored_Add() {
        Length sum = new Length(1.0, LengthUnit.FEET).add(new Length(12.0, LengthUnit.INCHES), LengthUnit.FEET);
        assertEquals(2.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.FEET, sum.getUnit());
    }

    @Test
    public void testQuantityLengthRefactored_AddWithTargetUnit() {
        Length sum = new Length(1.0, LengthUnit.FEET).add(new Length(12.0, LengthUnit.INCHES), LengthUnit.YARDS);
        assertEquals(2.0 / 3.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.YARDS, sum.getUnit());
    }

    @Test(expected = NullPointerException.class)
    public void testQuantityLengthRefactored_NullUnit() {
        new Length(1.0, null);
    }

    @Test
    public void testRoundTripConversion_RefactoredDesign() {
        double originalValue = 30.48;
        double inFeet = LengthUnit.CENTIMETERS.convertToBaseUnit(originalValue);
        double roundTrippedValue = LengthUnit.CENTIMETERS.convertFromBaseUnit(inFeet);
        assertEquals(originalValue, roundTrippedValue, EPS);
    }

    @Test
    public void testFeetEquality() {
        Length feet1 = new Length(1.0, LengthUnit.FEET);
        Length feet2 = new Length(1.0, LengthUnit.FEET);
        assertTrue(feet1.equals(feet2));
    }

    @Test
    public void testInchesEquality() {
        Length inch1 = new Length(1.0, LengthUnit.INCHES);
        Length inch2 = new Length(1.0, LengthUnit.INCHES);
        assertTrue(inch1.equals(inch2));
    }

    @Test
    public void testFeetInchesComparison() {
        Length feet = new Length(1.0, LengthUnit.FEET);
        Length inches = new Length(12.0, LengthUnit.INCHES);
        assertTrue(feet.equals(inches));
    }

    @Test
    public void testFeetInequality() {
        Length feet1 = new Length(1.0, LengthUnit.FEET);
        Length feet2 = new Length(2.0, LengthUnit.FEET);
        assertFalse(feet1.equals(feet2));
    }

    @Test
    public void testInchesInequality() {
        Length inch1 = new Length(1.0, LengthUnit.INCHES);
        Length inch2 = new Length(2.0, LengthUnit.INCHES);
        assertFalse(inch1.equals(inch2));
    }

    @Test
    public void testCrossUnitInequality() {
        Length inch = new Length(1.0, LengthUnit.INCHES);
        Length feet = new Length(1.0, LengthUnit.FEET);
        assertFalse(inch.equals(feet));
    }

    @Test
    public void testMultipleFeetComparison() {
        Length feet1 = new Length(3.0, LengthUnit.FEET);
        Length inches = new Length(36.0, LengthUnit.INCHES);
        assertTrue(feet1.equals(inches));
    }

    @Test
    public void yardEquals36Inches() {
        Length yard = new Length(1.0, LengthUnit.YARDS);
        Length inches = new Length(36.0, LengthUnit.INCHES);
        assertTrue(yard.equals(inches));
    }

    @Test
    public void centimeterEquals39Point3701Inches() {
        Length centimeters = new Length(100.0, LengthUnit.CENTIMETERS);
        Length inches = new Length(39.3701, LengthUnit.INCHES);
        assertTrue(centimeters.equals(inches));
    }

    @Test
    public void threeFeetEqualsOneYard() {
        Length feet = new Length(3.0, LengthUnit.FEET);
        Length yard = new Length(1.0, LengthUnit.YARDS);
        assertTrue(feet.equals(yard));
    }

    @Test
    public void thirtyPoint48CmEqualOneFoot() {
        Length centimeters = new Length(30.48, LengthUnit.CENTIMETERS);
        Length feet = new Length(1.0, LengthUnit.FEET);
        assertTrue(centimeters.equals(feet));
    }

    @Test
    public void yardNotEqualTo1Inches() {
        Length yard = new Length(1.0, LengthUnit.YARDS);
        Length oneInch = new Length(1.0, LengthUnit.INCHES);
        assertFalse(yard.equals(oneInch));
    }

    @Test
    public void referenceEqualitySameObject() {
        Length yard = new Length(1.0, LengthUnit.YARDS);
        assertTrue(yard.equals(yard));
    }

    @Test
    public void equalsReturnsFalseForNull() {
        Length yard = new Length(1.0, LengthUnit.YARDS);
        assertFalse(yard.equals(null));
    }

    @Test
    public void reflexiveSymmetricAndTransitiveProperty() {
        Length a = new Length(1.0, LengthUnit.YARDS);
        Length b = new Length(3.0, LengthUnit.FEET);
        Length c = new Length(36.0, LengthUnit.INCHES);

        assertTrue(a.equals(a));
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        assertTrue(a.equals(b));
        assertTrue(b.equals(c));
        assertTrue(a.equals(c));
    }

    @Test
    public void differentValuesSameUnitNotEqual() {
        Length cm1 = new Length(1.0, LengthUnit.CENTIMETERS);
        Length cm2 = new Length(2.0, LengthUnit.CENTIMETERS);
        assertFalse(cm1.equals(cm2));
    }

    @Test
    public void crossUnitEqualityDemonstrateMethod() {
        assertTrue(QuantityMeasurementApp.demonstrateLengthComparison(1.0, LengthUnit.YARDS, 3.0, LengthUnit.FEET));
        assertTrue(QuantityMeasurementApp.demonstrateLengthComparison(1.0, LengthUnit.YARDS, 36.0, LengthUnit.INCHES));
        assertTrue(
                QuantityMeasurementApp.demonstrateLengthComparison(100.0, LengthUnit.CENTIMETERS, 39.3701,
                        LengthUnit.INCHES));
    }

    @Test
    public void testEquality_YardToYard_SameValue() {
        Length first = new Length(2.0, LengthUnit.YARDS);
        Length second = new Length(2.0, LengthUnit.YARDS);
        assertTrue(first.equals(second));
    }

    @Test
    public void testEquality_YardToYard_DifferentValue() {
        Length first = new Length(1.0, LengthUnit.YARDS);
        Length second = new Length(2.0, LengthUnit.YARDS);
        assertFalse(first.equals(second));
    }

    @Test
    public void testEquality_YardToFeet_EquivalentValue() {
        Length yard = new Length(1.0, LengthUnit.YARDS);
        Length feet = new Length(3.0, LengthUnit.FEET);
        assertTrue(yard.equals(feet));
    }

    @Test
    public void testEquality_FeetToYard_EquivalentValue() {
        Length feet = new Length(3.0, LengthUnit.FEET);
        Length yard = new Length(1.0, LengthUnit.YARDS);
        assertTrue(feet.equals(yard));
    }

    @Test
    public void testEquality_YardToInches_EquivalentValue() {
        Length yard = new Length(1.0, LengthUnit.YARDS);
        Length inches = new Length(36.0, LengthUnit.INCHES);
        assertTrue(yard.equals(inches));
    }

    @Test
    public void testEquality_InchesToYard_EquivalentValue() {
        Length inches = new Length(36.0, LengthUnit.INCHES);
        Length yard = new Length(1.0, LengthUnit.YARDS);
        assertTrue(inches.equals(yard));
    }

    @Test
    public void testEquality_YardToFeet_NonEquivalentValue() {
        Length yard = new Length(1.0, LengthUnit.YARDS);
        Length feet = new Length(2.0, LengthUnit.FEET);
        assertFalse(yard.equals(feet));
    }

    @Test
    public void testEquality_centimetersToInches_EquivalentValue() {
        Length centimeters = new Length(1.0, LengthUnit.CENTIMETERS);
        Length inches = new Length(0.393701, LengthUnit.INCHES);
        assertTrue(centimeters.equals(inches));
    }

    @Test
    public void testEquality_centimetersToFeet_NonEquivalentValue() {
        Length centimeters = new Length(1.0, LengthUnit.CENTIMETERS);
        Length feet = new Length(1.0, LengthUnit.FEET);
        assertFalse(centimeters.equals(feet));
    }

    @Test(expected = NullPointerException.class)
    public void testEquality_NullUnit() {
        new Length(1.0, null);
    }

    @Test
    public void testEquality_AllUnits_ComplexScenario() {
        Length yards = new Length(2.0, LengthUnit.YARDS);
        Length feet = new Length(6.0, LengthUnit.FEET);
        Length inches = new Length(72.0, LengthUnit.INCHES);
        Length centimeters = new Length(182.88, LengthUnit.CENTIMETERS);

        assertTrue(yards.equals(feet));
        assertTrue(feet.equals(inches));
        assertTrue(yards.equals(inches));
        assertTrue(inches.equals(centimeters));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEquality_InvalidUnit() {
        LengthUnit.valueOf("METER");
    }

    @Test
    public void testHashCode_EqualObjectsHaveSameHashCode() {
        Length a = new Length(1.0, LengthUnit.YARDS);
        Length b = new Length(3.0, LengthUnit.FEET);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void convertFeetToInches() {
        Length lengthInInches = QuantityMeasurementApp.demonstrateLengthConversion(3.0, LengthUnit.FEET,
                LengthUnit.INCHES);
        Length expectedLength = new Length(36.0, LengthUnit.INCHES);
        assertTrue(QuantityMeasurementApp.demonstrateLengthEquality(lengthInInches, expectedLength));
    }

    @Test
    public void convertYardsToInchesUsingOverloadedMethod() {
        Length lengthInYards = new Length(2.0, LengthUnit.YARDS);
        Length lengthInInches = QuantityMeasurementApp.demonstrateLengthConversion(lengthInYards, LengthUnit.INCHES);
        Length expectedLength = new Length(72.0, LengthUnit.INCHES);
        assertTrue(QuantityMeasurementApp.demonstrateLengthEquality(lengthInInches, expectedLength));
    }

    @Test
    public void testAddition_SameUnit_FeetPlusFeet() {
        Length a = new Length(1.0, LengthUnit.FEET);
        Length b = new Length(2.0, LengthUnit.FEET);

        Length sum = a.add(b);
        assertEquals(3.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.FEET, sum.getUnit());
    }

    @Test
    public void testAddition_SameUnit_InchPlusInch() {
        Length a = new Length(6.0, LengthUnit.INCHES);
        Length b = new Length(6.0, LengthUnit.INCHES);

        Length sum = a.add(b);
        assertEquals(12.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.INCHES, sum.getUnit());
    }

    @Test
    public void testAddition_CrossUnit_FeetPlusInches() {
        Length a = new Length(1.0, LengthUnit.FEET);
        Length b = new Length(12.0, LengthUnit.INCHES);

        Length sum = a.add(b);
        assertEquals(2.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.FEET, sum.getUnit());
    }

    @Test
    public void testAddition_CrossUnit_InchPlusFeet() {
        Length a = new Length(12.0, LengthUnit.INCHES);
        Length b = new Length(1.0, LengthUnit.FEET);

        Length sum = a.add(b);
        assertEquals(24.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.INCHES, sum.getUnit());
    }

    @Test
    public void testAddition_CrossUnit_YardPlusFeet() {
        Length a = new Length(1.0, LengthUnit.YARDS);
        Length b = new Length(3.0, LengthUnit.FEET);

        Length sum = a.add(b);
        assertEquals(2.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.YARDS, sum.getUnit());
    }

    @Test
    public void testAddition_CrossUnit_CentimeterPlusInch() {
        Length a = new Length(2.54, LengthUnit.CENTIMETERS);
        Length b = new Length(1.0, LengthUnit.INCHES);

        Length sum = a.add(b);
        assertEquals(5.08, sum.getValue(), 1e-2);
        assertEquals(LengthUnit.CENTIMETERS, sum.getUnit());
    }

    @Test
    public void testAddition_Commutativity() {
        Length a = new Length(1.0, LengthUnit.FEET);
        Length b = new Length(12.0, LengthUnit.INCHES);

        Length sum1 = a.add(b);
        Length sum2 = b.add(a).convertTo(LengthUnit.FEET);
        assertEquals(sum1.getValue(), sum2.getValue(), EPS);
    }

    @Test
    public void testAddition_WithZero() {
        Length a = new Length(5.0, LengthUnit.FEET);
        Length zero = new Length(0.0, LengthUnit.INCHES);

        Length sum = a.add(zero);
        assertEquals(5.0, sum.getValue(), EPS);
    }

    @Test
    public void testAddition_NegativeValues() {
        Length a = new Length(5.0, LengthUnit.FEET);
        Length b = new Length(-2.0, LengthUnit.FEET);

        Length sum = a.add(b);
        assertEquals(3.0, sum.getValue(), EPS);
    }

    @Test(expected = NullPointerException.class)
    public void testAddition_NullSecondOperand() {
        Length a = new Length(1.0, LengthUnit.FEET);
        a.add(null);
    }

    @Test
    public void testAddition_LargeValues() {
        Length a = new Length(1e6, LengthUnit.FEET);
        Length b = new Length(1e6, LengthUnit.FEET);

        Length sum = a.add(b);
        assertEquals(2e6, sum.getValue(), 1e2);
    }

    @Test
    public void testAddition_SmallValues() {
        Length a = new Length(0.001, LengthUnit.FEET);
        Length b = new Length(0.002, LengthUnit.FEET);

        Length sum = a.add(b);
        assertEquals(0.003, sum.getValue(), 1e-6);
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Feet() {
        Length a = new Length(1.0, LengthUnit.FEET);
        Length b = new Length(12.0, LengthUnit.INCHES);
        Length sum = a.add(b, LengthUnit.FEET);
        assertEquals(2.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.FEET, sum.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Inches() {
        Length a = new Length(1.0, LengthUnit.FEET);
        Length b = new Length(12.0, LengthUnit.INCHES);
        Length sum = a.add(b, LengthUnit.INCHES);
        assertEquals(24.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.INCHES, sum.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Yards() {
        Length a = new Length(1.0, LengthUnit.FEET);
        Length b = new Length(12.0, LengthUnit.INCHES);
        Length sum = a.add(b, LengthUnit.YARDS);
        assertEquals(0.667, sum.getValue(), EPS);
        assertEquals(LengthUnit.YARDS, sum.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Centimeters() {
        Length a = new Length(1.0, LengthUnit.INCHES);
        Length b = new Length(1.0, LengthUnit.INCHES);
        Length sum = a.add(b, LengthUnit.CENTIMETERS);
        assertEquals(5.08, sum.getValue(), 1e-2);
        assertEquals(LengthUnit.CENTIMETERS, sum.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_SameAsFirstOperand() {
        Length a = new Length(2.0, LengthUnit.YARDS);
        Length b = new Length(3.0, LengthUnit.FEET);
        Length sum = a.add(b, LengthUnit.YARDS);
        assertEquals(3.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.YARDS, sum.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_SameAsSecondOperand() {
        Length a = new Length(2.0, LengthUnit.YARDS);
        Length b = new Length(3.0, LengthUnit.FEET);
        Length sum = a.add(b, LengthUnit.FEET);
        assertEquals(9.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.FEET, sum.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Commutativity() {
        Length a = new Length(1.0, LengthUnit.FEET);
        Length b = new Length(12.0, LengthUnit.INCHES);
        Length sum1 = a.add(b, LengthUnit.YARDS);
        Length sum2 = b.add(a, LengthUnit.YARDS);
        assertEquals(sum1.getValue(), sum2.getValue(), EPS);
        assertEquals(sum1.getUnit(), sum2.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_WithZero() {
        Length a = new Length(5.0, LengthUnit.FEET);
        Length b = new Length(0.0, LengthUnit.INCHES);
        Length sum = a.add(b, LengthUnit.YARDS);
        assertEquals(1.667, sum.getValue(), EPS);
        assertEquals(LengthUnit.YARDS, sum.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_NegativeValues() {
        Length a = new Length(5.0, LengthUnit.FEET);
        Length b = new Length(-2.0, LengthUnit.FEET);
        Length sum = a.add(b, LengthUnit.INCHES);
        assertEquals(36.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.INCHES, sum.getUnit());
    }

    @Test(expected = NullPointerException.class)
    public void testAddition_ExplicitTargetUnit_NullTargetUnit() {
        Length a = new Length(1.0, LengthUnit.FEET);
        Length b = new Length(12.0, LengthUnit.INCHES);
        a.add(b, null);
    }

    @Test
    public void testAddition_ExplicitTargetUnit_LargeToSmallScale() {
        Length a = new Length(1000.0, LengthUnit.FEET);
        Length b = new Length(500.0, LengthUnit.FEET);
        Length sum = a.add(b, LengthUnit.INCHES);
        assertEquals(18000.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.INCHES, sum.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_SmallToLargeScale() {
        Length a = new Length(12.0, LengthUnit.INCHES);
        Length b = new Length(12.0, LengthUnit.INCHES);
        Length sum = a.add(b, LengthUnit.YARDS);
        assertEquals(0.667, sum.getValue(), EPS);
        assertEquals(LengthUnit.YARDS, sum.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_AllUnitCombinations() {
        Length[] operands1 = {
                new Length(1.0, LengthUnit.FEET),
                new Length(12.0, LengthUnit.INCHES),
                new Length(1.0, LengthUnit.YARDS),
                new Length(30.48, LengthUnit.CENTIMETERS)
        };
        Length[] operands2 = {
                new Length(2.0, LengthUnit.FEET),
                new Length(24.0, LengthUnit.INCHES),
                new Length(2.0, LengthUnit.YARDS),
                new Length(60.96, LengthUnit.CENTIMETERS)
        };
        LengthUnit[] targetUnits = LengthUnit.values();

        for (Length op1 : operands1) {
            for (Length op2 : operands2) {
                for (LengthUnit target : targetUnits) {
                    Length sum = op1.add(op2, target);
                    assertEquals(target, sum.getUnit());

                    double baseSum = op1.getValue() * op1.getUnit().getConversionFactor() +
                            op2.getValue() * op2.getUnit().getConversionFactor();
                    double expectedTargetValue = baseSum / target.getConversionFactor();
                    assertEquals(expectedTargetValue, sum.getValue(), 1e-2);
                }
            }
        }
    }

    @Test
    public void testAddition_ExplicitTargetUnit_PrecisionTolerance() {
        Length a = new Length(1.0, LengthUnit.FEET);
        Length b = new Length(12.0, LengthUnit.INCHES);
        Length sum1 = a.add(b, LengthUnit.YARDS);

        Length c = new Length(36.0, LengthUnit.INCHES);
        Length d = new Length(36.0, LengthUnit.INCHES);
        Length sum2 = c.add(d, LengthUnit.YARDS);

        assertEquals(sum1.getValue() * 3, sum2.getValue(), EPS);
    }

    @Test
    public void testBackwardCompatibility_UC1EqualityTests() {
        assertTrue(new Length(1.0, LengthUnit.FEET).equals(new Length(1.0, LengthUnit.FEET)));
        assertTrue(new Length(12.0, LengthUnit.INCHES).equals(new Length(1.0, LengthUnit.FEET)));
        assertTrue(new Length(1.0, LengthUnit.YARDS).equals(new Length(36.0, LengthUnit.INCHES)));
        assertFalse(new Length(1.0, LengthUnit.FEET).equals(new Length(2.0, LengthUnit.FEET)));
    }

    @Test
    public void testBackwardCompatibility_UC5ConversionTests() {
        Length feetToInches = QuantityMeasurementApp.demonstrateLengthConversion(3.0, LengthUnit.FEET,
                LengthUnit.INCHES);
        Length yardsToInches = QuantityMeasurementApp.demonstrateLengthConversion(2.0, LengthUnit.YARDS,
                LengthUnit.INCHES);
        Length centimetersToFeet = QuantityMeasurementApp.demonstrateLengthConversion(30.48, LengthUnit.CENTIMETERS,
                LengthUnit.FEET);

        assertEquals(36.0, feetToInches.getValue(), EPS);
        assertEquals(72.0, yardsToInches.getValue(), EPS);
        assertEquals(1.0, centimetersToFeet.getValue(), EPS);
    }

    @Test
    public void testBackwardCompatibility_UC6AdditionTests() {
        Length sameUnitSum = new Length(1.0, LengthUnit.FEET).add(new Length(2.0, LengthUnit.FEET));
        Length crossUnitSum = new Length(1.0, LengthUnit.FEET).add(new Length(12.0, LengthUnit.INCHES));
        Length centimeterInchSum = new Length(2.54, LengthUnit.CENTIMETERS).add(new Length(1.0, LengthUnit.INCHES));

        assertEquals(3.0, sameUnitSum.getValue(), EPS);
        assertEquals(2.0, crossUnitSum.getValue(), EPS);
        assertEquals(5.08, centimeterInchSum.getValue(), 1e-2);
    }

    @Test
    public void testBackwardCompatibility_UC7AdditionWithTargetUnitTests() {
        Length sumInFeet = new Length(1.0, LengthUnit.FEET).add(new Length(12.0, LengthUnit.INCHES),
                LengthUnit.FEET);
        Length sumInInches = new Length(1.0, LengthUnit.FEET).add(new Length(12.0, LengthUnit.INCHES),
                LengthUnit.INCHES);
        Length sumInYards = new Length(1.0, LengthUnit.YARDS).add(new Length(3.0, LengthUnit.FEET),
                LengthUnit.YARDS);

        assertEquals(2.0, sumInFeet.getValue(), EPS);
        assertEquals(24.0, sumInInches.getValue(), EPS);
        assertEquals(2.0, sumInYards.getValue(), EPS);
    }

    @Test
    public void testArchitecturalScalability_MultipleCategories() {
        assertTrue(LengthUnit.class.isEnum());
        assertTrue(WeightUnit.class.isEnum());
        assertTrue(LengthUnit.class.getEnclosingClass() == null);
        assertFalse(LengthUnit.class.equals(WeightUnit.class));
        assertEquals("KILOGRAMS", WeightUnit.KILOGRAMS.name());
    }

    @Test
    public void testUnitImmutability() {
        LengthUnit[] units = LengthUnit.values();
        units[0] = null;

        assertTrue(LengthUnit.FEET == LengthUnit.valueOf("FEET"));
        assertEquals(4, LengthUnit.values().length);
        assertEquals(LengthUnit.FEET, LengthUnit.values()[0]);
    }

}