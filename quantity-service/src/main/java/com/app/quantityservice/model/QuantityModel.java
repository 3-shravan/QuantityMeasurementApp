package com.app.quantityservice.model;

import com.app.quantityservice.unit.IMeasurable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuantityModel<U extends IMeasurable> {

  private final double value;
  private final U unit;
}

