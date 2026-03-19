package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.model.OperationType;
import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import java.util.List;

public interface IQuantityMeasurementService {

  QuantityMeasurementDTO compareQuantities(QuantityDTO first, QuantityDTO second);

  QuantityMeasurementDTO convertQuantity(QuantityDTO source, QuantityDTO target);

  QuantityMeasurementDTO addQuantities(QuantityDTO first, QuantityDTO second);

  QuantityMeasurementDTO subtractQuantities(QuantityDTO first, QuantityDTO second);

  QuantityMeasurementDTO divideQuantities(QuantityDTO first, QuantityDTO second);

  List<QuantityMeasurementDTO> getOperationHistory(OperationType operationType);

  List<QuantityMeasurementDTO> getMeasurementTypeHistory(String measurementType);

  long getOperationCount(OperationType operationType);

  List<QuantityMeasurementDTO> getErroredHistory();
}
