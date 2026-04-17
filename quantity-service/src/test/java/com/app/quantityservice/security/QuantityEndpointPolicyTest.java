package com.app.quantityservice.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class QuantityEndpointPolicyTest {

  private final QuantityEndpointPolicy policy = new QuantityEndpointPolicy();

  @Test
  void publicPathsAreRecognized() {
    assertTrue(policy.isPublicPath("/api/v1/quantities/compare"));
    assertTrue(policy.isPublicPath("/swagger-ui/index.html"));
    assertTrue(policy.isPublicPath("/v3/api-docs"));
    assertTrue(policy.isPublicPath("/actuator/health"));
  }

  @Test
  void protectedPathsAreNotRecognizedAsPublic() {
    assertFalse(policy.isPublicPath("/internal/admin"));
    assertFalse(policy.isPublicPath(""));
    assertFalse(policy.isPublicPath(null));
  }
}

