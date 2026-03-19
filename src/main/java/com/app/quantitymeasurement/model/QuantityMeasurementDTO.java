package com.app.quantitymeasurement.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuantityMeasurementDTO {

  private Long id;
  private Double thisValue;
  private String thisUnit;
  private String thisMeasurementType;
  private Double thatValue;
  private String thatUnit;
  private String thatMeasurementType;
  private String operation;
  private String resultString;
  private Double resultValue;
  private String resultUnit;
  private String resultMeasurementType;
  private String errorMessage;
  private boolean error;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public QuantityMeasurementDTO() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private final QuantityMeasurementDTO dto = new QuantityMeasurementDTO();

    public Builder id(Long id) {
      dto.setId(id);
      return this;
    }

    public Builder thisValue(Double thisValue) {
      dto.setThisValue(thisValue);
      return this;
    }

    public Builder thisUnit(String thisUnit) {
      dto.setThisUnit(thisUnit);
      return this;
    }

    public Builder thisMeasurementType(String thisMeasurementType) {
      dto.setThisMeasurementType(thisMeasurementType);
      return this;
    }

    public Builder thatValue(Double thatValue) {
      dto.setThatValue(thatValue);
      return this;
    }

    public Builder thatUnit(String thatUnit) {
      dto.setThatUnit(thatUnit);
      return this;
    }

    public Builder thatMeasurementType(String thatMeasurementType) {
      dto.setThatMeasurementType(thatMeasurementType);
      return this;
    }

    public Builder operation(String operation) {
      dto.setOperation(operation);
      return this;
    }

    public Builder resultString(String resultString) {
      dto.setResultString(resultString);
      return this;
    }

    public Builder resultValue(Double resultValue) {
      dto.setResultValue(resultValue);
      return this;
    }

    public Builder resultUnit(String resultUnit) {
      dto.setResultUnit(resultUnit);
      return this;
    }

    public Builder resultMeasurementType(String resultMeasurementType) {
      dto.setResultMeasurementType(resultMeasurementType);
      return this;
    }

    public Builder errorMessage(String errorMessage) {
      dto.setErrorMessage(errorMessage);
      return this;
    }

    public Builder error(boolean error) {
      dto.setError(error);
      return this;
    }

    public Builder createdAt(LocalDateTime createdAt) {
      dto.setCreatedAt(createdAt);
      return this;
    }

    public Builder updatedAt(LocalDateTime updatedAt) {
      dto.setUpdatedAt(updatedAt);
      return this;
    }

    public QuantityMeasurementDTO build() {
      return dto;
    }
  }

  public static QuantityMeasurementDTO fromEntity(QuantityMeasurementEntity entity) {
    if (entity == null) {
      return null;
    }

    return QuantityMeasurementDTO.builder()
        .id(entity.getId())
        .thisValue(entity.getThisValue())
        .thisUnit(entity.getThisUnit())
        .thisMeasurementType(entity.getThisMeasurementType())
        .thatValue(entity.getThatValue())
        .thatUnit(entity.getThatUnit())
        .thatMeasurementType(entity.getThatMeasurementType())
        .operation(entity.getOperation())
        .resultString(entity.getResultString())
        .resultValue(entity.getResultValue())
        .resultUnit(entity.getResultUnit())
        .resultMeasurementType(entity.getResultMeasurementType())
        .errorMessage(entity.getErrorMessage())
        .error(entity.isError())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }

  public QuantityMeasurementEntity toEntity() {
    QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
    entity.setId(this.id);
    entity.setThisValue(this.thisValue);
    entity.setThisUnit(this.thisUnit);
    entity.setThisMeasurementType(this.thisMeasurementType);
    entity.setThatValue(this.thatValue);
    entity.setThatUnit(this.thatUnit);
    entity.setThatMeasurementType(this.thatMeasurementType);
    entity.setOperation(this.operation);
    entity.setResultString(this.resultString);
    entity.setResultValue(this.resultValue);
    entity.setResultUnit(this.resultUnit);
    entity.setResultMeasurementType(this.resultMeasurementType);
    entity.setErrorMessage(this.errorMessage);
    entity.setError(this.error);
    entity.setCreatedAt(this.createdAt);
    entity.setUpdatedAt(this.updatedAt);
    return entity;
  }

  public static List<QuantityMeasurementDTO> fromEntityList(List<QuantityMeasurementEntity> entities) {
    List<QuantityMeasurementDTO> dtos = new ArrayList<>();
    for (QuantityMeasurementEntity entity : entities) {
      dtos.add(fromEntity(entity));
    }
    return dtos;
  }

  public static List<QuantityMeasurementEntity> toEntityList(List<QuantityMeasurementDTO> dtos) {
    List<QuantityMeasurementEntity> entities = new ArrayList<>();
    for (QuantityMeasurementDTO dto : dtos) {
      entities.add(dto.toEntity());
    }
    return entities;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getThisValue() {
    return thisValue;
  }

  public void setThisValue(Double thisValue) {
    this.thisValue = thisValue;
  }

  public String getThisUnit() {
    return thisUnit;
  }

  public void setThisUnit(String thisUnit) {
    this.thisUnit = thisUnit;
  }

  public String getThisMeasurementType() {
    return thisMeasurementType;
  }

  public void setThisMeasurementType(String thisMeasurementType) {
    this.thisMeasurementType = thisMeasurementType;
  }

  public Double getThatValue() {
    return thatValue;
  }

  public void setThatValue(Double thatValue) {
    this.thatValue = thatValue;
  }

  public String getThatUnit() {
    return thatUnit;
  }

  public void setThatUnit(String thatUnit) {
    this.thatUnit = thatUnit;
  }

  public String getThatMeasurementType() {
    return thatMeasurementType;
  }

  public void setThatMeasurementType(String thatMeasurementType) {
    this.thatMeasurementType = thatMeasurementType;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public String getResultString() {
    return resultString;
  }

  public void setResultString(String resultString) {
    this.resultString = resultString;
  }

  public Double getResultValue() {
    return resultValue;
  }

  public void setResultValue(Double resultValue) {
    this.resultValue = resultValue;
  }

  public String getResultUnit() {
    return resultUnit;
  }

  public void setResultUnit(String resultUnit) {
    this.resultUnit = resultUnit;
  }

  public String getResultMeasurementType() {
    return resultMeasurementType;
  }

  public void setResultMeasurementType(String resultMeasurementType) {
    this.resultMeasurementType = resultMeasurementType;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public boolean isError() {
    return error;
  }

  public void setError(boolean error) {
    this.error = error;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
