package com.app.authservice.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class AuthEndpointPolicyTest {

  private final AuthEndpointPolicy policy = new AuthEndpointPolicy();

  @Test
  void recognizesPublicAuthPaths() {
    assertTrue(policy.isPublicAuthPath("/api/v1/auth/login"));
    assertTrue(policy.isPublicAuthPath("/auth-service/api/v1/auth/register/"));
    assertTrue(policy.isPublicAuthPath("/api/v1/auth/oauth2/google"));
  }

  @Test
  void rejectsProtectedPaths() {
    assertFalse(policy.isPublicAuthPath("/api/v1/auth/logout"));
    assertFalse(policy.isPublicAuthPath("/api/v1/quantities/add"));
  }

  @Test
  void exposesRequiredPermitAllMatchers() {
    String[] matchers = policy.publicRequestMatchers();

    assertTrue(Arrays.asList(matchers).contains("/api/v1/auth/login"));
    assertTrue(Arrays.asList(matchers).contains("/api/v1/auth/register"));
    assertTrue(Arrays.asList(matchers).contains("/api/v1/auth/oauth2/**"));
  }
}

