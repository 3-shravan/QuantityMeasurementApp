package com.app.quantityservice.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuantityInputDTO {

  @Valid
  @NotNull(message = "thisQuantityDTO is required")
  private QuantityDTO thisQuantityDTO;

  @Valid
  @NotNull(message = "thatQuantityDTO is required")
  private QuantityDTO thatQuantityDTO;
}

