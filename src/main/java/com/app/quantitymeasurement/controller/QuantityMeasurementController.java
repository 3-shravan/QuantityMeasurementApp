package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.List;

public interface QuantityMeasurementController {

  QuantityMeasurementEntity performCompare(QuantityDTO first, QuantityDTO second);

  QuantityMeasurementEntity performConvert(QuantityDTO source, String targetUnit);

  QuantityMeasurementEntity performAdd(QuantityDTO first, QuantityDTO second);

  QuantityMeasurementEntity performAdd(QuantityDTO first, QuantityDTO second, String targetUnit);

  QuantityMeasurementEntity performSubtract(QuantityDTO first, QuantityDTO second);

  QuantityMeasurementEntity performSubtract(QuantityDTO first, QuantityDTO second, String targetUnit);

  QuantityMeasurementEntity performDivide(QuantityDTO first, QuantityDTO second);

  List<QuantityMeasurementEntity> getOperationHistory();
}
