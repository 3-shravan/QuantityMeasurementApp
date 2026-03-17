package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;

import java.util.List;

public class QuantityMeasurementControllerImpl implements QuantityMeasurementController {

  private final IQuantityMeasurementService service;

  public QuantityMeasurementControllerImpl(IQuantityMeasurementService service) {
    if (service == null) {
      throw new IllegalArgumentException("Service cannot be null");
    }
    this.service = service;
  }

  @Override
  public QuantityMeasurementEntity performCompare(QuantityDTO first, QuantityDTO second) {
    return service.compare(first, second);
  }

  @Override
  public QuantityMeasurementEntity performConvert(QuantityDTO source, String targetUnit) {
    return service.convert(source, targetUnit);
  }

  @Override
  public QuantityMeasurementEntity performAdd(QuantityDTO first, QuantityDTO second) {
    return service.add(first, second);
  }

  @Override
  public QuantityMeasurementEntity performAdd(QuantityDTO first, QuantityDTO second, String targetUnit) {
    return service.add(first, second, targetUnit);
  }

  @Override
  public QuantityMeasurementEntity performSubtract(QuantityDTO first, QuantityDTO second) {
    return service.subtract(first, second);
  }

  @Override
  public QuantityMeasurementEntity performSubtract(QuantityDTO first, QuantityDTO second, String targetUnit) {
    return service.subtract(first, second, targetUnit);
  }

  @Override
  public QuantityMeasurementEntity performDivide(QuantityDTO first, QuantityDTO second) {
    return service.divide(first, second);
  }

  @Override
  public List<QuantityMeasurementEntity> getOperationHistory() {
    return service.getOperationHistory();
  }
}
