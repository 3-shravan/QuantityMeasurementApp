package com.shravan;

public class QuantityMeasurementApp {

  public boolean areFeetEqual(double firstValueInFeet, double secondValueInFeet) {
    Feet firstFeet = new Feet(firstValueInFeet);
    Feet secondFeet = new Feet(secondValueInFeet);
    return firstFeet.equals(secondFeet);
  }

  public boolean areInchesEqual(double firstValueInInches, double secondValueInInches) {
    Inches firstInches = new Inches(firstValueInInches);
    Inches secondInches = new Inches(secondValueInInches);
    return firstInches.equals(secondInches);
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

  public static void main(String[] args) {
    demonstrateFeetEquality();
    demonstrateInchesEquality();
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

  public static final class Inches {
    private final double value;

    public Inches(double value) {
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

      Inches other = (Inches) obj;
      return Double.compare(this.value, other.value) == 0;
    }
  }
}