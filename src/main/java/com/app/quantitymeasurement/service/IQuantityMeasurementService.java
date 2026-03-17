package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.List;

public interface IQuantityMeasurementService {

  QuantityMeasurementEntity compare(QuantityDTO first, QuantityDTO second);

  QuantityMeasurementEntity convert(QuantityDTO source, String targetUnit);

  QuantityMeasurementEntity add(QuantityDTO first, QuantityDTO second);

  QuantityMeasurementEntity add(QuantityDTO first, QuantityDTO second, String targetUnit);

  QuantityMeasurementEntity subtract(QuantityDTO first, QuantityDTO second);

  QuantityMeasurementEntity subtract(QuantityDTO first, QuantityDTO second, String targetUnit);

  QuantityMeasurementEntity divide(QuantityDTO first, QuantityDTO second);

  List<QuantityMeasurementEntity> getOperationHistory();
}
