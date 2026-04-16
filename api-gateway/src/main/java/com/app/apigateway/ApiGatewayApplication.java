package com.app.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API Gateway Application - Routes requests to microservices
 *
 * Features:
 * - Service discovery via Eureka
 * - JWT token relay for authenticated requests
 * - Rate limiting per user
 * - Circuit breaker pattern for resilience
 * - Fallback mechanisms for service failures
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiGatewayApplication.class, args);
  }
}

