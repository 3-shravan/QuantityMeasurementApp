package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.DatabaseException;
import com.app.quantitymeasurement.util.ApplicationConfig;
import com.app.quantitymeasurement.util.ConnectionPool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class QuantityMeasurementDataBaseRepository implements IQuantityMeasurementRepository {

  private final ConnectionPool pool;

  public QuantityMeasurementDataBaseRepository() {
    this(new ApplicationConfig());
  }

  public QuantityMeasurementDataBaseRepository(ApplicationConfig config) {
    this.pool = new ConnectionPool(config);
    initializeSchema();
  }

  private void initializeSchema() {
    try (Connection connection = pool.acquire(); Statement statement = connection.createStatement()) {
      connection.setAutoCommit(false);

      String jdbcUrl = connection.getMetaData().getURL();
      if (jdbcUrl != null && jdbcUrl.toLowerCase().contains("mysql")) {
        initializeMySqlSchema(statement);
      } else {
        String sqlScript = readClasspathResource("db/schema.sql");
        if (sqlScript == null || sqlScript.isBlank()) {
          connection.commit();
          return;
        }
        executeSqlScript(statement, sqlScript);
      }

      connection.commit();
    } catch (SQLException e) {
      throw new DatabaseException("Failed to initialize database schema", e);
    }
  }

  private void executeSqlScript(Statement statement, String sqlScript) throws SQLException {
    for (String command : sqlScript.split(";")) {
      String trimmed = command.trim();
      if (!trimmed.isEmpty()) {
        statement.execute(trimmed);
      }
    }
  }

  private void initializeMySqlSchema(Statement statement) throws SQLException {
    statement.execute("CREATE TABLE IF NOT EXISTS `quantity_measurement_entity` ("
        + "`id` BIGINT NOT NULL AUTO_INCREMENT,"
        + "`first_value` DOUBLE NOT NULL,"
        + "`first_unit` VARCHAR(64) NOT NULL,"
        + "`second_value` DOUBLE NULL,"
        + "`second_unit` VARCHAR(64) NULL,"
        + "`operation` VARCHAR(64) NOT NULL,"
        + "`measurement_type` VARCHAR(64) NOT NULL,"
        + "`result` VARCHAR(255) NOT NULL,"
        + "`error` BOOLEAN NOT NULL DEFAULT FALSE,"
        + "`error_message` VARCHAR(500) NULL,"
        + "`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
        + "PRIMARY KEY (`id`)"
        + ")");

    safeCreateIndex(statement,
        "CREATE INDEX `idx_qme_operation` ON `quantity_measurement_entity`(`operation`)");
    safeCreateIndex(statement,
        "CREATE INDEX `idx_qme_measurement_type` ON `quantity_measurement_entity`(`measurement_type`)");

    statement.execute("CREATE TABLE IF NOT EXISTS `quantity_measurement_history` ("
        + "`id` BIGINT NOT NULL AUTO_INCREMENT,"
        + "`measurement_id` BIGINT NOT NULL,"
        + "`operation` VARCHAR(64) NOT NULL,"
        + "`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
        + "PRIMARY KEY (`id`),"
        + "CONSTRAINT `fk_qmh_measurement` "
        + "FOREIGN KEY (`measurement_id`) REFERENCES `quantity_measurement_entity`(`id`) ON DELETE CASCADE"
        + ")");
  }

  private void safeCreateIndex(Statement statement, String sql) throws SQLException {
    try {
      statement.execute(sql);
    } catch (SQLException e) {
      if (e.getErrorCode() != 1061) {
        throw e;
      }
    }
  }

  private String readClasspathResource(String path) {
    try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
      if (in == null) {
        return null;
      }
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
        return reader.lines().collect(Collectors.joining("\n"));
      }
    } catch (IOException e) {
      throw new DatabaseException("Failed to read SQL schema resource: " + path, e);
    }
  }

  @Override
  public QuantityMeasurementEntity save(QuantityMeasurementEntity entity) {
    String insertSql = "INSERT INTO `quantity_measurement_entity` "
        + "(`first_value`, `first_unit`, `second_value`, `second_unit`, `operation`, `measurement_type`, `result`, `error`, `error_message`) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String historySql = "INSERT INTO `quantity_measurement_history` (`measurement_id`, `operation`, `created_at`) VALUES (?, ?, ?)";

    try (Connection connection = pool.acquire()) {
      connection.setAutoCommit(false);
      try (PreparedStatement insertStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
          PreparedStatement historyStatement = connection.prepareStatement(historySql)) {

        insertStatement.setDouble(1, entity.getFirstValue());
        insertStatement.setString(2, entity.getFirstUnit());

        if (entity.getSecondValue() == null) {
          insertStatement.setNull(3, java.sql.Types.DOUBLE);
        } else {
          insertStatement.setDouble(3, entity.getSecondValue());
        }

        insertStatement.setString(4, entity.getSecondUnit());
        insertStatement.setString(5, entity.getOperation());
        insertStatement.setString(6, entity.getMeasurementType());
        insertStatement.setString(7, entity.getResult());
        insertStatement.setBoolean(8, entity.hasError());
        insertStatement.setString(9, entity.getErrorMessage());

        int inserted = insertStatement.executeUpdate();
        if (inserted == 0) {
          throw new DatabaseException("Insert failed: no rows affected");
        }

        long id;
        try (ResultSet keys = insertStatement.getGeneratedKeys()) {
          if (!keys.next()) {
            throw new DatabaseException("Insert failed: no generated ID returned");
          }
          id = keys.getLong(1);
        }

        entity.setId(id);

        historyStatement.setLong(1, id);
        historyStatement.setString(2, entity.getOperation());
        historyStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        historyStatement.executeUpdate();

        connection.commit();
        return entity;
      } catch (Exception e) {
        connection.rollback();
        if (e instanceof DatabaseException) {
          throw e;
        }
        throw new DatabaseException("Failed to save measurement", e);
      } finally {
        connection.setAutoCommit(true);
      }
    } catch (SQLException e) {
      throw new DatabaseException("Failed to save measurement", e);
    }
  }

  @Override
  public Optional<QuantityMeasurementEntity> findById(long id) {
    String sql = "SELECT * FROM `quantity_measurement_entity` WHERE `id` = ?";
    try (Connection connection = pool.acquire(); PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setLong(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return Optional.of(map(resultSet));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new DatabaseException("Failed to fetch measurement by id", e);
    }
  }

  @Override
  public List<QuantityMeasurementEntity> findAll() {
    String sql = "SELECT * FROM `quantity_measurement_entity` ORDER BY `id`";
    return queryList(sql, null);
  }

  @Override
  public List<QuantityMeasurementEntity> findByMeasurementType(String measurementType) {
    String sql = "SELECT * FROM `quantity_measurement_entity` WHERE LOWER(`measurement_type`) = LOWER(?) ORDER BY `id`";
    return queryList(sql, statement -> statement.setString(1, measurementType));
  }

  @Override
  public List<QuantityMeasurementEntity> findByOperation(String operation) {
    String sql = "SELECT * FROM `quantity_measurement_entity` WHERE LOWER(`operation`) = LOWER(?) ORDER BY `id`";
    return queryList(sql, statement -> statement.setString(1, operation));
  }

  @Override
  public long getTotalCount() {
    String sql = "SELECT COUNT(*) FROM `quantity_measurement_entity`";
    try (Connection connection = pool.acquire();
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery()) {
      if (resultSet.next()) {
        return resultSet.getLong(1);
      }
      return 0;
    } catch (SQLException e) {
      throw new DatabaseException("Failed to fetch measurements count", e);
    }
  }

  @Override
  public void deleteAll() {
    String historyDelete = "DELETE FROM `quantity_measurement_history`";
    String measurementDelete = "DELETE FROM `quantity_measurement_entity`";

    try (Connection connection = pool.acquire();
        PreparedStatement historyStatement = connection.prepareStatement(historyDelete);
        PreparedStatement measurementStatement = connection.prepareStatement(measurementDelete)) {
      connection.setAutoCommit(false);
      historyStatement.executeUpdate();
      measurementStatement.executeUpdate();
      connection.commit();
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      throw new DatabaseException("Failed to delete all measurements", e);
    }
  }

  @Override
  public void deleteById(long id) {
    String historyDelete = "DELETE FROM `quantity_measurement_history` WHERE `measurement_id` = ?";
    String measurementDelete = "DELETE FROM `quantity_measurement_entity` WHERE `id` = ?";

    try (Connection connection = pool.acquire();
        PreparedStatement historyStatement = connection.prepareStatement(historyDelete);
        PreparedStatement measurementStatement = connection.prepareStatement(measurementDelete)) {
      connection.setAutoCommit(false);
      historyStatement.setLong(1, id);
      historyStatement.executeUpdate();

      measurementStatement.setLong(1, id);
      measurementStatement.executeUpdate();

      connection.commit();
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      throw new DatabaseException("Failed to delete measurement by id", e);
    }
  }

  @Override
  public String getPoolStatistics() {
    return pool.getStatistics();
  }

  @Override
  public void releaseResources() {
    pool.shutdown();
  }

  private List<QuantityMeasurementEntity> queryList(String sql, StatementConfigurer configurer) {
    List<QuantityMeasurementEntity> entities = new ArrayList<>();
    try (Connection connection = pool.acquire(); PreparedStatement statement = connection.prepareStatement(sql)) {
      if (configurer != null) {
        configurer.configure(statement);
      }
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          entities.add(map(resultSet));
        }
      }
      return entities;
    } catch (SQLException e) {
      throw new DatabaseException("Query execution failed", e);
    }
  }

  private QuantityMeasurementEntity map(ResultSet resultSet) throws SQLException {
    double firstValue = resultSet.getDouble("first_value");
    String firstUnit = resultSet.getString("first_unit");
    Double secondValue = resultSet.getObject("second_value") == null ? null : resultSet.getDouble("second_value");
    String secondUnit = resultSet.getString("second_unit");
    String operation = resultSet.getString("operation");
    String measurementType = resultSet.getString("measurement_type");
    String result = resultSet.getString("result");
    boolean error = resultSet.getBoolean("error");
    String errorMessage = resultSet.getString("error_message");

    QuantityMeasurementEntity entity = error
        ? new QuantityMeasurementEntity(firstValue, firstUnit, secondValue, secondUnit, operation, measurementType,
            true,
            errorMessage)
        : new QuantityMeasurementEntity(firstValue, firstUnit, secondValue, secondUnit, operation, measurementType,
            result);
    entity.setId(resultSet.getLong("id"));
    return entity;
  }

  @FunctionalInterface
  private interface StatementConfigurer {
    void configure(PreparedStatement statement) throws SQLException;
  }
}
