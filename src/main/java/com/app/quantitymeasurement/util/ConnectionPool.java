package com.app.quantitymeasurement.util;

import com.app.quantitymeasurement.exception.DatabaseException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Lightweight JDBC connection pool for UC16.
 */
public class ConnectionPool {

  private final String url;
  private final String username;
  private final String password;
  private final int maxPoolSize;
  private final long acquireTimeoutMillis;

  private final BlockingQueue<Connection> available;
  private final Set<Connection> allPhysicalConnections = ConcurrentHashMap.newKeySet();

  public ConnectionPool(ApplicationConfig config) {
    this.url = config.get("db.url", "jdbc:h2:mem:quantitymeasurement;DB_CLOSE_DELAY=-1;MODE=MySQL");
    this.username = config.get("db.username", "sa");
    this.password = config.get("db.password", "");
    this.maxPoolSize = config.getInt("db.pool.maxSize", 10);
    this.acquireTimeoutMillis = config.getLong("db.pool.acquireTimeoutMs", Duration.ofSeconds(5).toMillis());

    this.available = new LinkedBlockingQueue<>(maxPoolSize);
    initialize(config.getInt("db.pool.initialSize", 3));
  }

  private void initialize(int initialSize) {
    int size = Math.max(1, Math.min(initialSize, maxPoolSize));
    for (int i = 0; i < size; i++) {
      Connection connection = newPhysicalConnection();
      available.offer(connection);
      allPhysicalConnections.add(connection);
    }
  }

  public Connection acquire() {
    try {
      Connection connection = available.poll();
      if (connection == null && allPhysicalConnections.size() < maxPoolSize) {
        connection = newPhysicalConnection();
        allPhysicalConnections.add(connection);
      }
      if (connection == null) {
        connection = available.poll(acquireTimeoutMillis, TimeUnit.MILLISECONDS);
      }
      if (connection == null) {
        throw new DatabaseException("Timed out waiting for a database connection");
      }
      if (connection.isClosed()) {
        allPhysicalConnections.remove(connection);
        return acquire();
      }
      return wrap(connection);
    } catch (SQLException | InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new DatabaseException("Unable to acquire a database connection", e);
    }
  }

  private Connection wrap(Connection physicalConnection) {
    InvocationHandler handler = new PooledConnectionHandler(physicalConnection, this);
    return (Connection) Proxy.newProxyInstance(
        Connection.class.getClassLoader(),
        new Class<?>[] { Connection.class },
        handler);
  }

  private Connection newPhysicalConnection() {
    try {
      return DriverManager.getConnection(url, username, password);
    } catch (SQLException e) {
      throw new DatabaseException("Unable to create a database connection", e);
    }
  }

  void release(Connection physicalConnection) {
    if (physicalConnection == null) {
      return;
    }
    try {
      if (!physicalConnection.isClosed()) {
        available.offer(physicalConnection);
      }
    } catch (SQLException e) {
      closeQuietly(physicalConnection);
      allPhysicalConnections.remove(physicalConnection);
    }
  }

  public String getStatistics() {
    int total = allPhysicalConnections.size();
    int idle = available.size();
    int active = Math.max(total - idle, 0);
    return "PoolStats{total=" + total + ", active=" + active + ", idle=" + idle + ", max=" + maxPoolSize + "}";
  }

  public void shutdown() {
    for (Connection connection : allPhysicalConnections) {
      closeQuietly(connection);
    }
    available.clear();
    allPhysicalConnections.clear();
  }

  private void closeQuietly(Connection connection) {
    try {
      connection.close();
    } catch (SQLException ignored) {
      // ignored
    }
  }

  private static class PooledConnectionHandler implements InvocationHandler {

    private final Connection physicalConnection;
    private final ConnectionPool pool;
    private boolean closed;

    private PooledConnectionHandler(Connection physicalConnection, ConnectionPool pool) {
      this.physicalConnection = physicalConnection;
      this.pool = pool;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      String methodName = method.getName();
      if ("close".equals(methodName)) {
        if (!closed) {
          closed = true;
          pool.release(physicalConnection);
        }
        return null;
      }
      if ("isClosed".equals(methodName)) {
        return closed;
      }
      if (closed) {
        throw new SQLException("Connection is already closed");
      }
      return method.invoke(physicalConnection, args);
    }
  }
}
