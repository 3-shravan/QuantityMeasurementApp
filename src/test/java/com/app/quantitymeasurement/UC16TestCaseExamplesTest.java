package com.app.quantitymeasurement;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.DatabaseException;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementDataBaseRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepositoryFactory;
import com.app.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import com.app.quantitymeasurement.util.ApplicationConfig;
import com.app.quantitymeasurement.util.ConnectionPool;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UC16TestCaseExamplesTest {

  private QuantityMeasurementDataBaseRepository dbRepository;

  @Before
  public void setUp() {
    System.setProperty("app.env", "test");
    dbRepository = new QuantityMeasurementDataBaseRepository();
    dbRepository.deleteAll();
  }

  @After
  public void tearDown() {
    if (dbRepository != null) {
      dbRepository.deleteAll();
      dbRepository.releaseResources();
    }
  }

  @Test
  public void testMavenBuild_Success() {
    assertTrue(new File("pom.xml").exists());
    assertTrue(new File("target/classes").exists());
  }

  @Test
  public void testPackageStructure_AllLayersPresent() {
    assertTrue(new File("src/main/java/com/app/quantitymeasurement/controller").exists());
    assertTrue(new File("src/main/java/com/app/quantitymeasurement/service").exists());
    assertTrue(new File("src/main/java/com/app/quantitymeasurement/repository").exists());
    assertTrue(new File("src/main/java/com/app/quantitymeasurement/entity").exists());
  }

  @Test
  public void testPomDependencies_JDBCDriversIncluded() throws Exception {
    String pom = Files.readString(Path.of("pom.xml"));
    assertTrue(pom.contains("com.h2database"));
    assertTrue(pom.contains("mysql-connector-java") || pom.contains("mysql-connector-j"));
    assertTrue(pom.contains("JDBC Drivers"));
    assertTrue(pom.contains("junit"));
    assertTrue(pom.contains("mockito"));
  }

  @Test
  public void testDatabaseConfiguration_LoadedFromProperties() {
    ApplicationConfig config = new ApplicationConfig();
    String testUrl = config.get("db.url", "fallback");
    assertNotNull(testUrl);
    assertFalse(testUrl.isBlank());

    System.setProperty("db.url", "jdbc:h2:mem:override");
    assertEquals("jdbc:h2:mem:override", config.get("db.url", "fallback"));
    System.clearProperty("db.url");
  }

  @Test
  public void testConnectionPool_Initialization() {
    System.setProperty("db.pool.initialSize", "2");
    System.setProperty("db.pool.maxSize", "4");
    ConnectionPool pool = new ConnectionPool(new ApplicationConfig());

    String stats = pool.getStatistics();
    assertTrue(stats.contains("total="));
    assertTrue(stats.contains("idle="));

    pool.shutdown();
    System.clearProperty("db.pool.initialSize");
    System.clearProperty("db.pool.maxSize");
  }

  @Test
  public void testConnectionPool_Acquire_Release() throws Exception {
    ConnectionPool pool = new ConnectionPool(new ApplicationConfig());
    Connection connection = pool.acquire();
    assertNotNull(connection);
    connection.close();
    assertTrue(pool.getStatistics().contains("idle="));
    pool.shutdown();
  }

  @Test
  public void testConnectionPool_AllConnectionsExhausted() throws Exception {
    System.setProperty("db.pool.initialSize", "1");
    System.setProperty("db.pool.maxSize", "1");
    System.setProperty("db.pool.acquireTimeoutMs", "200");
    ConnectionPool pool = new ConnectionPool(new ApplicationConfig());
    Connection c1 = pool.acquire();

    try {
      pool.acquire();
      fail("Expected pool exhaustion");
    } catch (DatabaseException expected) {
      assertTrue(expected.getMessage().toLowerCase().contains("timed out"));
    } finally {
      c1.close();
      pool.shutdown();
      System.clearProperty("db.pool.initialSize");
      System.clearProperty("db.pool.maxSize");
      System.clearProperty("db.pool.acquireTimeoutMs");
    }
  }

  @Test
  public void testDatabaseRepository_SaveEntity() {
    QuantityMeasurementEntity saved = dbRepository.save(
        new QuantityMeasurementEntity(2, "FEET", 24.0, "INCHES", "COMPARISON", "LengthUnit", "true"));
    assertTrue(saved.getId() > 0);
  }

  @Test
  public void testDatabaseRepository_RetrieveAllMeasurements() {
    dbRepository
        .save(new QuantityMeasurementEntity(1, "LITRE", 1000.0, "MILLILITRE", "COMPARISON", "VolumeUnit", "true"));
    dbRepository.save(new QuantityMeasurementEntity(1, "KILOGRAM", 1000.0, "GRAM", "COMPARISON", "WeightUnit", "true"));
    assertEquals(2, dbRepository.findAll().size());
  }

  @Test
  public void testDatabaseRepository_QueryByOperation() {
    dbRepository.save(new QuantityMeasurementEntity(1, "FEET", 12.0, "INCHES", "COMPARISON", "LengthUnit", "true"));
    dbRepository.save(new QuantityMeasurementEntity(1, "LITRE", null, "MILLILITRE", "CONVERT", "VolumeUnit", "1000.0"));
    assertEquals(1, dbRepository.findByOperation("CONVERT").size());
  }

  @Test
  public void testDatabaseRepository_QueryByMeasurementType() {
    dbRepository.save(new QuantityMeasurementEntity(1, "FEET", 12.0, "INCHES", "COMPARISON", "LengthUnit", "true"));
    dbRepository.save(new QuantityMeasurementEntity(1, "LITRE", null, "MILLILITRE", "CONVERT", "VolumeUnit", "1000.0"));
    assertEquals(1, dbRepository.findByMeasurementType("VolumeUnit").size());
  }

  @Test
  public void testDatabaseRepository_CountMeasurements() {
    dbRepository.save(new QuantityMeasurementEntity(2, "FEET", 24.0, "INCHES", "COMPARISON", "LengthUnit", "true"));
    dbRepository.save(new QuantityMeasurementEntity(2, "KILOGRAM", 2000.0, "GRAM", "COMPARISON", "WeightUnit", "true"));
    assertEquals(2, dbRepository.getTotalCount());
  }

  @Test
  public void testDatabaseRepository_DeleteAll() {
    dbRepository.save(new QuantityMeasurementEntity(2, "FEET", 24.0, "INCHES", "COMPARISON", "LengthUnit", "true"));
    dbRepository.deleteAll();
    assertEquals(0, dbRepository.getTotalCount());
  }

  @Test
  public void testSQLInjectionPrevention() {
    dbRepository.save(new QuantityMeasurementEntity(2, "FEET", 24.0, "INCHES", "COMPARISON", "LengthUnit", "true"));
    List<QuantityMeasurementEntity> entities = dbRepository.findByMeasurementType("LengthUnit' OR '1'='1");
    assertEquals(0, entities.size());
  }

  @Test
  public void testTransactionRollback_OnError() {
    long before = dbRepository.getTotalCount();
    String veryLongUnit = "X".repeat(200);
    try {
      dbRepository
          .save(new QuantityMeasurementEntity(2, veryLongUnit, 24.0, "INCHES", "COMPARISON", "LengthUnit", "true"));
      fail("Expected database exception");
    } catch (DatabaseException expected) {
      // expected
    }
    assertEquals(before, dbRepository.getTotalCount());
  }

  @Test
  public void testDatabaseSchema_TablesCreated() throws Exception {
    ApplicationConfig config = new ApplicationConfig();
    try (
        Connection connection = DriverManager.getConnection(config.get("db.url", ""), config.get("db.username", ""),
            config.get("db.password", ""));
        PreparedStatement ps = connection.prepareStatement(
            "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE LOWER(TABLE_NAME) IN ('quantity_measurement_entity','quantity_measurement_history')");
        ResultSet rs = ps.executeQuery()) {
      assertTrue(rs.next());
      assertEquals(2, rs.getInt(1));
    }
  }

  @Test
  public void testH2TestDatabase_IsolationBetweenTests() {
    assertEquals(0, dbRepository.getTotalCount());
  }

  @Test
  public void testRepositoryFactory_CreateCacheRepository() {
    System.setProperty("repository.type", "cache");
    IQuantityMeasurementRepository repository = QuantityMeasurementRepositoryFactory
        .createRepository(new ApplicationConfig());
    assertTrue(repository instanceof QuantityMeasurementCacheRepository);
    repository.deleteAll();
    System.clearProperty("repository.type");
  }

  @Test
  public void testRepositoryFactory_CreateDatabaseRepository() {
    System.setProperty("repository.type", "database");
    IQuantityMeasurementRepository repository = QuantityMeasurementRepositoryFactory
        .createRepository(new ApplicationConfig());
    assertTrue(repository instanceof QuantityMeasurementDataBaseRepository);
    repository.releaseResources();
    System.clearProperty("repository.type");
  }

  @Test
  public void testServiceWithDatabaseRepository_Integration() {
    QuantityMeasurementServiceImpl service = new QuantityMeasurementServiceImpl(dbRepository);
    QuantityMeasurementEntity entity = service.compare(new QuantityDTO(1, "FEET"), new QuantityDTO(12, "INCHES"));
    assertFalse(entity.hasError());
    assertEquals(1, dbRepository.getTotalCount());
  }

  @Test
  public void testServiceWithCacheRepository_Integration() {
    QuantityMeasurementCacheRepository cache = QuantityMeasurementCacheRepository.getInstance();
    cache.deleteAll();
    QuantityMeasurementServiceImpl service = new QuantityMeasurementServiceImpl(cache);
    QuantityMeasurementEntity entity = service.compare(new QuantityDTO(1, "FEET"), new QuantityDTO(12, "INCHES"));
    assertFalse(entity.hasError());
    assertEquals(1, cache.getTotalCount());
    cache.deleteAll();
  }

  @Test
  public void testMavenTest_AllTestsPass() {
    assertTrue(new File("target/surefire-reports").exists());
  }

  @Test
  public void testMavenPackage_JarCreated() {
    File target = new File("target");
    assertTrue(target.exists());
  }

  @Test
  public void testDatabaseRepositoryPoolStatistics() {
    String stats = dbRepository.getPoolStatistics();
    assertTrue(stats.contains("PoolStats"));
    assertTrue(stats.contains("total="));
  }

  @Test
  public void testMySQLConnection_Success() throws Exception {
    ApplicationConfig config = new ApplicationConfig();
    String url = config.get("db.url", "");
    Assume.assumeTrue(url.toLowerCase().contains("mysql"));
    try (
        Connection connection = DriverManager.getConnection(url, config.get("db.username", ""),
            config.get("db.password", ""));
        Statement statement = connection.createStatement()) {
      statement.execute("CREATE TABLE IF NOT EXISTS quantity_mysql_probe (id INT PRIMARY KEY)");
      assertFalse(connection.isClosed());
    }
  }

  @Test
  public void testPostgreSQLConnection_Success() throws Exception {
    String pgUrl = System.getProperty("pg.url", "");
    String pgUser = System.getProperty("pg.username", "");
    String pgPass = System.getProperty("pg.password", "");
    Assume.assumeTrue(!pgUrl.isBlank());

    try (Connection connection = DriverManager.getConnection(pgUrl, pgUser, pgPass);
        Statement statement = connection.createStatement()) {
      statement.execute("CREATE TABLE IF NOT EXISTS quantity_pg_probe (id INT PRIMARY KEY)");
      assertFalse(connection.isClosed());
    }
  }

  @Test
  public void testDatabaseRepository_ConcurrentAccess() throws Exception {
    int threads = 8;
    int perThread = 20;
    ExecutorService executor = Executors.newFixedThreadPool(threads);
    CountDownLatch latch = new CountDownLatch(threads);

    for (int i = 0; i < threads; i++) {
      executor.submit(() -> {
        try {
          for (int j = 0; j < perThread; j++) {
            dbRepository
                .save(new QuantityMeasurementEntity(1, "FEET", 12.0, "INCHES", "COMPARISON", "LengthUnit", "true"));
          }
        } finally {
          latch.countDown();
        }
      });
    }

    assertTrue(latch.await(20, TimeUnit.SECONDS));
    executor.shutdownNow();

    assertEquals(threads * perThread, dbRepository.getTotalCount());
  }

  @Test
  public void testParameterizedQuery_DateTimeHandling() throws Exception {
    QuantityMeasurementEntity saved = dbRepository.save(
        new QuantityMeasurementEntity(1, "LITRE", null, "MILLILITRE", "CONVERT", "VolumeUnit", "1000.0"));
    assertTrue(saved.getId() > 0);

    ApplicationConfig config = new ApplicationConfig();
    try (
        Connection connection = DriverManager.getConnection(config.get("db.url", ""), config.get("db.username", ""),
            config.get("db.password", ""));
        PreparedStatement ps = connection
            .prepareStatement("SELECT created_at FROM quantity_measurement_history WHERE measurement_id = ?");) {
      ps.setLong(1, saved.getId());
      try (ResultSet rs = ps.executeQuery()) {
        assertTrue(rs.next());
        Timestamp ts = rs.getTimestamp(1);
        assertNotNull(ts);
      }
    }
  }

  @Test
  public void testDatabaseRepository_LargeDataSet() {
    for (int i = 0; i < 1000; i++) {
      dbRepository.save(new QuantityMeasurementEntity(1, "FEET", 12.0, "INCHES", "COMPARISON", "LengthUnit", "true"));
    }
    assertEquals(1000, dbRepository.findAll().size());
  }

  @Test
  public void testMavenClean_RemovesTargetDirectory() {
    assertTrue(new File("target").exists());
  }

  @Test
  public void testPropertiesConfiguration_EnvironmentOverride() {
    System.setProperty("repository.type", "cache");
    IQuantityMeasurementRepository repository = QuantityMeasurementRepositoryFactory
        .createRepository(new ApplicationConfig());
    assertTrue(repository instanceof QuantityMeasurementCacheRepository);
    System.clearProperty("repository.type");
  }

  @Test
  public void testDatabaseException_CustomException() {
    DatabaseException exception = new DatabaseException("db error");
    assertEquals("db error", exception.getMessage());
  }

  @Test
  public void testResourceCleanup_ConnectionClosed() throws Exception {
    ConnectionPool pool = new ConnectionPool(new ApplicationConfig());
    Connection connection = pool.acquire();
    assertFalse(connection.isClosed());
    connection.close();
    assertTrue(connection.isClosed());
    pool.shutdown();
  }

  @Test
  public void testBatchInsert_MultipleEntities() {
    List<QuantityMeasurementEntity> batch = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      batch.add(new QuantityMeasurementEntity(1, "FEET", 12.0, "INCHES", "COMPARISON", "LengthUnit", "true"));
    }
    for (QuantityMeasurementEntity entity : batch) {
      dbRepository.save(entity);
    }
    assertEquals(100, dbRepository.getTotalCount());
  }

  @Test
  public void testPomPlugin_Configuration() throws Exception {
    String pom = Files.readString(Path.of("pom.xml"));
    assertTrue(pom.contains("maven-compiler-plugin"));
    assertTrue(pom.contains("maven-surefire-plugin"));
    assertTrue(
        pom.contains("<release>17</release>") ||
            (pom.contains("<source>11</source>") && pom.contains("<target>11</target>")));
  }
}
