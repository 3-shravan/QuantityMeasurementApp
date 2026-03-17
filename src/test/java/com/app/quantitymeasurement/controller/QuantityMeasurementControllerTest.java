package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.repository.QuantityMeasurementDataBaseRepository;
import com.app.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class QuantityMeasurementControllerTest {

  private QuantityMeasurementDataBaseRepository repository;
  private QuantityMeasurementControllerImpl controller;

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
  public void testControllerCompare() {
    assertFalse(controller.performCompare(new QuantityDTO(2, "FEET"), new QuantityDTO(24, "INCHES")).hasError());
    assertEquals(1, repository.getTotalCount());
  }

  @Test
  public void testControllerHistory() {
    controller.performConvert(new QuantityDTO(1, "LITRE"), "MILLILITRE");
    controller.performDivide(new QuantityDTO(2, "FEET"), new QuantityDTO(24, "INCHES"));

    assertEquals(2, controller.getOperationHistory().size());
  }
}
