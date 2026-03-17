package com.app.quantitymeasurement.integrationTests;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.controller.QuantityMeasurementControllerImpl;
import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.repository.QuantityMeasurementDataBaseRepository;
import com.app.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class QuantityMeasurementIntegrationTest {

  private QuantityMeasurementDataBaseRepository repository;
  private QuantityMeasurementController controller;

  @Before
  public void setUp() {
    System.setProperty("app.env", "test");
    repository = new QuantityMeasurementDataBaseRepository();
    repository.deleteAll();
    controller = new QuantityMeasurementControllerImpl(new QuantityMeasurementServiceImpl(repository));
  }

  @After
  public void tearDown() {
    repository.deleteAll();
    repository.releaseResources();
  }

  @Test
  public void testEndToEnd_WithDatabasePersistence() {
    assertFalse(controller.performAdd(new QuantityDTO(2, "FEET"), new QuantityDTO(24, "INCHES")).hasError());
    assertFalse(controller.performSubtract(new QuantityDTO(5, "KILOGRAM"), new QuantityDTO(500, "GRAM")).hasError());
    assertTrue(controller.performAdd(new QuantityDTO(1, "FEET"), new QuantityDTO(1, "KILOGRAM")).hasError());

    assertEquals(3, repository.getTotalCount());
    assertEquals(3, controller.getOperationHistory().size());
  }
}
