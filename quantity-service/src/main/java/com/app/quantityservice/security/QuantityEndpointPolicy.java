package com.app.quantityservice.security;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class QuantityEndpointPolicy {

  public String[] publicMatcherPatterns() {
    return new String[] {
        "/actuator/**",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/api/v1/quantities/**"
    };
  }

  public boolean isPublicPath(String path) {
    if (!StringUtils.hasText(path)) {
      return false;
    }

    return path.startsWith("/actuator")
        || path.startsWith("/swagger-ui")
        || path.startsWith("/v3/api-docs")
        || path.startsWith("/api/v1/quantities");
  }
}

