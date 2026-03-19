package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.model.OperationType;
import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.model.QuantityModel;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.quantity.Quantity;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.unit.IMeasurable;
import com.app.quantitymeasurement.unit.LengthUnit;
import com.app.quantitymeasurement.unit.TemperatureUnit;
import com.app.quantitymeasurement.unit.VolumeUnit;
import com.app.quantitymeasurement.unit.WeightUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class QuantityMeasurementServiceImpl implements QuantityMeasurementService {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);

  @Autowired
  private QuantityMeasurementRepository repository;

  @Override
  public QuantityMeasurementDTO compareQuantities(QuantityDTO first, QuantityDTO second) {
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
  public QuantityMeasurementDTO convertQuantity(QuantityDTO source, QuantityDTO target) {
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
  public QuantityMeasurementDTO addQuantities(QuantityDTO first, QuantityDTO second) {
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
  public QuantityMeasurementDTO subtractQuantities(QuantityDTO first, QuantityDTO second) {
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
  public QuantityMeasurementDTO divideQuantities(QuantityDTO first, QuantityDTO second) {
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
  public List<QuantityMeasurementDTO> getOperationHistory(OperationType operationType) {
    return QuantityMeasurementDTO.fromEntityList(repository.findByOperation(operationType.name().toLowerCase()));
  }

  @Override
  public List<QuantityMeasurementDTO> getMeasurementTypeHistory(String measurementType) {
    return QuantityMeasurementDTO.fromEntityList(repository.findByThisMeasurementType(measurementType));
  }

  @Override
  public long getOperationCount(OperationType operationType) {
    return repository.countByOperationAndErrorFalse(operationType.name().toLowerCase());
  }

  @Override
  public List<QuantityMeasurementDTO> getErroredHistory() {
    return QuantityMeasurementDTO.fromEntityList(repository.findByErrorTrue());
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
