package com.app.apigateway.controller;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class GatewayFallbackController {

  @RequestMapping("/auth")
  public Mono<ResponseEntity<Map<String, Object>>> authFallback(ServerWebExchange exchange) {
    HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
    return Mono.just(ResponseEntity.status(status)
        .body(buildError(status, "Authentication service is temporarily unavailable. Please retry.",
            resolveOriginalPath(exchange, "/fallback/auth"))));
  }

  @RequestMapping("/quantity")
  public Mono<ResponseEntity<Map<String, Object>>> quantityFallback(ServerWebExchange exchange) {
    HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
    return Mono.just(ResponseEntity.status(status)
        .body(buildError(status, "Quantity service is temporarily unavailable. Please retry.",
            resolveOriginalPath(exchange, "/fallback/quantity"))));
  }

  private Map<String, Object> buildError(HttpStatus status, String message, String path) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", Instant.now().toString());
    body.put("status", status.value());
    body.put("error", status.getReasonPhrase());
    body.put("message", message);
    body.put("path", path);
    return body;
  }

  private String resolveOriginalPath(ServerWebExchange exchange, String fallbackPath) {
    Object original = exchange.getAttributes().get(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
    if (original instanceof Set<?> urls && !urls.isEmpty()) {
      Object first = urls.iterator().next();
      if (first instanceof URI uri && uri.getPath() != null && !uri.getPath().isBlank()) {
        return uri.getPath();
      }
    }
    return fallbackPath;
  }
}
