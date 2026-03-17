package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.util.ApplicationConfig;

public final class QuantityMeasurementRepositoryFactory {

  private QuantityMeasurementRepositoryFactory() {
  }

  public static IQuantityMeasurementRepository createRepository(ApplicationConfig config) {
    String repositoryType = config.get("repository.type", "cache");
    if ("database".equalsIgnoreCase(repositoryType)) {
      return new QuantityMeasurementDataBaseRepository(config);
    }
    return QuantityMeasurementCacheRepository.getInstance();
  }
}
