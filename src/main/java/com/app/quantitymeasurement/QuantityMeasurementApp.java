package com.app.quantitymeasurement;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.controller.QuantityMeasurementControllerImpl;
import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementDataBaseRepository;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.app.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import com.app.quantitymeasurement.util.ApplicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class QuantityMeasurementApp {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuantityMeasurementApp.class);

  private final IQuantityMeasurementRepository repository;
  private final QuantityMeasurementController controller;

  public QuantityMeasurementApp() {
    this.repository = createRepository();
    IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repository);
    this.controller = new QuantityMeasurementControllerImpl(service);
  }

  private IQuantityMeasurementRepository createRepository() {
    ApplicationConfig config = new ApplicationConfig();
    String repositoryType = config.get("repository.type", "cache");
    if ("database".equalsIgnoreCase(repositoryType)) {
      LOGGER.info("Initializing JDBC repository implementation");
      return new QuantityMeasurementDataBaseRepository(config);
    }
    LOGGER.info("Initializing in-memory cache repository implementation");
    return QuantityMeasurementCacheRepository.getInstance();
  }

  public void closeResources() {
    repository.releaseResources();
  }

  public void deleteAllMeasurements() {
    repository.deleteAll();
  }

  public static void main(String[] args) {
    QuantityMeasurementApp app = new QuantityMeasurementApp();

    LOGGER.info("=== Quantity Measurement Application - UC16 Demonstration ===");

    // -----------------------------------------------------------------------
    // Example 1: Length Equality Demonstration
    // -----------------------------------------------------------------------
    LOGGER.info("--- Equality Demonstration ---");
    QuantityDTO quantity1 = dto(2, "FEET");
    QuantityDTO quantity2 = dto(24, "INCHES");
    QuantityMeasurementEntity comparisonResult = app.controller.performCompare(quantity1, quantity2);
    LOGGER.info("Operation: {}", comparisonResult.getOperation());
    LOGGER.info("This Quantity: {} {}", comparisonResult.getFirstValue(), comparisonResult.getFirstUnit());
    LOGGER.info("That Quantity: {} {}", comparisonResult.getSecondValue(), comparisonResult.getSecondUnit());
    if (comparisonResult.hasError()) {
      LOGGER.error("✗ Error: {}", comparisonResult.getErrorMessage());
    } else {
      LOGGER.info("Comparison Result: {}", comparisonResult.getResult());
    }

    // -----------------------------------------------------------------------
    // Example 2: Temperature Conversion
    // -----------------------------------------------------------------------
    LOGGER.info("--- Temperature Conversion ---");
    QuantityDTO tempCelsius = dto(0, "CELSIUS");
    QuantityMeasurementEntity conversionResult = app.controller.performConvert(tempCelsius, "FAHRENHEIT");
    LOGGER.info("Operation: {}", conversionResult.getOperation());
    LOGGER.info("This Quantity: {} {}", conversionResult.getFirstValue(), conversionResult.getFirstUnit());
    LOGGER.info("Target Unit: {}", conversionResult.getSecondUnit());
    if (conversionResult.hasError()) {
      LOGGER.error("✗ Error: {}", conversionResult.getErrorMessage());
    } else {
      LOGGER.info("Conversion Result: {}", conversionResult.getResult());
    }

    // -----------------------------------------------------------------------
    // Example 2 (cont.): Temperature Addition Attempt
    // -----------------------------------------------------------------------
    LOGGER.info("--- Addition Demonstration ---");
    QuantityDTO temp1 = dto(0, "CELSIUS");
    QuantityDTO temp2 = dto(32, "FAHRENHEIT");
    QuantityMeasurementEntity tempAddResult = app.controller.performAdd(temp1, temp2, "CELSIUS");
    LOGGER.info("Operation: {}", tempAddResult.getOperation());
    LOGGER.info("This Quantity: {} {}", tempAddResult.getFirstValue(), tempAddResult.getFirstUnit());
    LOGGER.info("That Quantity: {} {}", tempAddResult.getSecondValue(), tempAddResult.getSecondUnit());
    if (tempAddResult.hasError()) {
      LOGGER.error("✗ Error: {}", tempAddResult.getErrorMessage());
    } else {
      LOGGER.info("Addition Result: {}", tempAddResult.getResult());
    }

    // -----------------------------------------------------------------------
    // Example 3: Cross-Category Operation Prevention
    // -----------------------------------------------------------------------
    LOGGER.info("--- Addition Demonstration ---");
    QuantityDTO lengthQty = dto(2, "FEET");
    QuantityDTO weightQty = dto(10, "KILOGRAM");
    QuantityMeasurementEntity crossCategoryResult = app.controller.performAdd(lengthQty, weightQty);
    LOGGER.info("Operation: {}", crossCategoryResult.getOperation());
    LOGGER.info("This Quantity: {} {}", crossCategoryResult.getFirstValue(), crossCategoryResult.getFirstUnit());
    LOGGER.info("That Quantity: {} {}", crossCategoryResult.getSecondValue(), crossCategoryResult.getSecondUnit());
    if (crossCategoryResult.hasError()) {
      LOGGER.error("✗ Error: {}", crossCategoryResult.getErrorMessage());
    } else {
      LOGGER.info("Addition Result: {}", crossCategoryResult.getResult());
    }

    // -----------------------------------------------------------------------
    // Operation History
    // -----------------------------------------------------------------------
    List<QuantityMeasurementEntity> history = app.controller.getOperationHistory();
    LOGGER.info("Total Operations Recorded: {}", history.size());
    LOGGER.info("Repository Stats: {}", app.repository.getPoolStatistics());

    app.deleteAllMeasurements();
    app.closeResources();
  }

  private static QuantityDTO dto(double value, String unit) {
    return new QuantityDTO(value, unit);
  }
}
