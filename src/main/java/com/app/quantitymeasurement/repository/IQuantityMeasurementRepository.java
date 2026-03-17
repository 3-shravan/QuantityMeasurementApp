package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.List;
import java.util.Optional;

public interface IQuantityMeasurementRepository {

  QuantityMeasurementEntity save(QuantityMeasurementEntity entity);

  Optional<QuantityMeasurementEntity> findById(long id);

  List<QuantityMeasurementEntity> findAll();

  List<QuantityMeasurementEntity> findByMeasurementType(String measurementType);

  List<QuantityMeasurementEntity> findByOperation(String operation);

  long getTotalCount();

  void deleteAll();

  void deleteById(long id);

  default String getPoolStatistics() {
    return "Pooling not supported";
  }

  default void releaseResources() {
    // no-op for repositories without external resources
  }
}
