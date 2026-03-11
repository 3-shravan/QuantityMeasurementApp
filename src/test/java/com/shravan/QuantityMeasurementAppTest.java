package com.shravan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class QuantityMeasurementAppTest {

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
        QuantityLength sum = new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCHES),
                LengthUnit.FEET);
        assertEquals(2.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.FEET, sum.getUnit());
    }

    @Test
    public void testQuantityLengthRefactored_AddWithTargetUnit() {
        QuantityLength sum = new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCHES),
                LengthUnit.YARDS);
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
        QuantityLength lengthInInches = QuantityMeasurementApp.demonstrateLengthConversion(lengthInYards,
                LengthUnit.INCHES);
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
        QuantityLength centimetersToFeet = QuantityMeasurementApp.demonstrateLengthConversion(30.48,
                LengthUnit.CENTIMETERS,
                LengthUnit.FEET);

        assertEquals(36.0, feetToInches.getValue(), EPS);
        assertEquals(72.0, yardsToInches.getValue(), EPS);
        assertEquals(1.0, centimetersToFeet.getValue(), EPS);
    }

    @Test
    public void testBackwardCompatibility_UC6AdditionTests() {
        QuantityLength sameUnitSum = new QuantityLength(1.0, LengthUnit.FEET)
                .add(new QuantityLength(2.0, LengthUnit.FEET));
        QuantityLength crossUnitSum = new QuantityLength(1.0, LengthUnit.FEET)
                .add(new QuantityLength(12.0, LengthUnit.INCHES));
        QuantityLength centimeterInchSum = new QuantityLength(2.54, LengthUnit.CENTIMETERS)
                .add(new QuantityLength(1.0, LengthUnit.INCHES));

        assertEquals(3.0, sameUnitSum.getValue(), EPS);
        assertEquals(2.0, crossUnitSum.getValue(), EPS);
        assertEquals(5.08, centimeterInchSum.getValue(), 1e-2);
    }

    @Test
    public void testBackwardCompatibility_UC7AdditionWithTargetUnitTests() {
        QuantityLength sumInFeet = new QuantityLength(1.0, LengthUnit.FEET).add(
                new QuantityLength(12.0, LengthUnit.INCHES),
                LengthUnit.FEET);
        QuantityLength sumInInches = new QuantityLength(1.0, LengthUnit.FEET).add(
                new QuantityLength(12.0, LengthUnit.INCHES),
                LengthUnit.INCHES);
        QuantityLength sumInYards = new QuantityLength(1.0, LengthUnit.YARDS).add(
                new QuantityLength(3.0, LengthUnit.FEET),
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
        assertEquals("KILOGRAM", WeightUnit.KILOGRAM.name());
    }

    @Test
    public void testUnitImmutability() {
        LengthUnit[] units = LengthUnit.values();
        units[0] = null;

        assertTrue(LengthUnit.FEET == LengthUnit.valueOf("FEET"));
        assertEquals(4, LengthUnit.values().length);
        assertEquals(LengthUnit.FEET, LengthUnit.values()[0]);
    }

    @Test
    public void testWeightUnitEnum_KilogramConstant() {
        assertEquals(1.0, WeightUnit.KILOGRAM.getConversionFactor(), EPS);
    }

    @Test
    public void testWeightUnitEnum_GramConstant() {
        assertEquals(0.001, WeightUnit.GRAM.getConversionFactor(), EPS);
    }

    @Test
    public void testWeightUnitEnum_PoundConstant() {
        assertEquals(0.453592, WeightUnit.POUND.getConversionFactor(), EPS);
    }

    @Test
    public void testEquality_KilogramToKilogram_SameValue() {
        assertTrue(new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                .equals(new QuantityWeight(1.0, WeightUnit.KILOGRAM)));
    }

    @Test
    public void testEquality_KilogramToKilogram_DifferentValue() {
        assertFalse(new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                .equals(new QuantityWeight(2.0, WeightUnit.KILOGRAM)));
    }

    @Test
    public void testEquality_KilogramToGram_EquivalentValue() {
        assertTrue(new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                .equals(new QuantityWeight(1000.0, WeightUnit.GRAM)));
    }

    @Test
    public void testEquality_GramToKilogram_EquivalentValue() {
        assertTrue(new QuantityWeight(1000.0, WeightUnit.GRAM)
                .equals(new QuantityWeight(1.0, WeightUnit.KILOGRAM)));
    }

    @Test
    public void testEquality_WeightVsLength_Incompatible() {
        assertFalse(new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                .equals(new QuantityLength(1.0, LengthUnit.FEET)));
    }

    @Test
    public void testEquality_NullComparison() {
        assertFalse(new QuantityWeight(1.0, WeightUnit.KILOGRAM).equals(null));
    }

    @Test
    public void testEquality_SameReference() {
        QuantityWeight weight = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        assertTrue(weight.equals(weight));
    }

    @Test(expected = NullPointerException.class)
    public void testEquality_NullUnit_Weight() {
        new QuantityWeight(1.0, null);
    }

    @Test
    public void testEquality_TransitiveProperty() {
        QuantityWeight kilogram = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight gram = new QuantityWeight(1000.0, WeightUnit.GRAM);
        QuantityWeight pound = new QuantityWeight(2.2046244202, WeightUnit.POUND);

        assertTrue(kilogram.equals(gram));
        assertTrue(gram.equals(pound));
        assertTrue(kilogram.equals(pound));
    }

    @Test
    public void testEquality_ZeroValue() {
        assertTrue(new QuantityWeight(0.0, WeightUnit.KILOGRAM)
                .equals(new QuantityWeight(0.0, WeightUnit.GRAM)));
    }

    @Test
    public void testEquality_NegativeWeight() {
        assertTrue(new QuantityWeight(-1.0, WeightUnit.KILOGRAM)
                .equals(new QuantityWeight(-1000.0, WeightUnit.GRAM)));
    }

    @Test
    public void testEquality_LargeWeightValue() {
        assertTrue(new QuantityWeight(1_000_000.0, WeightUnit.GRAM)
                .equals(new QuantityWeight(1000.0, WeightUnit.KILOGRAM)));
    }

    @Test
    public void testEquality_SmallWeightValue() {
        assertTrue(new QuantityWeight(0.001, WeightUnit.KILOGRAM)
                .equals(new QuantityWeight(1.0, WeightUnit.GRAM)));
    }

    @Test
    public void testConversion_PoundToKilogram() {
        QuantityWeight converted = new QuantityWeight(2.20462, WeightUnit.POUND).convertTo(WeightUnit.KILOGRAM);
        assertEquals(0.999998, converted.getValue(), 1e-3);
        assertEquals(WeightUnit.KILOGRAM, converted.getUnit());
    }

    @Test
    public void testConversion_KilogramToPound() {
        QuantityWeight converted = new QuantityWeight(1.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.POUND);
        assertEquals(2.2046244202, converted.getValue(), 1e-3);
        assertEquals(WeightUnit.POUND, converted.getUnit());
    }

    @Test
    public void testConversion_SameUnit() {
        QuantityWeight converted = new QuantityWeight(5.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.KILOGRAM);
        assertEquals(5.0, converted.getValue(), EPS);
        assertEquals(WeightUnit.KILOGRAM, converted.getUnit());
    }

    @Test
    public void testConversion_ZeroValue() {
        QuantityWeight converted = new QuantityWeight(0.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM);
        assertEquals(0.0, converted.getValue(), EPS);
        assertEquals(WeightUnit.GRAM, converted.getUnit());
    }

    @Test
    public void testConversion_NegativeValue() {
        QuantityWeight converted = new QuantityWeight(-1.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM);
        assertEquals(-1000.0, converted.getValue(), EPS);
        assertEquals(WeightUnit.GRAM, converted.getUnit());
    }

    @Test
    public void testConversion_RoundTrip() {
        QuantityWeight converted = new QuantityWeight(1.5, WeightUnit.KILOGRAM)
                .convertTo(WeightUnit.GRAM)
                .convertTo(WeightUnit.KILOGRAM);
        assertEquals(1.5, converted.getValue(), 1e-6);
        assertEquals(WeightUnit.KILOGRAM, converted.getUnit());
    }

    @Test
    public void testAddition_SameUnit_KilogramPlusKilogram() {
        QuantityWeight sum = new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                .add(new QuantityWeight(2.0, WeightUnit.KILOGRAM));
        assertEquals(3.0, sum.getValue(), EPS);
        assertEquals(WeightUnit.KILOGRAM, sum.getUnit());
    }

    @Test
    public void testAddition_CrossUnit_KilogramPlusGram() {
        QuantityWeight sum = new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                .add(new QuantityWeight(1000.0, WeightUnit.GRAM));
        assertEquals(2.0, sum.getValue(), EPS);
        assertEquals(WeightUnit.KILOGRAM, sum.getUnit());
    }

    @Test
    public void testAddition_CrossUnit_PoundPlusKilogram() {
        QuantityWeight sum = new QuantityWeight(2.20462, WeightUnit.POUND)
                .add(new QuantityWeight(1.0, WeightUnit.KILOGRAM));
        assertEquals(4.40924, sum.getValue(), 1e-2);
        assertEquals(WeightUnit.POUND, sum.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Kilogram() {
        QuantityWeight sum = new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                .add(new QuantityWeight(1000.0, WeightUnit.GRAM), WeightUnit.GRAM);
        assertEquals(2000.0, sum.getValue(), EPS);
        assertEquals(WeightUnit.GRAM, sum.getUnit());
    }

    @Test
    public void testAddition_Commutativity_Weight() {
        QuantityWeight sum1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                .add(new QuantityWeight(1000.0, WeightUnit.GRAM), WeightUnit.GRAM);
        QuantityWeight sum2 = new QuantityWeight(1000.0, WeightUnit.GRAM)
                .add(new QuantityWeight(1.0, WeightUnit.KILOGRAM), WeightUnit.GRAM);
        assertEquals(sum1.getValue(), sum2.getValue(), EPS);
        assertEquals(sum1.getUnit(), sum2.getUnit());
    }

    @Test
    public void testAddition_WithZero_Weight() {
        QuantityWeight sum = new QuantityWeight(5.0, WeightUnit.KILOGRAM)
                .add(new QuantityWeight(0.0, WeightUnit.GRAM));
        assertEquals(5.0, sum.getValue(), EPS);
        assertEquals(WeightUnit.KILOGRAM, sum.getUnit());
    }

    @Test
    public void testAddition_NegativeValues_Weight() {
        QuantityWeight sum = new QuantityWeight(5.0, WeightUnit.KILOGRAM)
                .add(new QuantityWeight(-2000.0, WeightUnit.GRAM));
        assertEquals(3.0, sum.getValue(), EPS);
        assertEquals(WeightUnit.KILOGRAM, sum.getUnit());
    }

    @Test
    public void testAddition_LargeValues_Weight() {
        QuantityWeight sum = new QuantityWeight(1e6, WeightUnit.KILOGRAM)
                .add(new QuantityWeight(1e6, WeightUnit.KILOGRAM));
        assertEquals(2e6, sum.getValue(), 1e-2);
        assertEquals(WeightUnit.KILOGRAM, sum.getUnit());
    }

    @Test
    public void testWeightDemonstrationMethods() {
        assertTrue(
                QuantityMeasurementApp.demonstrateWeightComparison(1.0, WeightUnit.KILOGRAM, 1000.0, WeightUnit.GRAM));

        QuantityWeight converted = QuantityMeasurementApp.demonstrateWeightConversion(500.0, WeightUnit.GRAM,
                WeightUnit.POUND);
        assertEquals(1.1023122101, converted.getValue(), 1e-3);

        QuantityWeight sum = QuantityMeasurementApp.demonstrateWeightAddition(2.0, WeightUnit.KILOGRAM, 4.0,
                WeightUnit.POUND, WeightUnit.KILOGRAM);
        assertEquals(3.814368, sum.getValue(), 1e-3);
    }

    @Test
    public void testEquality_LitreToLitre_SameValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1.0, VolumeUnit.LITRE)));
    }

    @Test
    public void testEquality_LitreToLitre_DifferentValue() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(2.0, VolumeUnit.LITRE)));
    }

    @Test
    public void testEquality_LitreToMillilitre_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    public void testEquality_MillilitreToLitre_EquivalentValue() {
        assertTrue(new Quantity<>(1000.0, VolumeUnit.MILLILITRE).equals(new Quantity<>(1.0, VolumeUnit.LITRE)));
    }

    @Test
    public void testEquality_LitreToGallon_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(0.264172, VolumeUnit.GALLON)));
    }

    @Test
    public void testEquality_GallonToLitre_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.GALLON).equals(new Quantity<>(3.78541, VolumeUnit.LITRE)));
    }

    @Test
    public void testEquality_VolumeVsLength_Incompatible() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1.0, LengthUnit.FEET)));
    }

    @Test
    public void testEquality_VolumeVsWeight_Incompatible() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
    }

    @Test
    public void testEquality_NullComparison_Volume() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(null));
    }

    @Test
    public void testEquality_SameReference_Volume() {
        Quantity<VolumeUnit> quantity = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertTrue(quantity.equals(quantity));
    }

    @Test
    public void testEquality_NullUnit_Volume() {
        expectException(IllegalArgumentException.class, () -> new Quantity<>(1.0, (VolumeUnit) null));
    }

    @Test
    public void testEquality_TransitiveProperty_Volume() {
        Quantity<VolumeUnit> litre = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> millilitre = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> gallonEquivalent = new Quantity<>(0.264172, VolumeUnit.GALLON);

        assertTrue(litre.equals(millilitre));
        assertTrue(millilitre.equals(gallonEquivalent));
        assertTrue(litre.equals(gallonEquivalent));
    }

    @Test
    public void testEquality_ZeroValue_Volume() {
        assertTrue(new Quantity<>(0.0, VolumeUnit.LITRE).equals(new Quantity<>(0.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    public void testEquality_NegativeVolume() {
        assertTrue(new Quantity<>(-1.0, VolumeUnit.LITRE).equals(new Quantity<>(-1000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    public void testEquality_LargeVolumeValue() {
        assertTrue(new Quantity<>(1_000_000.0, VolumeUnit.MILLILITRE).equals(new Quantity<>(1000.0, VolumeUnit.LITRE)));
    }

    @Test
    public void testEquality_SmallVolumeValue() {
        assertTrue(new Quantity<>(0.001, VolumeUnit.LITRE).equals(new Quantity<>(1.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    public void testConversion_LitreToMillilitre() {
        assertEquals("Quantity(1000.0, MILLILITRE)",
                new Quantity<>(1.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE).toString());
    }

    @Test
    public void testConversion_MillilitreToLitre() {
        assertEquals("Quantity(1.0, LITRE)",
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE).convertTo(VolumeUnit.LITRE).toString());
    }

    @Test
    public void testConversion_GallonToLitre() {
        assertEquals(3.78541, new Quantity<>(1.0, VolumeUnit.GALLON).convertTo(VolumeUnit.LITRE).getValue(), 1e-6);
    }

    @Test
    public void testConversion_LitreToGallon() {
        assertEquals(1.0, new Quantity<>(3.78541, VolumeUnit.LITRE).convertTo(VolumeUnit.GALLON).getValue(), 1e-6);
    }

    @Test
    public void testConversion_MillilitreToGallon() {
        assertEquals(0.264172, new Quantity<>(1000.0, VolumeUnit.MILLILITRE).convertTo(VolumeUnit.GALLON).getValue(),
                1e-6);
    }

    @Test
    public void testConversion_SameUnit_Volume() {
        assertEquals("Quantity(5.0, LITRE)",
                new Quantity<>(5.0, VolumeUnit.LITRE).convertTo(VolumeUnit.LITRE).toString());
    }

    @Test
    public void testConversion_ZeroValue_Volume() {
        assertEquals("Quantity(0.0, MILLILITRE)",
                new Quantity<>(0.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE).toString());
    }

    @Test
    public void testConversion_NegativeValue_Volume() {
        assertEquals("Quantity(-1000.0, MILLILITRE)",
                new Quantity<>(-1.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE).toString());
    }

    @Test
    public void testConversion_RoundTrip_Volume() {
        assertEquals(1.5, new Quantity<>(1.5, VolumeUnit.LITRE)
                .convertTo(VolumeUnit.MILLILITRE)
                .convertTo(VolumeUnit.LITRE)
                .getValue(), 1e-6);
    }

    @Test
    public void testAddition_SameUnit_LitrePlusLitre() {
        assertEquals("Quantity(3.0, LITRE)",
                new Quantity<>(1.0, VolumeUnit.LITRE).add(new Quantity<>(2.0, VolumeUnit.LITRE)).toString());
    }

    @Test
    public void testAddition_SameUnit_MillilitrePlusMillilitre() {
        assertEquals("Quantity(1000.0, MILLILITRE)",
                new Quantity<>(500.0, VolumeUnit.MILLILITRE).add(new Quantity<>(500.0, VolumeUnit.MILLILITRE))
                        .toString());
    }

    @Test
    public void testAddition_CrossUnit_LitrePlusMillilitre() {
        assertEquals("Quantity(2.0, LITRE)",
                new Quantity<>(1.0, VolumeUnit.LITRE).add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)).toString());
    }

    @Test
    public void testAddition_CrossUnit_MillilitrePlusLitre() {
        assertEquals("Quantity(2000.0, MILLILITRE)",
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE).add(new Quantity<>(1.0, VolumeUnit.LITRE)).toString());
    }

    @Test
    public void testAddition_CrossUnit_GallonPlusLitre() {
        assertEquals(2.0,
                new Quantity<>(1.0, VolumeUnit.GALLON).add(new Quantity<>(3.78541, VolumeUnit.LITRE)).getValue(),
                1e-6);
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Litre() {
        assertEquals("Quantity(2.0, LITRE)", new Quantity<>(1.0, VolumeUnit.LITRE)
                .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE), VolumeUnit.LITRE).toString());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Millilitre() {
        assertEquals("Quantity(2000.0, MILLILITRE)", new Quantity<>(1.0, VolumeUnit.LITRE)
                .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE), VolumeUnit.MILLILITRE).toString());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Gallon() {
        assertEquals(2.0, new Quantity<>(3.78541, VolumeUnit.LITRE)
                .add(new Quantity<>(3.78541, VolumeUnit.LITRE), VolumeUnit.GALLON).getValue(), 1e-6);
    }

    @Test
    public void testAddition_Commutativity_Volume() {
        Quantity<VolumeUnit> sumInLitre = new Quantity<>(1.0, VolumeUnit.LITRE)
                .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE));
        Quantity<VolumeUnit> sumInMillilitre = new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                .add(new Quantity<>(1.0, VolumeUnit.LITRE))
                .convertTo(VolumeUnit.LITRE);

        assertEquals(sumInLitre.getValue(), sumInMillilitre.getValue(), 1e-6);
    }

    @Test
    public void testAddition_WithZero_Volume() {
        assertEquals("Quantity(5.0, LITRE)",
                new Quantity<>(5.0, VolumeUnit.LITRE).add(new Quantity<>(0.0, VolumeUnit.MILLILITRE)).toString());
    }

    @Test
    public void testAddition_NegativeValues_Volume() {
        assertEquals("Quantity(3.0, LITRE)",
                new Quantity<>(5.0, VolumeUnit.LITRE).add(new Quantity<>(-2000.0, VolumeUnit.MILLILITRE)).toString());
    }

    @Test
    public void testAddition_LargeValues_Volume() {
        assertEquals("Quantity(2000000.0, LITRE)",
                new Quantity<>(1e6, VolumeUnit.LITRE).add(new Quantity<>(1e6, VolumeUnit.LITRE)).toString());
    }

    @Test
    public void testAddition_SmallValues_Volume() {
        assertEquals(0.003,
                new Quantity<>(0.001, VolumeUnit.LITRE).add(new Quantity<>(0.002, VolumeUnit.LITRE)).getValue(),
                1e-6);
    }

    @Test
    public void testVolumeUnitEnum_LitreConstant() {
        assertEquals(1.0, VolumeUnit.LITRE.getConversionFactor(), EPS);
    }

    @Test
    public void testVolumeUnitEnum_MillilitreConstant() {
        assertEquals(0.001, VolumeUnit.MILLILITRE.getConversionFactor(), EPS);
    }

    @Test
    public void testVolumeUnitEnum_GallonConstant() {
        assertEquals(3.78541, VolumeUnit.GALLON.getConversionFactor(), EPS);
    }

    @Test
    public void testConvertToBaseUnit_LitreToLitre() {
        assertEquals(5.0, VolumeUnit.LITRE.convertToBaseUnit(5.0), EPS);
    }

    @Test
    public void testConvertToBaseUnit_MillilitreToLitre() {
        assertEquals(1.0, VolumeUnit.MILLILITRE.convertToBaseUnit(1000.0), EPS);
    }

    @Test
    public void testConvertToBaseUnit_GallonToLitre() {
        assertEquals(3.78541, VolumeUnit.GALLON.convertToBaseUnit(1.0), EPS);
    }

    @Test
    public void testConvertFromBaseUnit_LitreToLitre() {
        assertEquals(2.0, VolumeUnit.LITRE.convertFromBaseUnit(2.0), EPS);
    }

    @Test
    public void testConvertFromBaseUnit_LitreToMillilitre() {
        assertEquals(1000.0, VolumeUnit.MILLILITRE.convertFromBaseUnit(1.0), EPS);
    }

    @Test
    public void testConvertFromBaseUnit_LitreToGallon() {
        assertEquals(1.0, VolumeUnit.GALLON.convertFromBaseUnit(3.78541), 1e-6);
    }

    @Test
    public void testBackwardCompatibility_AllUC1Through10Tests() {
        assertTrue(new Quantity<>(1.0, LengthUnit.FEET).equals(new Quantity<>(12.0, LengthUnit.INCHES)));
        assertTrue(new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(1000.0, WeightUnit.GRAM)));
        assertEquals("Quantity(2.0, FEET)",
                QuantityMeasurementApp.demonstrateAddition(new Quantity<>(1.0, LengthUnit.FEET),
                        new Quantity<>(12.0, LengthUnit.INCHES), LengthUnit.FEET).toString());
        assertEquals("Quantity(2.0, KILOGRAM)", QuantityMeasurementApp
                .demonstrateAddition(new Quantity<>(1.0, WeightUnit.KILOGRAM), new Quantity<>(1000.0, WeightUnit.GRAM),
                        WeightUnit.KILOGRAM)
                .toString());
    }

    @Test
    public void testGenericQuantity_VolumeOperations_Consistency() {
        assertTrue(QuantityMeasurementApp.demonstrateEquality(new Quantity<>(1.0, VolumeUnit.LITRE),
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE)));
        assertEquals("Quantity(1000.0, MILLILITRE)",
                QuantityMeasurementApp
                        .demonstrateConversion(new Quantity<>(1.0, VolumeUnit.LITRE), VolumeUnit.MILLILITRE)
                        .toString());
        assertEquals("Quantity(2.0, LITRE)",
                QuantityMeasurementApp.demonstrateAddition(new Quantity<>(1.0, VolumeUnit.LITRE),
                        new Quantity<>(1000.0, VolumeUnit.MILLILITRE), VolumeUnit.LITRE).toString());
    }

    @Test
    public void testScalability_VolumeIntegration() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)));
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1.0, LengthUnit.FEET)));
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
    }

    @Test
    public void testIMeasurableInterface_LengthUnitImplementation() {
        assertTrue(IMeasurable.class.isAssignableFrom(LengthUnit.class));
        assertEquals(1.0, LengthUnit.FEET.getConversionFactor(), EPS);
        assertEquals(1.0, LengthUnit.FEET.convertToBaseUnit(1.0), EPS);
        assertEquals(12.0, LengthUnit.INCHES.convertFromBaseUnit(1.0), EPS);
        assertEquals("FEET", LengthUnit.FEET.getUnitName());
    }

    @Test
    public void testIMeasurableInterface_WeightUnitImplementation() {
        assertTrue(IMeasurable.class.isAssignableFrom(WeightUnit.class));
        assertEquals(1.0, WeightUnit.KILOGRAM.getConversionFactor(), EPS);
        assertEquals(1.0, WeightUnit.KILOGRAM.convertToBaseUnit(1.0), EPS);
        assertEquals(1000.0, WeightUnit.GRAM.convertFromBaseUnit(1.0), EPS);
        assertEquals("KILOGRAM", WeightUnit.KILOGRAM.getUnitName());
    }

    @Test
    public void testIMeasurableInterface_ConsistentBehavior() throws Exception {
        java.lang.reflect.Method conversionFactor = IMeasurable.class.getMethod("getConversionFactor");
        java.lang.reflect.Method convertToBaseUnit = IMeasurable.class.getMethod("convertToBaseUnit", double.class);
        java.lang.reflect.Method convertFromBaseUnit = IMeasurable.class.getMethod("convertFromBaseUnit", double.class);
        java.lang.reflect.Method getUnitName = IMeasurable.class.getMethod("getUnitName");

        assertEquals(double.class, conversionFactor.getReturnType());
        assertEquals(double.class, convertToBaseUnit.getReturnType());
        assertEquals(double.class, convertFromBaseUnit.getReturnType());
        assertEquals(String.class, getUnitName.getReturnType());

        assertEquals(3.0, LengthUnit.YARDS.convertToBaseUnit(1.0), EPS);
        assertEquals(1.0, WeightUnit.GRAM.convertToBaseUnit(1000.0), EPS);
    }

    @Test
    public void testGenericQuantity_LengthOperations_Equality() {
        assertTrue(new Quantity<>(1.0, LengthUnit.FEET).equals(new Quantity<>(12.0, LengthUnit.INCHES)));
    }

    @Test
    public void testGenericQuantity_WeightOperations_Equality() {
        assertTrue(new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(1000.0, WeightUnit.GRAM)));
    }

    @Test
    public void testGenericQuantity_LengthOperations_Conversion() {
        assertEquals("Quantity(12.0, INCHES)",
                new Quantity<>(1.0, LengthUnit.FEET).convertTo(LengthUnit.INCHES).toString());
    }

    @Test
    public void testGenericQuantity_WeightOperations_Conversion() {
        assertEquals("Quantity(1000.0, GRAM)",
                new Quantity<>(1.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM).toString());
    }

    @Test
    public void testGenericQuantity_LengthOperations_Addition() {
        assertEquals("Quantity(2.0, FEET)",
                new Quantity<>(1.0, LengthUnit.FEET)
                        .add(new Quantity<>(12.0, LengthUnit.INCHES), LengthUnit.FEET)
                        .toString());
    }

    @Test
    public void testGenericQuantity_WeightOperations_Addition() {
        assertEquals("Quantity(2.0, KILOGRAM)",
                new Quantity<>(1.0, WeightUnit.KILOGRAM)
                        .add(new Quantity<>(1000.0, WeightUnit.GRAM), WeightUnit.KILOGRAM)
                        .toString());
    }

    @Test
    public void testCrossCategoryPrevention_LengthVsWeight() {
        assertFalse(new Quantity<>(1.0, LengthUnit.FEET).equals(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
    }

    @Test
    public void testCrossCategoryPrevention_CompilerTypeSafety() throws Exception {
        String source = "import com.shravan.*;\n"
                + "public class TypeMismatchCheck {\n"
                + "  public static void main(String[] args) {\n"
                + "    Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);\n"
                + "    Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);\n"
                + "    QuantityMeasurementApp.demonstrateEquality(length, weight);\n"
                + "  }\n"
                + "}\n";

        assertFalse(compileSnippet("TypeMismatchCheck", source));
    }

    @Test
    public void testGenericQuantity_ConstructorValidation_NullUnit() {
        expectException(IllegalArgumentException.class, () -> new Quantity<>(1.0, (LengthUnit) null));
    }

    @Test
    public void testGenericQuantity_ConstructorValidation_InvalidValue() {
        expectException(IllegalArgumentException.class, () -> new Quantity<>(Double.NaN, LengthUnit.FEET));
    }

    @Test
    public void testGenericQuantity_Conversion_AllUnitCombinations() {
        assertAllConversionsMatchBaseUnit(LengthUnit.values(), new double[] { 1.0, 3.0, 30.48 });
        assertAllConversionsMatchBaseUnit(WeightUnit.values(), new double[] { 1.0, 2.5, 1000.0 });
        assertAllConversionsMatchBaseUnit(VolumeUnit.values(), new double[] { 1.0, 500.0, 3.78541 });
    }

    @Test
    public void testGenericQuantity_Addition_AllUnitCombinations() {
        assertAllAdditionsMatchBaseUnit(LengthUnit.values(), 2.0, 3.0);
        assertAllAdditionsMatchBaseUnit(WeightUnit.values(), 1.0, 4.0);
        assertAllAdditionsMatchBaseUnit(VolumeUnit.values(), 1.0, 2.0);
    }

    @Test
    public void testBackwardCompatibility_AllUC1Through9Tests() {
        assertTrue(new QuantityLength(1.0, LengthUnit.FEET).equals(new QuantityLength(12.0, LengthUnit.INCHES)));
        assertEquals(36.0,
                QuantityMeasurementApp.demonstrateLengthConversion(3.0, LengthUnit.FEET, LengthUnit.INCHES).getValue(),
                EPS);
        assertEquals(2.0,
                QuantityMeasurementApp.demonstrateLengthAddition(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCHES,
                        LengthUnit.FEET).getValue(),
                EPS);
        assertTrue(new QuantityWeight(1.0, WeightUnit.KILOGRAM).equals(new QuantityWeight(1000.0, WeightUnit.GRAM)));
    }

    @Test
    public void testQuantityMeasurementApp_SimplifiedDemonstration_Equality() {
        assertTrue(QuantityMeasurementApp.demonstrateEquality(new Quantity<>(1.0, LengthUnit.FEET),
                new Quantity<>(12.0, LengthUnit.INCHES)));
        assertTrue(QuantityMeasurementApp.demonstrateEquality(new Quantity<>(1.0, WeightUnit.KILOGRAM),
                new Quantity<>(1000.0, WeightUnit.GRAM)));
    }

    @Test
    public void testQuantityMeasurementApp_SimplifiedDemonstration_Conversion() {
        assertEquals("Quantity(12.0, INCHES)",
                QuantityMeasurementApp.demonstrateConversion(new Quantity<>(1.0, LengthUnit.FEET), LengthUnit.INCHES)
                        .toString());
        assertEquals("Quantity(1000.0, GRAM)", QuantityMeasurementApp
                .demonstrateConversion(new Quantity<>(1.0, WeightUnit.KILOGRAM), WeightUnit.GRAM).toString());
    }

    @Test
    public void testQuantityMeasurementApp_SimplifiedDemonstration_Addition() {
        assertEquals("Quantity(2.0, FEET)",
                QuantityMeasurementApp.demonstrateAddition(new Quantity<>(1.0, LengthUnit.FEET),
                        new Quantity<>(12.0, LengthUnit.INCHES), LengthUnit.FEET).toString());
        assertEquals("Quantity(2.0, KILOGRAM)", QuantityMeasurementApp
                .demonstrateAddition(new Quantity<>(1.0, WeightUnit.KILOGRAM), new Quantity<>(1000.0, WeightUnit.GRAM),
                        WeightUnit.KILOGRAM)
                .toString());
    }

    @Test
    public void testTypeWildcard_FlexibleSignatures() {
        assertEquals("FEET", describeQuantity(new Quantity<>(1.0, LengthUnit.FEET)));
        assertEquals("KILOGRAM", describeQuantity(new Quantity<>(1.0, WeightUnit.KILOGRAM)));
    }

    @Test
    public void testScalability_NewUnitEnumIntegration() {
        Quantity<VolumeUnit> liters = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> milliliters = liters.convertTo(VolumeUnit.MILLILITRE);

        assertEquals("Quantity(1000.0, MILLILITRE)", milliliters.toString());
        assertTrue(liters.equals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    public void testScalability_MultipleNewCategories() {
        assertTrue(new Quantity<>(1.0, TimeScaleUnit.HOUR).equals(new Quantity<>(60.0, TimeScaleUnit.MINUTE)));
        assertTrue(new Quantity<>(2.0, VolumeUnit.LITRE).equals(new Quantity<>(2000.0, VolumeUnit.MILLILITRE)));
        assertTrue(new Quantity<>(1.0, TemperatureDeltaUnit.DEGREE)
                .equals(new Quantity<>(1000.0, TemperatureDeltaUnit.MILLI_DEGREE)));
    }

    @Test
    public void testGenericBoundedTypeParameter_Enforcement() throws Exception {
        String source = "import com.shravan.Quantity;\n"
                + "enum InvalidUnit { ONE }\n"
                + "public class InvalidBoundCheck {\n"
                + "  Quantity<InvalidUnit> quantity = new Quantity<>(1.0, InvalidUnit.ONE);\n"
                + "}\n";

        assertFalse(compileSnippet("InvalidBoundCheck", source));
    }

    @Test
    public void testHashCode_GenericQuantity_Consistency() {
        assertEquals(new Quantity<>(1.0, LengthUnit.FEET).hashCode(),
                new Quantity<>(12.0, LengthUnit.INCHES).hashCode());
        assertEquals(new Quantity<>(1.0, WeightUnit.KILOGRAM).hashCode(),
                new Quantity<>(1000.0, WeightUnit.GRAM).hashCode());
    }

    @Test
    public void testEquals_GenericQuantity_ContractPreservation() {
        Quantity<LengthUnit> first = new Quantity<>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> second = new Quantity<>(3.0, LengthUnit.FEET);
        Quantity<LengthUnit> third = new Quantity<>(36.0, LengthUnit.INCHES);

        assertTrue(first.equals(first));
        assertTrue(first.equals(second));
        assertTrue(second.equals(first));
        assertTrue(second.equals(third));
        assertTrue(first.equals(third));
    }

    @Test
    public void testEnumAsUnitCarrier_BehaviorEncapsulation() {
        IMeasurable length = LengthUnit.INCHES;
        IMeasurable weight = WeightUnit.GRAM;

        assertEquals(1.0, length.convertToBaseUnit(12.0), EPS);
        assertEquals(1.0, weight.convertToBaseUnit(1000.0), EPS);
    }

    @Test
    public void testTypeErasure_RuntimeSafety() {
        Quantity<? extends IMeasurable> rawLength = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<? extends IMeasurable> rawWeight = new Quantity<>(1.0, WeightUnit.KILOGRAM);

        assertFalse(rawLength.equals(rawWeight));
    }

    @Test
    public void testCompositionOverInheritance_Flexibility() {
        Quantity<CustomUnit> customQuantity = new Quantity<>(2.0, new CustomUnit(10.0, "TOKEN"));
        Quantity<CustomUnit> converted = customQuantity.convertTo(new CustomUnit(5.0, "HALF_TOKEN"));

        assertEquals("Quantity(4.0, HALF_TOKEN)", converted.toString());
    }

    @Test
    public void testCodeReduction_DRYValidation() throws Exception {
        int genericLines = java.nio.file.Files.readAllLines(
                java.nio.file.Paths.get("src/main/java/com/shravan/Quantity.java")).size();
        int wrapperLines = java.nio.file.Files.readAllLines(
                java.nio.file.Paths.get("src/main/java/com/shravan/QuantityLength.java")).size()
                + java.nio.file.Files
                        .readAllLines(java.nio.file.Paths.get("src/main/java/com/shravan/QuantityWeight.java"))
                        .size();

        assertTrue(genericLines < wrapperLines);
    }

    @Test
    public void testMaintainability_SingleSourceOfTruth() throws Exception {
        java.lang.reflect.Field lengthField = QuantityLength.class.getDeclaredField("quantity");
        java.lang.reflect.Field weightField = QuantityWeight.class.getDeclaredField("quantity");

        assertEquals(Quantity.class, lengthField.getType());
        assertEquals(Quantity.class, weightField.getType());
        assertNotNull(Quantity.class.getDeclaredMethod("add", Quantity.class, IMeasurable.class));
        assertNotNull(Quantity.class.getDeclaredMethod("equals", Object.class));
    }

    @Test
    public void testArchitecturalReadiness_MultipleNewCategories() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE).equals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)));
        assertTrue(new Quantity<>(1.0, TimeScaleUnit.HOUR).equals(new Quantity<>(60.0, TimeScaleUnit.MINUTE)));
        assertTrue(new Quantity<>(1.0, TemperatureDeltaUnit.DEGREE)
                .equals(new Quantity<>(1000.0, TemperatureDeltaUnit.MILLI_DEGREE)));
        assertTrue(new Quantity<>(1.0, DistanceStepUnit.KILOSTEP)
                .equals(new Quantity<>(1000.0, DistanceStepUnit.STEP)));
        assertTrue(new Quantity<>(1.0, DataUnit.KILOBYTE).equals(new Quantity<>(1000.0, DataUnit.BYTE)));
    }

    @Test
    public void testPerformance_GenericOverhead() {
        long start = System.nanoTime();
        Quantity<LengthUnit> quantity = new Quantity<>(1.0, LengthUnit.FEET);

        for (int i = 0; i < 20000; i++) {
            quantity = quantity.add(new Quantity<>(12.0, LengthUnit.INCHES), LengthUnit.FEET)
                    .convertTo(LengthUnit.INCHES)
                    .convertTo(LengthUnit.FEET);
        }

        long elapsedMillis = java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        assertTrue(elapsedMillis < 5000);
    }

    @Test
    public void testDocumentation_PatternClarity() {
        Quantity<VolumeUnit> sample = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertEquals("Unified handling",
                QuantityMeasurementApp.demonstrateGenericHandling(sample,
                        new Quantity<>(1000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    public void testInterfaceSegregation_MinimalContract() {
        java.lang.reflect.Method[] methods = IMeasurable.class.getDeclaredMethods();
        assertEquals(5, methods.length);
    }

    @Test
    public void testImmutability_GenericQuantity() throws Exception {
        java.lang.reflect.Field valueField = Quantity.class.getDeclaredField("value");
        java.lang.reflect.Field unitField = Quantity.class.getDeclaredField("unit");

        assertTrue(java.lang.reflect.Modifier.isFinal(valueField.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isFinal(unitField.getModifiers()));
        assertFalse(hasSetter(Quantity.class));

        Quantity<LengthUnit> original = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> converted = original.convertTo(LengthUnit.INCHES);
        assertEquals("Quantity(1.0, FEET)", original.toString());
        assertEquals("Quantity(12.0, INCHES)", converted.toString());
    }

    private <U extends IMeasurable> void assertAllConversionsMatchBaseUnit(U[] units, double[] samples) {
        for (U source : units) {
            for (double sample : samples) {
                Quantity<U> quantity = new Quantity<>(sample, source);
                double baseValue = source.convertToBaseUnit(sample);

                for (U target : units) {
                    Quantity<U> converted = quantity.convertTo(target);
                    double expected = target.convertFromBaseUnit(baseValue);
                    assertEquals(expected, converted.getValue(), 1e-2);
                }
            }
        }
    }

    private <U extends IMeasurable> void assertAllAdditionsMatchBaseUnit(U[] units, double left, double right) {
        for (U firstUnit : units) {
            for (U secondUnit : units) {
                for (U targetUnit : units) {
                    Quantity<U> first = new Quantity<>(left, firstUnit);
                    Quantity<U> second = new Quantity<>(right, secondUnit);
                    Quantity<U> sum = first.add(second, targetUnit);

                    double expectedBase = firstUnit.convertToBaseUnit(left) + secondUnit.convertToBaseUnit(right);
                    double expected = targetUnit.convertFromBaseUnit(expectedBase);
                    assertEquals(expected, sum.getValue(), 1e-2);
                }
            }
        }
    }

    private static String describeQuantity(Quantity<? extends IMeasurable> quantity) {
        return quantity.getUnit().getUnitName();
    }

    private static boolean hasSetter(Class<?> type) {
        for (java.lang.reflect.Method method : type.getDeclaredMethods()) {
            if (method.getName().startsWith("set")) {
                return true;
            }
        }
        return false;
    }

    private static boolean compileSnippet(String className, String source) throws Exception {
        javax.tools.JavaCompiler compiler = javax.tools.ToolProvider.getSystemJavaCompiler();
        assertNotNull(compiler);

        java.nio.file.Path tempDir = java.nio.file.Files.createTempDirectory("quantity-compile-");

        try {
            java.nio.file.Path sourceFile = tempDir.resolve(className + ".java");
            java.nio.file.Files.write(sourceFile, source.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            String classPath = System.getProperty("java.class.path") + java.io.File.pathSeparator
                    + java.nio.file.Paths.get("target", "classes").toAbsolutePath().toString();

            int result = compiler.run(null, null, null, "-classpath", classPath, "-d", tempDir.toString(),
                    sourceFile.toString());
            return result == 0;
        } finally {
            java.nio.file.Files.walk(tempDir)
                    .sorted(java.util.Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            java.nio.file.Files.deleteIfExists(path);
                        } catch (java.io.IOException ignored) {
                        }
                    });
        }
    }

    private static void expectException(Class<? extends Throwable> expectedType, ThrowingRunnable runnable) {
        try {
            runnable.run();
            fail("Expected exception: " + expectedType.getSimpleName());
        } catch (Throwable error) {
            if (!expectedType.isInstance(error)) {
                fail("Expected " + expectedType.getSimpleName() + " but got " + error.getClass().getSimpleName());
            }
        }
    }

    private interface ThrowingRunnable {
        void run() throws Exception;
    }

    private enum TimeScaleUnit implements IMeasurable {
        HOUR(1.0),
        MINUTE(1.0 / 60.0);

        private final double conversionFactor;

        TimeScaleUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        @Override
        public double getConversionFactor() {
            return conversionFactor;
        }
    }

    private enum TemperatureDeltaUnit implements IMeasurable {
        DEGREE(1.0),
        MILLI_DEGREE(0.001);

        private final double conversionFactor;

        TemperatureDeltaUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        @Override
        public double getConversionFactor() {
            return conversionFactor;
        }
    }

    private enum DistanceStepUnit implements IMeasurable {
        KILOSTEP(1.0),
        STEP(0.001);

        private final double conversionFactor;

        DistanceStepUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        @Override
        public double getConversionFactor() {
            return conversionFactor;
        }
    }

    private enum DataUnit implements IMeasurable {
        KILOBYTE(1.0),
        BYTE(0.001);

        private final double conversionFactor;

        DataUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        @Override
        public double getConversionFactor() {
            return conversionFactor;
        }
    }

    private static final class CustomUnit implements IMeasurable {
        private final double conversionFactor;
        private final String unitName;

        private CustomUnit(double conversionFactor, String unitName) {
            this.conversionFactor = conversionFactor;
            this.unitName = unitName;
        }

        @Override
        public double getConversionFactor() {
            return conversionFactor;
        }

        @Override
        public String getUnitName() {
            return unitName;
        }
    }

}