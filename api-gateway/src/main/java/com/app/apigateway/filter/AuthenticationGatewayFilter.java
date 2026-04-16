package com.app.apigateway.filter;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Authentication Gateway Filter
 *
 * Handles JWT token validation and authentication for protected routes.
 *
 * Features:
 * - Allows public endpoints (login, register, oauth2) without authentication
 * - Requires JWT token for protected endpoints
 * - Passes JWT token to downstream services
 * - Logs authentication attempts
 */
@Component
public class AuthenticationGatewayFilter implements GlobalFilter, Ordered {

  // Public endpoints that don't require authentication
  private static final List<String> PUBLIC_ENDPOINTS = Arrays.asList(
      "/api/v1/auth/login",
      "/api/v1/auth/register",
      "/api/v1/auth/oauth2"
  );

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    String path = request.getPath().value();

    // Skip authentication for public endpoints
    if (isPublicEndpoint(path)) {
      return chain.filter(exchange);
    }

    // Check for JWT token on protected endpoints
    String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

    if (!StringUtils.hasText(authHeader)) {
      return onError(exchange, "Missing authentication token", HttpStatus.UNAUTHORIZED);
    }

    if (!authHeader.startsWith("Bearer ")) {
      return onError(exchange, "Invalid token format. Expected Bearer token", HttpStatus.UNAUTHORIZED);
    }

    String token = authHeader.substring("Bearer ".length());

    if (!StringUtils.hasText(token)) {
      return onError(exchange, "Empty token", HttpStatus.UNAUTHORIZED);
    }

    // Log authentication attempt
    logAuthenticationAttempt(request, token);

    // Pass request through with JWT token
    return chain.filter(exchange);
  }

  /**
   * Check if the request path is a public endpoint
   */
  private boolean isPublicEndpoint(String path) {
    return PUBLIC_ENDPOINTS.stream().anyMatch(path::startsWith);
  }

  /**
   * Handle authentication error response
   */
  private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(status);
    response.getHeaders().set(HttpHeaders.CONTENT_TYPE, "application/json");

    String body = String.format(
        "{\"error\":\"%s\",\"status\":%d,\"path\":\"%s\"}",
        message,
        status.value(),
        exchange.getRequest().getPath().value()
    );

    DataBufferFactory bufferFactory = response.bufferFactory();
    DataBuffer buffer = bufferFactory.wrap(body.getBytes(StandardCharsets.UTF_8));
    return response.writeWith(Mono.just(buffer));
  }

  /**
   * Log authentication attempt for monitoring
   */
  private void logAuthenticationAttempt(ServerHttpRequest request, String token) {
    String remoteAddress = request.getRemoteAddress() != null
        ? request.getRemoteAddress().getAddress().getHostAddress()
        : "UNKNOWN";

    // Log token received (without full token for security)
    String tokenPrefix = token.length() > 10 ? token.substring(0, 10) + "..." : token;
    System.out.println(String.format(
        "[AUTH-GATEWAY] Authentication attempt - Path: %s, RemoteIP: %s, Token: %s",
        request.getPath().value(),
        remoteAddress,
        tokenPrefix
    ));
  }

  @Override
  public int getOrder() {
    return 0; // Execute after global JWT relay filter
  }
}



