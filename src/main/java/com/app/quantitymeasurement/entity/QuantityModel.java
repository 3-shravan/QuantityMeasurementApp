package com.app.quantitymeasurement.entity;

import com.app.quantitymeasurement.unit.IMeasurable;

public class QuantityModel<U extends IMeasurable> {

  private final double value;
  private final U unit;

  public QuantityModel(double value, U unit) {
    this.value = value;
    this.unit = unit;
  }

  public double getValue() {
    return value;
  }

  public U getUnit() {
    return unit;
  }
}
