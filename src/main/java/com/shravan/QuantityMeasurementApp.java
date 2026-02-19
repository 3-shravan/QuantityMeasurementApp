package com.shravan;

public class QuantityMeasurementApp {

  public boolean areEqual(double firstValueInFeet, double secondValueInFeet) {
    Feet firstFeet = new Feet(firstValueInFeet);
    Feet secondFeet = new Feet(secondValueInFeet);
    return firstFeet.equals(secondFeet);
  }

  public static final class Feet {
    private final double value;

    public Feet(double value) {
      this.value = value;
    }

    public double getValue() {
      return value;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }

      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }

      Feet other = (Feet) obj;
      return Double.compare(this.value, other.value) == 0;
    }
  }
}