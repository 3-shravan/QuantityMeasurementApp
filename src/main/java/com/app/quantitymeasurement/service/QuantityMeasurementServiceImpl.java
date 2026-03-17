package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.entity.QuantityModel;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.quantity.Quantity;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.unit.IMeasurable;
import com.app.quantitymeasurement.unit.LengthUnit;
import com.app.quantitymeasurement.unit.TemperatureUnit;
import com.app.quantitymeasurement.unit.VolumeUnit;
import com.app.quantitymeasurement.unit.WeightUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class QuantityMeasurementServiceImpl implements QuantityMeasurementService {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);

  private final IQuantityMeasurementRepository repository;

  public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
    if (repository == null) {
      throw new IllegalArgumentException("Repository cannot be null");
    }
    this.repository = repository;
    LOGGER.info("QuantityMeasurementService initialized with repository: {}", repository.getClass().getSimpleName());
  }

  @Override
  public QuantityMeasurementEntity compare(QuantityDTO first, QuantityDTO second) {
    final String operation = "COMPARISON";
    try {
      QuantityModel<IMeasurable> left = toModel(first, "First quantity");
      QuantityModel<IMeasurable> right = toModel(second, "Second quantity");
      validateCompatible(left.getUnit(), right.getUnit());

      boolean result = buildQuantity(left).equals(buildQuantity(right));
      return repository.save(new QuantityMeasurementEntity(first.getValue(), first.getUnit(),
          second.getValue(), second.getUnit(), operation, left.getUnit().getMeasurementType(), String.valueOf(result)));
    } catch (RuntimeException ex) {
      LOGGER.warn("Compare operation failed: {}", ex.getMessage());
      return saveError(operation, first, second, ex);
    }
  }

  @Override
  public QuantityMeasurementEntity convert(QuantityDTO source, String targetUnit) {
    final String operation = "CONVERT";
    try {
      QuantityModel<IMeasurable> from = toModel(source, "Source quantity");
      IMeasurable target = resolve(targetUnit);
      validateCompatible(from.getUnit(), target);

      Quantity<IMeasurable> resultQty = buildQuantity(from).convertTo(target);
      String result = resultQty.getValue() + " " + resultQty.getUnit();
      return repository.save(new QuantityMeasurementEntity(source.getValue(), source.getUnit(),
          null, targetUnit, operation, from.getUnit().getMeasurementType(), result));
    } catch (RuntimeException ex) {
      LOGGER.warn("Convert operation failed: {}", ex.getMessage());
      return saveError(operation, source, null, ex);
    }
  }

  @Override
  public QuantityMeasurementEntity add(QuantityDTO first, QuantityDTO second) {
    return add(first, second, first.getUnit());
  }

  @Override
  public QuantityMeasurementEntity add(QuantityDTO first, QuantityDTO second, String targetUnit) {
    final String operation = "ADD";
    try {
      QuantityModel<IMeasurable> left = toModel(first, "First quantity");
      QuantityModel<IMeasurable> right = toModel(second, "Second quantity");
      IMeasurable target = resolve(targetUnit);
      validateCompatible(left.getUnit(), right.getUnit(), target);

      Quantity<IMeasurable> resultQty = buildQuantity(left).add(buildQuantity(right), target);
      String result = resultQty.getValue() + " " + resultQty.getUnit();
      return repository.save(new QuantityMeasurementEntity(first.getValue(), first.getUnit(),
          second.getValue(), second.getUnit(), operation, left.getUnit().getMeasurementType(), result));
    } catch (RuntimeException ex) {
      LOGGER.warn("Add operation failed: {}", ex.getMessage());
      return saveError(operation, first, second, ex);
    }
  }

  @Override
  public QuantityMeasurementEntity subtract(QuantityDTO first, QuantityDTO second) {
    return subtract(first, second, first.getUnit());
  }

  @Override
  public QuantityMeasurementEntity subtract(QuantityDTO first, QuantityDTO second, String targetUnit) {
    final String operation = "SUBTRACT";
    try {
      QuantityModel<IMeasurable> left = toModel(first, "First quantity");
      QuantityModel<IMeasurable> right = toModel(second, "Second quantity");
      IMeasurable target = resolve(targetUnit);
      validateCompatible(left.getUnit(), right.getUnit(), target);

      Quantity<IMeasurable> resultQty = buildQuantity(left).subtract(buildQuantity(right), target);
      String result = resultQty.getValue() + " " + resultQty.getUnit();
      return repository.save(new QuantityMeasurementEntity(first.getValue(), first.getUnit(),
          second.getValue(), second.getUnit(), operation, left.getUnit().getMeasurementType(), result));
    } catch (RuntimeException ex) {
      LOGGER.warn("Subtract operation failed: {}", ex.getMessage());
      return saveError(operation, first, second, ex);
    }
  }

  @Override
  public QuantityMeasurementEntity divide(QuantityDTO first, QuantityDTO second) {
    final String operation = "DIVIDE";
    try {
      QuantityModel<IMeasurable> left = toModel(first, "First quantity");
      QuantityModel<IMeasurable> right = toModel(second, "Second quantity");
      validateCompatible(left.getUnit(), right.getUnit());

      double value = buildQuantity(left).divide(buildQuantity(right));
      return repository.save(new QuantityMeasurementEntity(first.getValue(), first.getUnit(),
          second.getValue(), second.getUnit(), operation, left.getUnit().getMeasurementType(), String.valueOf(value)));
    } catch (RuntimeException ex) {
      LOGGER.warn("Divide operation failed: {}", ex.getMessage());
      return saveError(operation, first, second, ex);
    }
  }

  @Override
  public List<QuantityMeasurementEntity> getOperationHistory() {
    return repository.findAll();
  }

  private QuantityMeasurementEntity saveError(String operation, QuantityDTO first, QuantityDTO second,
      RuntimeException ex) {
    double firstValue = first != null ? first.getValue() : 0;
    String firstUnit = first != null ? first.getUnit() : null;
    Double secondValue = second != null ? second.getValue() : null;
    String secondUnit = second != null ? second.getUnit() : null;
    return repository.save(new QuantityMeasurementEntity(firstValue, firstUnit, secondValue, secondUnit,
        operation, "UNKNOWN", true, ex.getMessage()));
  }

  private QuantityModel<IMeasurable> toModel(QuantityDTO dto, String label) {
    validateDto(dto, label);
    return new QuantityModel<>(dto.getValue(), resolve(dto.getUnit()));
  }

  private static void validateDto(QuantityDTO dto, String label) {
    if (dto == null) {
      throw new QuantityMeasurementException(label + " cannot be null");
    }
    if (dto.getUnit() == null || dto.getUnit().trim().isEmpty()) {
      throw new QuantityMeasurementException(label + " unit cannot be null or empty");
    }
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
                + units[0].getClass().getSimpleName() + " and " + units[i].getClass().getSimpleName());
      }
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
