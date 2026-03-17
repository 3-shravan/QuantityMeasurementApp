package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public final class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {

  private static volatile QuantityMeasurementCacheRepository instance;

  private final Map<Long, QuantityMeasurementEntity> cache = new LinkedHashMap<>();
  private final AtomicLong idSequence = new AtomicLong(1);

  private QuantityMeasurementCacheRepository() {
  }

  public static QuantityMeasurementCacheRepository getInstance() {
    if (instance == null) {
      synchronized (QuantityMeasurementCacheRepository.class) {
        if (instance == null) {
          instance = new QuantityMeasurementCacheRepository();
        }
      }
    }
    return instance;
  }

  @Override
  public synchronized QuantityMeasurementEntity save(QuantityMeasurementEntity entity) {
    if (entity == null) {
      throw new IllegalArgumentException("Entity cannot be null");
    }
    if (entity.getId() == 0) {
      entity.setId(idSequence.getAndIncrement());
    }
    cache.put(entity.getId(), entity);
    return entity;
  }

  @Override
  public synchronized Optional<QuantityMeasurementEntity> findById(long id) {
    return Optional.ofNullable(cache.get(id));
  }

  @Override
  public synchronized List<QuantityMeasurementEntity> findAll() {
    return new ArrayList<>(cache.values());
  }

  @Override
  public synchronized List<QuantityMeasurementEntity> findByMeasurementType(String measurementType) {
    if (measurementType == null) {
      return new ArrayList<>();
    }
    return cache.values().stream()
        .filter(entity -> measurementType.equalsIgnoreCase(entity.getMeasurementType()))
        .collect(Collectors.toList());
  }

  @Override
  public synchronized List<QuantityMeasurementEntity> findByOperation(String operation) {
    if (operation == null) {
      return new ArrayList<>();
    }
    return cache.values().stream()
        .filter(entity -> operation.equalsIgnoreCase(entity.getOperation()))
        .collect(Collectors.toList());
  }

  @Override
  public synchronized long getTotalCount() {
    return cache.size();
  }

  @Override
  public synchronized void deleteAll() {
    cache.clear();
  }

  @Override
  public synchronized void deleteById(long id) {
    cache.remove(id);
  }
}
