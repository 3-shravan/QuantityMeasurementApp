package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.List;
import java.util.Optional;

public interface IQuantityMeasurementRepository {

  QuantityMeasurementEntity save(QuantityMeasurementEntity entity);

  Optional<QuantityMeasurementEntity> findById(long id);

  List<QuantityMeasurementEntity> findAll();

  List<QuantityMeasurementEntity> findByMeasurementType(String measurementType);

  void deleteById(long id);
}
