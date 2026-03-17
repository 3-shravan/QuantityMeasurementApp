package com.app.quantitymeasurement.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads configuration from application.properties and supports
 * environment-specific keys.
 * Resolution order: System property -> env-prefixed property -> plain property
 * -> default.
 */
public class ApplicationConfig {

  private final Properties properties = new Properties();
  private final String environment;

  public ApplicationConfig() {
    this("application.properties");
  }

  public ApplicationConfig(String propertiesFileName) {
    this.environment = System.getProperty("app.env", "dev").trim().toLowerCase();
    load(propertiesFileName);
  }

  private void load(String fileName) {
    try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
      if (inputStream != null) {
        properties.load(inputStream);
      }
    } catch (IOException e) {
      throw new IllegalStateException("Unable to load configuration file: " + fileName, e);
    }
  }

  public String get(String key, String defaultValue) {
    String sysValue = System.getProperty(key);
    if (sysValue != null && !sysValue.isBlank()) {
      return sysValue.trim();
    }

    String envKey = environment + "." + key;
    String envValue = properties.getProperty(envKey);
    if (envValue != null && !envValue.isBlank()) {
      return envValue.trim();
    }

    String value = properties.getProperty(key);
    if (value != null && !value.isBlank()) {
      return value.trim();
    }

    return defaultValue;
  }

  public int getInt(String key, int defaultValue) {
    return Integer.parseInt(get(key, String.valueOf(defaultValue)));
  }

  public long getLong(String key, long defaultValue) {
    return Long.parseLong(get(key, String.valueOf(defaultValue)));
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
  }
}
