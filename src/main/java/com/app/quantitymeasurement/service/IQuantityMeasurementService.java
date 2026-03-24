package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import java.util.List;

public interface IQuantityMeasurementService {

  QuantityMeasurementDTO compare(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

  QuantityMeasurementDTO convert(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

  QuantityMeasurementDTO add(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

  QuantityMeasurementDTO subtract(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

  QuantityMeasurementDTO divide(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO);

  /**
   * Retrieve the history of quantity measurement operations for a specific
   * operation type.
   *
   * @param operation the type of operation for which to retrieve the history
   *                  (e.g., "conversion", "comparison")
   * @return a list of {@code QuantityMeasurementDTO} representing the history of
   *         operations for the specified type
   */
  List<QuantityMeasurementDTO> getOperationHistory(String operation);

  /**
   * Retrieve the history of quantity measurement operations for a specific
   * measurement type.
   *
   * @param type the measurement type for which to retrieve the history (e.g.,
   *             "length", "weight", "volume", "temperature")
   * @return a list of {@code QuantityMeasurementDTO} representing the history
   *         of operations for the specified type
   */
  List<QuantityMeasurementDTO> getMeasurementsByType(String type);

  /**
   * Get the count of quantity measurement operations for a specific operation
   * type.
   *
   * @param operation the type of operation for which to count operations
   * @return the number of operations of the specified type
   */
  long getOperationCount(String operation);

  /**
   * Retrieve the history of quantity measurement operations that resulted in
   * errors.
   *
   * @return a list of {@code QuantityMeasurementDTO} representing the history of
   *         operations that resulted in errors
   */
  List<QuantityMeasurementDTO> getErrorHistory();
}
