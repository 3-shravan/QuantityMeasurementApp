package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class QuantityMeasurementDatabaseRepositoryTest {

  private QuantityMeasurementDataBaseRepository repository;

  @Before
  public void setUp() {
    System.setProperty("app.env", "test");
    repository = new QuantityMeasurementDataBaseRepository();
    repository.deleteAll();
  }

  @After
  public void tearDown() {
    repository.deleteAll();
    repository.releaseResources();
  }

  @Test
  public void testSaveAndFindById() {
    QuantityMeasurementEntity saved = repository.save(
        new QuantityMeasurementEntity(2, "FEET", 24.0, "INCHES", "COMPARISON", "LengthUnit", "true"));

    assertTrue(saved.getId() > 0);
    assertTrue(repository.findById(saved.getId()).isPresent());
  }

  @Test
  public void testFindByOperationAndType() {
    repository.save(new QuantityMeasurementEntity(2, "FEET", 24.0, "INCHES", "COMPARISON", "LengthUnit", "true"));
    repository.save(new QuantityMeasurementEntity(0, "CELSIUS", null, "FAHRENHEIT", "CONVERT", "TemperatureUnit",
        "32.0 FAHRENHEIT"));

    List<QuantityMeasurementEntity> byOperation = repository.findByOperation("COMPARISON");
    List<QuantityMeasurementEntity> byType = repository.findByMeasurementType("TemperatureUnit");

    assertEquals(1, byOperation.size());
    assertEquals(1, byType.size());
    assertEquals("CONVERT", byType.get(0).getOperation());
  }

  @Test
  public void testCountAndDeleteAll() {
    repository.save(new QuantityMeasurementEntity(1, "KILOGRAM", 1000.0, "GRAM", "COMPARISON", "WeightUnit", "true"));
    repository
        .save(new QuantityMeasurementEntity(1, "LITRE", 1000.0, "MILLILITRE", "COMPARISON", "VolumeUnit", "true"));

    assertEquals(2, repository.getTotalCount());

    repository.deleteAll();

    assertEquals(0, repository.getTotalCount());
    assertTrue(repository.findAll().isEmpty());
  }

  @Test
  public void testPoolStatisticsAvailable() {
    String stats = repository.getPoolStatistics();
    assertNotNull(stats);
    assertFalse(stats.isEmpty());
    assertTrue(stats.contains("PoolStats"));
  }
}
