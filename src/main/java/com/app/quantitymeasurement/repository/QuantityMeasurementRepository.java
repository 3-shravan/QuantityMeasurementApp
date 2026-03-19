package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {

  List<QuantityMeasurementEntity> findByOperation(String operation);

  List<QuantityMeasurementEntity> findByThisMeasurementType(String measurementType);

  List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime date);

  @Query("SELECT q FROM QuantityMeasurementEntity q WHERE q.operation = ?1 AND q.error = false")
  List<QuantityMeasurementEntity> findSuccessfulByOperation(String operation);

  long countByOperationAndErrorFalse(String operation);

  List<QuantityMeasurementEntity> findByErrorTrue();
}
