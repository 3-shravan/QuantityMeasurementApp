package com.app.quantityservice.service;

import com.app.quantityservice.domain.quantity.Quantity;
import com.app.quantityservice.exception.QuantityMeasurementException;
import com.app.quantityservice.model.OperationType;
import com.app.quantityservice.model.QuantityDTO;
import com.app.quantityservice.model.QuantityMeasurementDTO;
import com.app.quantityservice.model.QuantityMeasurementEntity;
import com.app.quantityservice.model.QuantityModel;
import com.app.quantityservice.repository.QuantityMeasurementRepository;
import com.app.quantityservice.security.SecurityUtil;
import com.app.quantityservice.unit.IMeasurable;
import com.app.quantityservice.unit.LengthUnit;
import com.app.quantityservice.unit.TemperatureUnit;
import com.app.quantityservice.unit.VolumeUnit;
import com.app.quantityservice.unit.WeightUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);

  private final QuantityMeasurementRepository repository;
  private final SecurityUtil securityUtil;

  @Override
  public QuantityMeasurementDTO compare(QuantityDTO first, QuantityDTO second) {
    OperationType operation = OperationType.COMPARE;
    try {
      QuantityModel<IMeasurable> left = convertDtoToModel(first, "First quantity");
      QuantityModel<IMeasurable> right = convertDtoToModel(second, "Second quantity");
      validateCompatible(left.getUnit(), right.getUnit());

      boolean result = buildQuantity(left).equals(buildQuantity(right));
      return persistSuccess(operation, first, second, String.valueOf(result), null, null, null);
    } catch (RuntimeException ex) {
      saveError(operation, first, second, ex.getMessage());
      throw new QuantityMeasurementException("compare Error: " + ex.getMessage(), ex);
    }
  }

  @Override
  public QuantityMeasurementDTO convert(QuantityDTO source, QuantityDTO target) {
    OperationType operation = OperationType.CONVERT;
    try {
      QuantityModel<IMeasurable> from = convertDtoToModel(source, "Source quantity");
      QuantityModel<IMeasurable> targetModel = convertDtoToModel(target, "Target quantity");
      validateCompatible(from.getUnit(), targetModel.getUnit());

      Quantity<IMeasurable> result = buildQuantity(from).convertTo(targetModel.getUnit());
      return persistSuccess(operation, source, target, null, result.getValue(), null, null);
    } catch (RuntimeException ex) {
      saveError(operation, source, target, ex.getMessage());
      throw new QuantityMeasurementException("convert Error: " + ex.getMessage(), ex);
    }
  }

  @Override
  public QuantityMeasurementDTO add(QuantityDTO first, QuantityDTO second) {
    OperationType operation = OperationType.ADD;
    try {
      QuantityModel<IMeasurable> left = convertDtoToModel(first, "First quantity");
      QuantityModel<IMeasurable> right = convertDtoToModel(second, "Second quantity");
      validateCompatible(left.getUnit(), right.getUnit());

      Quantity<IMeasurable> result = buildQuantity(left).add(buildQuantity(right), left.getUnit());
      return persistSuccess(operation, first, second, null, result.getValue(), left.getUnit().toString(),
          left.getUnit().getMeasurementType());
    } catch (RuntimeException ex) {
      saveError(operation, first, second, ex.getMessage());
      throw new QuantityMeasurementException("add Error: " + ex.getMessage(), ex);
    }
  }

  @Override
  public QuantityMeasurementDTO subtract(QuantityDTO first, QuantityDTO second) {
    OperationType operation = OperationType.SUBTRACT;
    try {
      QuantityModel<IMeasurable> left = convertDtoToModel(first, "First quantity");
      QuantityModel<IMeasurable> right = convertDtoToModel(second, "Second quantity");
      validateCompatible(left.getUnit(), right.getUnit());

      Quantity<IMeasurable> result = buildQuantity(left).subtract(buildQuantity(right), left.getUnit());
      return persistSuccess(operation, first, second, null, result.getValue(), left.getUnit().toString(),
          left.getUnit().getMeasurementType());
    } catch (RuntimeException ex) {
      saveError(operation, first, second, ex.getMessage());
      throw new QuantityMeasurementException("subtract Error: " + ex.getMessage(), ex);
    }
  }

  @Override
  public QuantityMeasurementDTO divide(QuantityDTO first, QuantityDTO second) {
    OperationType operation = OperationType.DIVIDE;
    try {
      QuantityModel<IMeasurable> left = convertDtoToModel(first, "First quantity");
      QuantityModel<IMeasurable> right = convertDtoToModel(second, "Second quantity");
      validateCompatible(left.getUnit(), right.getUnit());

      double result = buildQuantity(left).divide(buildQuantity(right));
      return persistSuccess(operation, first, second, null, result, null, null);
    } catch (ArithmeticException ex) {
      saveError(operation, first, second, "Divide by zero");
      throw new RuntimeException("Divide by zero", ex);
    } catch (RuntimeException ex) {
      saveError(operation, first, second, ex.getMessage());
      throw new QuantityMeasurementException("divide Error: " + ex.getMessage(), ex);
    }
  }

  @Override
  public List<QuantityMeasurementDTO> getOperationHistory(String operation) {
    String username = securityUtil.getCurrentUsername();
    if (username == null) {
      return List.of();
    }
    return QuantityMeasurementDTO.fromEntityList(repository.findByOperationAndUsername(operation, username));
  }

  @Override
  public List<QuantityMeasurementDTO> getMeasurementsByType(String measurementType) {
    String username = securityUtil.getCurrentUsername();
    if (username == null) {
      return List.of();
    }
    return QuantityMeasurementDTO.fromEntityList(
        repository.findByThisMeasurementTypeAndUsername(measurementType, username));
  }

  @Override
  public long getOperationCount(String operation) {
    String username = securityUtil.getCurrentUsername();
    if (username == null) {
      return 0L;
    }
    return repository.countByOperationAndErrorFalseAndUsername(operation.toLowerCase(), username);
  }

  @Override
  public List<QuantityMeasurementDTO> getErrorHistory() {
    String username = securityUtil.getCurrentUsername();
    if (username == null) {
      return List.of();
    }
    return QuantityMeasurementDTO.fromEntityList(repository.findByErrorTrueAndUsername(username));
  }

  private QuantityMeasurementDTO persistSuccess(OperationType operation, QuantityDTO first, QuantityDTO second,
      String resultString, Double resultValue, String resultUnit, String resultMeasurementType) {
    QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
    entity.setThisValue(first.getValue());
    entity.setThisUnit(first.getUnit());
    entity.setThisMeasurementType(first.getMeasurementType());
    entity.setThatValue(second != null ? second.getValue() : null);
    entity.setThatUnit(second != null ? second.getUnit() : null);
    entity.setThatMeasurementType(second != null ? second.getMeasurementType() : null);
    entity.setOperation(operation.name().toLowerCase());
    entity.setResultString(resultString);
    entity.setResultValue(resultValue);
    entity.setResultUnit(resultUnit);
    entity.setResultMeasurementType(resultMeasurementType);
    entity.setError(false);
    entity.setUsername(securityUtil.getCurrentUsername());
    return QuantityMeasurementDTO.fromEntity(repository.save(entity));
  }

  private void saveError(OperationType operation, QuantityDTO first, QuantityDTO second, String errorMessage) {
    LOGGER.warn("{} operation failed: {}", operation, errorMessage);
    QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
    entity.setThisValue(first != null ? first.getValue() : 0.0);
    entity.setThisUnit(first != null ? first.getUnit() : null);
    entity.setThisMeasurementType(first != null ? first.getMeasurementType() : "Unknown");
    entity.setThatValue(second != null ? second.getValue() : null);
    entity.setThatUnit(second != null ? second.getUnit() : null);
    entity.setThatMeasurementType(second != null ? second.getMeasurementType() : null);
    entity.setOperation(operation.name().toLowerCase());
    entity.setError(true);
    entity.setErrorMessage(errorMessage);
    entity.setUsername(securityUtil.getCurrentUsername());
    repository.save(entity);
  }

  private QuantityModel<IMeasurable> convertDtoToModel(QuantityDTO dto, String label) {
    validateDto(dto, label);
    return new QuantityModel<>(dto.getValue(), resolve(dto.getUnit()));
  }

  private static Quantity<IMeasurable> buildQuantity(QuantityModel<IMeasurable> model) {
    return new Quantity<>(model.getValue(), model.getUnit());
  }

  private static void validateCompatible(IMeasurable... units) {
    String category = units[0].getMeasurementType();
    for (int i = 1; i < units.length; i++) {
      if (!category.equals(units[i].getMeasurementType())) {
        throw new QuantityMeasurementException(
            "Cannot perform arithmetic between different measurement categories: "
                + units[0].getMeasurementType() + " and " + units[i].getMeasurementType());
      }
    }
  }

  private static void validateDto(QuantityDTO dto, String label) {
    if (dto == null) {
      throw new QuantityMeasurementException(label + " cannot be null");
    }
    if (dto.getUnit() == null || dto.getUnit().trim().isEmpty()) {
      throw new QuantityMeasurementException(label + " unit cannot be null or empty");
    }
    if (dto.getMeasurementType() == null || dto.getMeasurementType().trim().isEmpty()) {
      throw new QuantityMeasurementException(label + " measurementType cannot be null or empty");
    }
  }

  private static IMeasurable resolve(String unitName) {
    if (unitName == null || unitName.trim().isEmpty()) {
      throw new QuantityMeasurementException("Unit cannot be null or empty");
    }

    String key = unitName.trim().toUpperCase();
    try {
      return LengthUnit.valueOf(key);
    } catch (IllegalArgumentException ignored) {
    }
    try {
      return WeightUnit.valueOf(key);
    } catch (IllegalArgumentException ignored) {
    }
    try {
      return VolumeUnit.valueOf(key);
    } catch (IllegalArgumentException ignored) {
    }
    try {
      return TemperatureUnit.valueOf(key);
    } catch (IllegalArgumentException ignored) {
    }
    throw new QuantityMeasurementException("Unknown unit: " + unitName);
  }
}

