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
        new QuantityLength(Double.NaN, LengthUnit.FEET);
    }

    @Test
    public void testQuantityLengthRefactored_ConvertTo() {
        QuantityLength converted = new QuantityLength(1.0, LengthUnit.FEET).convertTo(LengthUnit.INCHES);
        assertEquals(12.0, converted.getValue(), EPS);
        assertEquals(LengthUnit.INCHES, converted.getUnit());
    }

    @Test
    public void testQuantityLengthRefactored_Equality() {
        assertTrue(new QuantityLength(1.0, LengthUnit.FEET).equals(new QuantityLength(12.0, LengthUnit.INCHES)));
    }

    @Test
    public void testQuantityLengthRefactored_Add() {
        QuantityLength sum = new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCHES), LengthUnit.FEET);
        assertEquals(2.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.FEET, sum.getUnit());
    }

    @Test
    public void testQuantityLengthRefactored_AddWithTargetUnit() {
        QuantityLength sum = new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCHES), LengthUnit.YARDS);
        assertEquals(2.0 / 3.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.YARDS, sum.getUnit());
    }

    @Test(expected = NullPointerException.class)
    public void testQuantityLengthRefactored_NullUnit() {
        new QuantityLength(1.0, null);
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
        QuantityLength feet1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength feet2 = new QuantityLength(1.0, LengthUnit.FEET);
        assertTrue(feet1.equals(feet2));
    }

    @Test
    public void testInchesEquality() {
        QuantityLength inch1 = new QuantityLength(1.0, LengthUnit.INCHES);
        QuantityLength inch2 = new QuantityLength(1.0, LengthUnit.INCHES);
        assertTrue(inch1.equals(inch2));
    }

    @Test
    public void testFeetInchesComparison() {
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength inches = new QuantityLength(12.0, LengthUnit.INCHES);
        assertTrue(feet.equals(inches));
    }

    @Test
    public void testFeetInequality() {
        QuantityLength feet1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength feet2 = new QuantityLength(2.0, LengthUnit.FEET);
        assertFalse(feet1.equals(feet2));
    }

    @Test
    public void testInchesInequality() {
        QuantityLength inch1 = new QuantityLength(1.0, LengthUnit.INCHES);
        QuantityLength inch2 = new QuantityLength(2.0, LengthUnit.INCHES);
        assertFalse(inch1.equals(inch2));
    }

    @Test
    public void testCrossUnitInequality() {
        QuantityLength inch = new QuantityLength(1.0, LengthUnit.INCHES);
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        assertFalse(inch.equals(feet));
    }

    @Test
    public void testMultipleFeetComparison() {
        QuantityLength feet1 = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength inches = new QuantityLength(36.0, LengthUnit.INCHES);
        assertTrue(feet1.equals(inches));
    }

    @Test
    public void yardEquals36Inches() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength inches = new QuantityLength(36.0, LengthUnit.INCHES);
        assertTrue(yard.equals(inches));
    }

    @Test
    public void centimeterEquals39Point3701Inches() {
        QuantityLength centimeters = new QuantityLength(100.0, LengthUnit.CENTIMETERS);
        QuantityLength inches = new QuantityLength(39.3701, LengthUnit.INCHES);
        assertTrue(centimeters.equals(inches));
    }

    @Test
    public void threeFeetEqualsOneYard() {
        QuantityLength feet = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        assertTrue(feet.equals(yard));
    }

    @Test
    public void thirtyPoint48CmEqualOneFoot() {
        QuantityLength centimeters = new QuantityLength(30.48, LengthUnit.CENTIMETERS);
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        assertTrue(centimeters.equals(feet));
    }

    @Test
    public void yardNotEqualTo1Inches() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength oneInch = new QuantityLength(1.0, LengthUnit.INCHES);
        assertFalse(yard.equals(oneInch));
    }

    @Test
    public void referenceEqualitySameObject() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        assertTrue(yard.equals(yard));
    }

    @Test
    public void equalsReturnsFalseForNull() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        assertFalse(yard.equals(null));
    }

    @Test
    public void reflexiveSymmetricAndTransitiveProperty() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength b = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength c = new QuantityLength(36.0, LengthUnit.INCHES);

        assertTrue(a.equals(a));
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        assertTrue(a.equals(b));
        assertTrue(b.equals(c));
        assertTrue(a.equals(c));
    }

    @Test
    public void differentValuesSameUnitNotEqual() {
        QuantityLength cm1 = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
        QuantityLength cm2 = new QuantityLength(2.0, LengthUnit.CENTIMETERS);
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
        QuantityLength first = new QuantityLength(2.0, LengthUnit.YARDS);
        QuantityLength second = new QuantityLength(2.0, LengthUnit.YARDS);
        assertTrue(first.equals(second));
    }

    @Test
    public void testEquality_YardToYard_DifferentValue() {
        QuantityLength first = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength second = new QuantityLength(2.0, LengthUnit.YARDS);
        assertFalse(first.equals(second));
    }

    @Test
    public void testEquality_YardToFeet_EquivalentValue() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength feet = new QuantityLength(3.0, LengthUnit.FEET);
        assertTrue(yard.equals(feet));
    }

    @Test
    public void testEquality_FeetToYard_EquivalentValue() {
        QuantityLength feet = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        assertTrue(feet.equals(yard));
    }

    @Test
    public void testEquality_YardToInches_EquivalentValue() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength inches = new QuantityLength(36.0, LengthUnit.INCHES);
        assertTrue(yard.equals(inches));
    }

    @Test
    public void testEquality_InchesToYard_EquivalentValue() {
        QuantityLength inches = new QuantityLength(36.0, LengthUnit.INCHES);
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        assertTrue(inches.equals(yard));
    }

    @Test
    public void testEquality_YardToFeet_NonEquivalentValue() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength feet = new QuantityLength(2.0, LengthUnit.FEET);
        assertFalse(yard.equals(feet));
    }

    @Test
    public void testEquality_centimetersToInches_EquivalentValue() {
        QuantityLength centimeters = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
        QuantityLength inches = new QuantityLength(0.393701, LengthUnit.INCHES);
        assertTrue(centimeters.equals(inches));
    }

    @Test
    public void testEquality_centimetersToFeet_NonEquivalentValue() {
        QuantityLength centimeters = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        assertFalse(centimeters.equals(feet));
    }

    @Test(expected = NullPointerException.class)
    public void testEquality_NullUnit() {
        new QuantityLength(1.0, null);
    }

    @Test
    public void testEquality_AllUnits_ComplexScenario() {
        QuantityLength yards = new QuantityLength(2.0, LengthUnit.YARDS);
        QuantityLength feet = new QuantityLength(6.0, LengthUnit.FEET);
        QuantityLength inches = new QuantityLength(72.0, LengthUnit.INCHES);
        QuantityLength centimeters = new QuantityLength(182.88, LengthUnit.CENTIMETERS);

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
        QuantityLength a = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength b = new QuantityLength(3.0, LengthUnit.FEET);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void convertFeetToInches() {
        QuantityLength lengthInInches = QuantityMeasurementApp.demonstrateLengthConversion(3.0, LengthUnit.FEET,
                LengthUnit.INCHES);
        QuantityLength expectedLength = new QuantityLength(36.0, LengthUnit.INCHES);
        assertTrue(QuantityMeasurementApp.demonstrateLengthEquality(lengthInInches, expectedLength));
    }

    @Test
    public void convertYardsToInchesUsingOverloadedMethod() {
        QuantityLength lengthInYards = new QuantityLength(2.0, LengthUnit.YARDS);
        QuantityLength lengthInInches = QuantityMeasurementApp.demonstrateLengthConversion(lengthInYards, LengthUnit.INCHES);
        QuantityLength expectedLength = new QuantityLength(72.0, LengthUnit.INCHES);
        assertTrue(QuantityMeasurementApp.demonstrateLengthEquality(lengthInInches, expectedLength));
    }

    @Test
    public void testAddition_SameUnit_FeetPlusFeet() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(2.0, LengthUnit.FEET);

        QuantityLength sum = a.add(b);
        assertEquals(3.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.FEET, sum.getUnit());
    }

    @Test
    public void testAddition_SameUnit_InchPlusInch() {
        QuantityLength a = new QuantityLength(6.0, LengthUnit.INCHES);
        QuantityLength b = new QuantityLength(6.0, LengthUnit.INCHES);

        QuantityLength sum = a.add(b);
        assertEquals(12.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.INCHES, sum.getUnit());
    }

    @Test
    public void testAddition_CrossUnit_FeetPlusInches() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength sum = a.add(b);
        assertEquals(2.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.FEET, sum.getUnit());
    }

    @Test
    public void testAddition_CrossUnit_InchPlusFeet() {
        QuantityLength a = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength sum = a.add(b);
        assertEquals(24.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.INCHES, sum.getUnit());
    }

    @Test
    public void testAddition_CrossUnit_YardPlusFeet() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength b = new QuantityLength(3.0, LengthUnit.FEET);

        QuantityLength sum = a.add(b);
        assertEquals(2.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.YARDS, sum.getUnit());
    }

    @Test
    public void testAddition_CrossUnit_CentimeterPlusInch() {
        QuantityLength a = new QuantityLength(2.54, LengthUnit.CENTIMETERS);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.INCHES);

        QuantityLength sum = a.add(b);
        assertEquals(5.08, sum.getValue(), 1e-2);
        assertEquals(LengthUnit.CENTIMETERS, sum.getUnit());
    }

    @Test
    public void testAddition_Commutativity() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength sum1 = a.add(b);
        QuantityLength sum2 = b.add(a).convertTo(LengthUnit.FEET);
        assertEquals(sum1.getValue(), sum2.getValue(), EPS);
    }

    @Test
    public void testAddition_WithZero() {
        QuantityLength a = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength zero = new QuantityLength(0.0, LengthUnit.INCHES);

        QuantityLength sum = a.add(zero);
        assertEquals(5.0, sum.getValue(), EPS);
    }

    @Test
    public void testAddition_NegativeValues() {
        QuantityLength a = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(-2.0, LengthUnit.FEET);

        QuantityLength sum = a.add(b);
        assertEquals(3.0, sum.getValue(), EPS);
    }

    @Test(expected = NullPointerException.class)
    public void testAddition_NullSecondOperand() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        a.add(null);
    }

    @Test
    public void testAddition_LargeValues() {
        QuantityLength a = new QuantityLength(1e6, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(1e6, LengthUnit.FEET);

        QuantityLength sum = a.add(b);
        assertEquals(2e6, sum.getValue(), 1e2);
    }

    @Test
    public void testAddition_SmallValues() {
        QuantityLength a = new QuantityLength(0.001, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(0.002, LengthUnit.FEET);

        QuantityLength sum = a.add(b);
        assertEquals(0.003, sum.getValue(), 1e-6);
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Feet() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength sum = a.add(b, LengthUnit.FEET);
        assertEquals(2.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.FEET, sum.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Inches() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength sum = a.add(b, LengthUnit.INCHES);
        assertEquals(24.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.INCHES, sum.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Yards() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength sum = a.add(b, LengthUnit.YARDS);
        assertEquals(0.667, sum.getValue(), EPS);
        assertEquals(LengthUnit.YARDS, sum.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Centimeters() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.INCHES);
        QuantityLength b = new QuantityLength(1.0, LengthUnit.INCHES);
        QuantityLength sum = a.add(b, LengthUnit.CENTIMETERS);
        assertEquals(5.08, sum.getValue(), 1e-2);
        assertEquals(LengthUnit.CENTIMETERS, sum.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_SameAsFirstOperand() {
        QuantityLength a = new QuantityLength(2.0, LengthUnit.YARDS);
        QuantityLength b = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength sum = a.add(b, LengthUnit.YARDS);
        assertEquals(3.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.YARDS, sum.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_SameAsSecondOperand() {
        QuantityLength a = new QuantityLength(2.0, LengthUnit.YARDS);
        QuantityLength b = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength sum = a.add(b, LengthUnit.FEET);
        assertEquals(9.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.FEET, sum.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Commutativity() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength sum1 = a.add(b, LengthUnit.YARDS);
        QuantityLength sum2 = b.add(a, LengthUnit.YARDS);
        assertEquals(sum1.getValue(), sum2.getValue(), EPS);
        assertEquals(sum1.getUnit(), sum2.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_WithZero() {
        QuantityLength a = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(0.0, LengthUnit.INCHES);
        QuantityLength sum = a.add(b, LengthUnit.YARDS);
        assertEquals(1.667, sum.getValue(), EPS);
        assertEquals(LengthUnit.YARDS, sum.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_NegativeValues() {
        QuantityLength a = new QuantityLength(5.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(-2.0, LengthUnit.FEET);
        QuantityLength sum = a.add(b, LengthUnit.INCHES);
        assertEquals(36.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.INCHES, sum.getUnit());
    }

    @Test(expected = NullPointerException.class)
    public void testAddition_ExplicitTargetUnit_NullTargetUnit() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        a.add(b, null);
    }

    @Test
    public void testAddition_ExplicitTargetUnit_LargeToSmallScale() {
        QuantityLength a = new QuantityLength(1000.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(500.0, LengthUnit.FEET);
        QuantityLength sum = a.add(b, LengthUnit.INCHES);
        assertEquals(18000.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.INCHES, sum.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_SmallToLargeScale() {
        QuantityLength a = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength sum = a.add(b, LengthUnit.YARDS);
        assertEquals(0.667, sum.getValue(), EPS);
        assertEquals(LengthUnit.YARDS, sum.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_AllUnitCombinations() {
        QuantityLength[] operands1 = {
                new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCHES),
                new QuantityLength(1.0, LengthUnit.YARDS),
                new QuantityLength(30.48, LengthUnit.CENTIMETERS)
        };
        QuantityLength[] operands2 = {
                new QuantityLength(2.0, LengthUnit.FEET),
                new QuantityLength(24.0, LengthUnit.INCHES),
                new QuantityLength(2.0, LengthUnit.YARDS),
                new QuantityLength(60.96, LengthUnit.CENTIMETERS)
        };
        LengthUnit[] targetUnits = LengthUnit.values();

        for (QuantityLength op1 : operands1) {
            for (QuantityLength op2 : operands2) {
                for (LengthUnit target : targetUnits) {
                    QuantityLength sum = op1.add(op2, target);
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
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength sum1 = a.add(b, LengthUnit.YARDS);

        QuantityLength c = new QuantityLength(36.0, LengthUnit.INCHES);
        QuantityLength d = new QuantityLength(36.0, LengthUnit.INCHES);
        QuantityLength sum2 = c.add(d, LengthUnit.YARDS);

        assertEquals(sum1.getValue() * 3, sum2.getValue(), EPS);
    }

    @Test
    public void testBackwardCompatibility_UC1EqualityTests() {
        assertTrue(new QuantityLength(1.0, LengthUnit.FEET).equals(new QuantityLength(1.0, LengthUnit.FEET)));
        assertTrue(new QuantityLength(12.0, LengthUnit.INCHES).equals(new QuantityLength(1.0, LengthUnit.FEET)));
        assertTrue(new QuantityLength(1.0, LengthUnit.YARDS).equals(new QuantityLength(36.0, LengthUnit.INCHES)));
        assertFalse(new QuantityLength(1.0, LengthUnit.FEET).equals(new QuantityLength(2.0, LengthUnit.FEET)));
    }

    @Test
    public void testBackwardCompatibility_UC5ConversionTests() {
        QuantityLength feetToInches = QuantityMeasurementApp.demonstrateLengthConversion(3.0, LengthUnit.FEET,
                LengthUnit.INCHES);
        QuantityLength yardsToInches = QuantityMeasurementApp.demonstrateLengthConversion(2.0, LengthUnit.YARDS,
                LengthUnit.INCHES);
        QuantityLength centimetersToFeet = QuantityMeasurementApp.demonstrateLengthConversion(30.48, LengthUnit.CENTIMETERS,
                LengthUnit.FEET);

        assertEquals(36.0, feetToInches.getValue(), EPS);
        assertEquals(72.0, yardsToInches.getValue(), EPS);
        assertEquals(1.0, centimetersToFeet.getValue(), EPS);
    }

    @Test
    public void testBackwardCompatibility_UC6AdditionTests() {
        QuantityLength sameUnitSum = new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(2.0, LengthUnit.FEET));
        QuantityLength crossUnitSum = new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCHES));
        QuantityLength centimeterInchSum = new QuantityLength(2.54, LengthUnit.CENTIMETERS).add(new QuantityLength(1.0, LengthUnit.INCHES));

        assertEquals(3.0, sameUnitSum.getValue(), EPS);
        assertEquals(2.0, crossUnitSum.getValue(), EPS);
        assertEquals(5.08, centimeterInchSum.getValue(), 1e-2);
    }

    @Test
    public void testBackwardCompatibility_UC7AdditionWithTargetUnitTests() {
        QuantityLength sumInFeet = new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCHES),
                LengthUnit.FEET);
        QuantityLength sumInInches = new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCHES),
                LengthUnit.INCHES);
        QuantityLength sumInYards = new QuantityLength(1.0, LengthUnit.YARDS).add(new QuantityLength(3.0, LengthUnit.FEET),
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