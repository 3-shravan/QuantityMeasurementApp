package com.app.apigateway.controller;

import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class GatewayFallbackController {

  @RequestMapping("/auth")
  public Mono<ResponseEntity<Map<String, Object>>> authFallback() {
    return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
        .body(Map.of(
            "service", "auth",
            "message", "Authentication service is temporarily unavailable. Please retry.",
            "timestamp", Instant.now().toString())));
  }

  @RequestMapping("/quantity")
  public Mono<ResponseEntity<Map<String, Object>>> quantityFallback() {
    return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
        .body(Map.of(
            "service", "quantity",
            "message", "Quantity service is temporarily unavailable. Please retry.",
            "timestamp", Instant.now().toString())));
  }
}

