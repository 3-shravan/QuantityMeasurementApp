package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.DatabaseException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Placeholder DB repository for future persistence integration.
 */
public class QuantityMeasurementDataBaseRepository implements IQuantityMeasurementRepository {

  @Override
  public QuantityMeasurementEntity save(QuantityMeasurementEntity entity) {
    throw new DatabaseException("Database repository not configured");
  }

  @Override
  public Optional<QuantityMeasurementEntity> findById(long id) {
    return Optional.empty();
  }

  @Override
  public List<QuantityMeasurementEntity> findAll() {
    return Collections.emptyList();
  }

  @Override
  public List<QuantityMeasurementEntity> findByMeasurementType(String measurementType) {
    return Collections.emptyList();
  }

  @Override
  public void deleteById(long id) {
    throw new DatabaseException("Database repository not configured");
  }
}
