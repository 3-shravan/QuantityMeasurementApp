package com.app.quantityservice.application.port;

import com.app.quantityservice.model.QuantityDTO;
import com.app.quantityservice.model.QuantityMeasurementDTO;
import java.util.List;

public interface QuantityApplicationService {

  QuantityMeasurementDTO compare(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

  QuantityMeasurementDTO convert(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

  QuantityMeasurementDTO add(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

  QuantityMeasurementDTO subtract(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

  QuantityMeasurementDTO divide(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

  List<QuantityMeasurementDTO> getOperationHistory(String operation);

  List<QuantityMeasurementDTO> getMeasurementsByType(String type);

  long getOperationCount(String operation);

  List<QuantityMeasurementDTO> getErrorHistory();
}

