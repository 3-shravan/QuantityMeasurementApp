package com.app.quantitymeasurement.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;

@Component
@Profile("!test")
public class StartupEnvironmentLogger {

  private static final Logger LOGGER = LoggerFactory.getLogger(StartupEnvironmentLogger.class);

  private final Environment environment;

  public StartupEnvironmentLogger(Environment environment) {
    this.environment = environment;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void logRuntimeConfiguration() {
    String profiles = resolveProfiles();
    String dataSourceTarget = parseDataSourceTarget(environment.getProperty("spring.datasource.url"));

    LOGGER.info("Runtime profile(s): {} | Datasource target: {}", profiles, dataSourceTarget);
  }

  private String resolveProfiles() {
    String[] activeProfiles = environment.getActiveProfiles();
    if (activeProfiles.length > 0) {
      return Arrays.stream(activeProfiles).collect(Collectors.joining(","));
    }

    String[] defaultProfiles = environment.getDefaultProfiles();
    if (defaultProfiles.length > 0) {
      return Arrays.stream(defaultProfiles).collect(Collectors.joining(","));
    }

    return "default";
  }

  static String parseDataSourceTarget(String jdbcUrl) {
    if (jdbcUrl == null || jdbcUrl.isBlank()) {
      return "unconfigured";
    }

    if (!jdbcUrl.startsWith("jdbc:")) {
      return "unresolved";
    }

    String normalized = jdbcUrl.substring(5);
    try {
      URI uri = new URI(normalized);
      String host = uri.getHost();
      int port = uri.getPort();
      String path = uri.getPath();

      if (host == null || path == null || path.length() <= 1) {
        return "unresolved";
      }

      String database = path.substring(1);
      String portPart = port > 0 ? String.valueOf(port) : "default";
      return host + ":" + portPart + "/" + database;
    } catch (URISyntaxException ex) {
      return "unresolved";
    }
  }
}

