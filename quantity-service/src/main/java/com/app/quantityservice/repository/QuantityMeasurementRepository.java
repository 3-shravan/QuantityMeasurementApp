package com.app.quantityservice.repository;

import com.app.quantityservice.model.QuantityMeasurementEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {

  List<QuantityMeasurementEntity> findByOperationAndUsername(String operation, String username);

  List<QuantityMeasurementEntity> findByThisMeasurementTypeAndUsername(String measurementType, String username);

  @Query("SELECT q FROM QuantityMeasurementEntity q WHERE q.error = true AND q.username = ?1")
  List<QuantityMeasurementEntity> findByErrorTrueAndUsername(String username);

  long countByOperationAndErrorFalseAndUsername(String operation, String username);
}

