package com.app.authservice.security;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class AuthEndpointPolicy {

  private static final Pattern PUBLIC_AUTH_PATH_PATTERN = Pattern.compile(
      "^/(?:.*/)?api/v1/auth/(?:login|register)(?:/.*)?$|^/(?:.*/)?api/v1/auth/oauth2(?:/.*)?$"
  );

  private static final String[] PUBLIC_REQUEST_MATCHERS = {
      "/api/v1/auth/login", "/api/v1/auth/login/**",
      "/api/v1/auth/register", "/api/v1/auth/register/**",
      "/api/v1/auth/oauth2/**",
      "/auth-service/api/v1/auth/login", "/auth-service/api/v1/auth/login/**",
      "/auth-service/api/v1/auth/register", "/auth-service/api/v1/auth/register/**",
      "/auth-service/api/v1/auth/oauth2/**"
  };

  public boolean isPublicAuthPath(String path) {
    if (path == null || path.isBlank()) {
      return false;
    }

    String normalized = path.trim();
    int queryIndex = normalized.indexOf('?');
    if (queryIndex >= 0) {
      normalized = normalized.substring(0, queryIndex);
    }

    if (normalized.length() > 1 && normalized.endsWith("/")) {
      normalized = normalized.substring(0, normalized.length() - 1);
    }

    return PUBLIC_AUTH_PATH_PATTERN.matcher(normalized).matches();
  }

  public String[] publicRequestMatchers() {
    return PUBLIC_REQUEST_MATCHERS.clone();
  }
}

