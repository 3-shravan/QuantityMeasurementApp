package com.app.quantityservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "quantity_measurement_entity", indexes = {
    @Index(name = "idx_qme_operation", columnList = "operation"),
    @Index(name = "idx_qme_this_measurement_type", columnList = "this_measurement_type"),
    @Index(name = "idx_qme_created_at", columnList = "created_at"),
    @Index(name = "idx_qme_username", columnList = "username")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuantityMeasurementEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "this_value", nullable = false)
  private Double thisValue;

  @Column(name = "this_unit", nullable = false, length = 64)
  private String thisUnit;

  @Column(name = "this_measurement_type", nullable = false, length = 64)
  private String thisMeasurementType;

  @Column(name = "that_value")
  private Double thatValue;

  @Column(name = "that_unit", length = 64)
  private String thatUnit;

  @Column(name = "that_measurement_type", length = 64)
  private String thatMeasurementType;

  @Column(name = "operation", nullable = false, length = 32)
  private String operation;

  @Column(name = "result_string")
  private String resultString;

  @Column(name = "result_value")
  private Double resultValue;

  @Column(name = "result_unit", length = 64)
  private String resultUnit;

  @Column(name = "result_measurement_type", length = 64)
  private String resultMeasurementType;

  @Column(name = "error_message", length = 500)
  private String errorMessage;

  @Column(name = "is_error", nullable = false)
  private boolean error;

  @Column(name = "username", length = 255)
  private String username;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDateTime.now();
  }
}

