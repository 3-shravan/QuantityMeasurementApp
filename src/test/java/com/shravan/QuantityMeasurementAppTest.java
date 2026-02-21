package com.shravan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.shravan.Length.LengthUnit;

public class QuantityMeasurementAppTest {

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
}