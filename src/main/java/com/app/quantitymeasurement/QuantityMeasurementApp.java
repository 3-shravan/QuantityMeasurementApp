package com.app.quantitymeasurement;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.controller.QuantityMeasurementControllerImpl;
import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.app.quantitymeasurement.service.QuantityMeasurementServiceImpl;

import java.util.List;

public class QuantityMeasurementApp {

  public static void main(String[] args) {
    IQuantityMeasurementRepository repository = QuantityMeasurementCacheRepository.getInstance();
    IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repository);
    QuantityMeasurementController controller = new QuantityMeasurementControllerImpl(service);

    System.out.println("=== Quantity Measurement Application - UC15 Demonstration ===");
    System.out.println();

    // -----------------------------------------------------------------------
    // Example 1: Length Equality Demonstration
    // -----------------------------------------------------------------------
    System.out.println("--- Equality Demonstration ---");
    QuantityDTO quantity1 = dto(2, "FEET");
    QuantityDTO quantity2 = dto(24, "INCHES");
    QuantityMeasurementEntity comparisonResult = controller.performCompare(quantity1, quantity2);
    System.out.println("Operation: " + comparisonResult.getOperation());
    System.out.println("This Quantity: " + comparisonResult.getFirstValue() + " " + comparisonResult.getFirstUnit());
    System.out.println("That Quantity: " + comparisonResult.getSecondValue() + " " + comparisonResult.getSecondUnit());
    if (comparisonResult.hasError()) {
      System.out.println("\u2717 Error: " + comparisonResult.getErrorMessage());
    } else {
      System.out.println("Comparison Result: " + comparisonResult.getResult());
    }
    System.out.println();

    // -----------------------------------------------------------------------
    // Example 2: Temperature Conversion
    // -----------------------------------------------------------------------
    System.out.println("--- Temperature Conversion ---");
    QuantityDTO tempCelsius = dto(0, "CELSIUS");
    QuantityMeasurementEntity conversionResult = controller.performConvert(tempCelsius, "FAHRENHEIT");
    System.out.println("Operation: " + conversionResult.getOperation());
    System.out.println("This Quantity: " + conversionResult.getFirstValue() + " " + conversionResult.getFirstUnit());
    System.out.println("Target Unit: " + conversionResult.getSecondUnit());
    if (conversionResult.hasError()) {
      System.out.println("\u2717 Error: " + conversionResult.getErrorMessage());
    } else {
      System.out.println("Conversion Result: " + conversionResult.getResult());
    }
    System.out.println();

    // -----------------------------------------------------------------------
    // Example 2 (cont.): Temperature Addition Attempt
    // -----------------------------------------------------------------------
    System.out.println("--- Addition Demonstration ---");
    QuantityDTO temp1 = dto(0, "CELSIUS");
    QuantityDTO temp2 = dto(32, "FAHRENHEIT");
    QuantityMeasurementEntity tempAddResult = controller.performAdd(temp1, temp2, "CELSIUS");
    System.out.println("Operation: " + tempAddResult.getOperation());
    System.out.println("This Quantity: " + tempAddResult.getFirstValue() + " " + tempAddResult.getFirstUnit());
    System.out.println("That Quantity: " + tempAddResult.getSecondValue() + " " + tempAddResult.getSecondUnit());
    if (tempAddResult.hasError()) {
      System.out.println("\u2717 Error: " + tempAddResult.getErrorMessage());
    } else {
      System.out.println("Addition Result: " + tempAddResult.getResult());
    }
    System.out.println();

    // -----------------------------------------------------------------------
    // Example 3: Cross-Category Operation Prevention
    // -----------------------------------------------------------------------
    System.out.println("--- Addition Demonstration ---");
    QuantityDTO lengthQty = dto(2, "FEET");
    QuantityDTO weightQty = dto(10, "KILOGRAM");
    QuantityMeasurementEntity crossCategoryResult = controller.performAdd(lengthQty, weightQty);
    System.out.println("Operation: " + crossCategoryResult.getOperation());
    System.out
        .println("This Quantity: " + crossCategoryResult.getFirstValue() + " " + crossCategoryResult.getFirstUnit());
    System.out
        .println("That Quantity: " + crossCategoryResult.getSecondValue() + " " + crossCategoryResult.getSecondUnit());
    if (crossCategoryResult.hasError()) {
      System.out.println("\u2717 Error: " + crossCategoryResult.getErrorMessage());
    } else {
      System.out.println("Addition Result: " + crossCategoryResult.getResult());
    }
    System.out.println();

    // -----------------------------------------------------------------------
    // Operation History
    // -----------------------------------------------------------------------
    List<QuantityMeasurementEntity> history = controller.getOperationHistory();
    System.out.println("Total Operations Recorded: " + history.size());
  }

  private static QuantityDTO dto(double value, String unit) {
    return new QuantityDTO(value, unit);
  }
}
