package com.app.apigateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class GatewaySecurityAndRateLimitConfig {

  @Bean
  public KeyResolver userKeyResolver() {
    return exchange -> Mono.just(resolveKey(exchange));
  }

  private String resolveKey(ServerWebExchange exchange) {
    String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
      // Use bearer token as a stable per-user key for rate limiting.
      return authorizationHeader.substring("Bearer ".length());
    }

    String remoteAddress = exchange.getRequest().getRemoteAddress() != null
        ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
        : "anonymous";

    return remoteAddress;
  }
}

