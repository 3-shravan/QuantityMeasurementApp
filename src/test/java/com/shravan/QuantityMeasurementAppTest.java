package com.shravan;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.shravan.QuantityMeasurementApp.Feet;

public class QuantityMeasurementAppTest {

  @Test
  public void testFeetEquality_SameValue() {
    Feet firstValue = new Feet(1.0);
    Feet secondValue = new Feet(1.0);

    assertTrue(firstValue.equals(secondValue));
  }

  @Test
  public void testFeetEquality_DifferentValue() {
    Feet firstValue = new Feet(1.0);
    Feet secondValue = new Feet(2.0);

    assertFalse(firstValue.equals(secondValue));
  }

  @Test
  public void testFeetEquality_NullComparison() {
    Feet value = new Feet(1.0);

    assertFalse(value.equals(null));
  }

  @Test
  public void testFeetEquality_DifferentClass() {
    Feet value = new Feet(1.0);

    assertFalse(value.equals("1.0"));
  }

  @Test
  public void testFeetEquality_SameReference() {
    Feet value = new Feet(1.0);

    assertTrue(value.equals(value));
  }
}