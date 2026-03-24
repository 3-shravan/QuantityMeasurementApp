package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.QuantityMeasurementDataBaseRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class QuantityMeasurementServiceTest {

  private QuantityMeasurementDataBaseRepository repository;
  private QuantityMeasurementServiceImpl service;

  @Before
  public void setUp() {
    System.setProperty("app.env", "test");
    repository = new QuantityMeasurementDataBaseRepository();
    repository.deleteAll();
    service = new QuantityMeasurementServiceImpl(repository);
  }

  @After
  public void tearDown() {
    repository.deleteAll();
    repository.releaseResources();
  }

  @Test
  public void testServicePersistsComparison() {
    QuantityMeasurementEntity result = service.compare(new QuantityDTO(1, "FEET"), new QuantityDTO(12, "INCHES"));

    assertFalse(result.hasError());
    assertEquals("true", result.getResult());
    assertEquals(1, repository.getTotalCount());
  }

  @Test
  public void testServicePersistsConvert() {
    QuantityMeasurementEntity result = service.convert(new QuantityDTO(0, "CELSIUS"), "FAHRENHEIT");

    assertFalse(result.hasError());
    assertTrue(result.getResult().contains("32.0"));
    assertEquals(1, repository.getTotalCount());
  }

  @Test
  public void testServiceHandlesCrossCategoryError() {
    QuantityMeasurementEntity result = service.add(new QuantityDTO(1, "FEET"), new QuantityDTO(1, "KILOGRAM"));

    assertTrue(result.hasError());
    assertEquals(1, repository.getTotalCount());
  }
}
