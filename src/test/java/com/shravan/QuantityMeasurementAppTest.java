package com.shravan;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.shravan.Length.LengthUnit;

public class QuantityMeasurementAppTest {

    @Test
    public void testEquality_FeetToFeet_SameValue() {
        Length firstValue = new Length(1.0, LengthUnit.FEET);
        Length secondValue = new Length(1.0, LengthUnit.FEET);

        assertTrue(firstValue.equals(secondValue));
    }

    @Test
    public void testEquality_InchToInch_SameValue() {
        Length firstValue = new Length(1.0, LengthUnit.INCHES);
        Length secondValue = new Length(1.0, LengthUnit.INCHES);

        assertTrue(firstValue.equals(secondValue));
    }

    @Test
    public void testEquality_FeetToInch_EquivalentValue() {
        Length feet = new Length(1.0, LengthUnit.FEET);
        Length inches = new Length(12.0, LengthUnit.INCHES);

        assertTrue(feet.equals(inches));
    }

    @Test
    public void testEquality_InchToFeet_EquivalentValue() {
        Length inches = new Length(12.0, LengthUnit.INCHES);
        Length feet = new Length(1.0, LengthUnit.FEET);

        assertTrue(inches.equals(feet));
    }

    @Test
    public void testEquality_FeetToFeet_DifferentValue() {
        Length firstValue = new Length(1.0, LengthUnit.FEET);
        Length secondValue = new Length(2.0, LengthUnit.FEET);

        assertFalse(firstValue.equals(secondValue));
    }

    @Test
    public void testEquality_InchToInch_DifferentValue() {
        Length firstValue = new Length(1.0, LengthUnit.INCHES);
        Length secondValue = new Length(2.0, LengthUnit.INCHES);

        assertFalse(firstValue.equals(secondValue));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEquality_InvalidUnit() {
        LengthUnit.from("meter");
    }

    @Test(expected = NullPointerException.class)
    public void testEquality_NullUnit() {
        new Length(1.0, null);
    }

    @Test
    public void testEquality_SameReference() {
        Length value = new Length(1.0, LengthUnit.FEET);

        assertTrue(value.equals(value));
    }

    @Test
    public void testEquality_NullComparison() {
        Length value = new Length(1.0, LengthUnit.FEET);

        assertFalse(value.equals(null));
    }

    @Test
    public void testEquality_DifferentClass() {
        Length value = new Length(1.0, LengthUnit.FEET);

        assertFalse(value.equals("1.0"));
    }

    @Test
    public void testEquality_Transitive() {
        Length a = new Length(1.0, LengthUnit.FEET);
        Length b = new Length(12.0, LengthUnit.INCHES);
        Length c = new Length(1.0, LengthUnit.FEET);

        assertTrue(a.equals(b));
        assertTrue(b.equals(c));
        assertTrue(a.equals(c));
    }

    @Test
    public void testHashCode_EqualObjectsHaveSameHashCode() {
        Length a = new Length(1.0, LengthUnit.FEET);
        Length b = new Length(12.0, LengthUnit.INCHES);

        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testBackwardCompatibility_AppMethodsStillWork() {
        QuantityMeasurementApp app = new QuantityMeasurementApp();

        assertTrue(app.areFeetEqual(1.0, 1.0));
        assertTrue(app.areInchesEqual(12.0, 12.0));
        assertTrue(app.areLengthsEqual(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCHES));
    }
}