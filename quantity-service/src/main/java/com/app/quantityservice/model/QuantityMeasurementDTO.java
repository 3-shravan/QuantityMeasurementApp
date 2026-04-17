package com.app.quantityservice.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
        .build();
  }

  public static List<QuantityMeasurementDTO> fromEntityList(List<QuantityMeasurementEntity> entities) {
    List<QuantityMeasurementDTO> dtos = new ArrayList<>();
    for (QuantityMeasurementEntity entity : entities) {
      dtos.add(fromEntity(entity));
    }
    return dtos;
  }
}

