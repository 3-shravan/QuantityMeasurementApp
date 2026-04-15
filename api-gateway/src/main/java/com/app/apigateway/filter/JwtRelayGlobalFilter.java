package com.app.apigateway.filter;

import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import reactor.core.publisher.Mono;

@Component
public class JwtRelayGlobalFilter implements GlobalFilter, Ordered {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (!StringUtils.hasText(authorization) || !authorization.startsWith("Bearer ")) {
      return chain.filter(exchange);
    }

    // Ensure downstream services always receive the original bearer token.
    ServerWebExchange updatedExchange = exchange.mutate()
        .request(exchange.getRequest().mutate().headers(headers -> headers.set(HttpHeaders.AUTHORIZATION, authorization)).build())
        .build();

    return chain.filter(updatedExchange);
  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }
}

