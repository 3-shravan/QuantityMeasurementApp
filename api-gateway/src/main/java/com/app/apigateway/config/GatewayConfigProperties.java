package com.app.apigateway.config;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Gateway configuration properties
 * Maps to app.gateway and app.services configuration sections
 */
@Component
@ConfigurationProperties(prefix = "app")
public class GatewayConfigProperties {

  private Gateway gateway;
  private Services services;

  public Gateway getGateway() {
    return gateway;
  }

  public void setGateway(Gateway gateway) {
    this.gateway = gateway;
  }

  public Services getServices() {
    return services;
  }

  public void setServices(Services services) {
    this.services = services;
  }

  public static class Gateway {
    private Retry retry;
    private Map<String, RateLimit> ratelimit;

    public Retry getRetry() {
      return retry;
    }

    public void setRetry(Retry retry) {
      this.retry = retry;
    }

    public Map<String, RateLimit> getRatelimit() {
      return ratelimit;
    }

    public void setRatelimit(Map<String, RateLimit> ratelimit) {
      this.ratelimit = ratelimit;
    }
  }

  public static class Retry {
    private int retries;

    public int getRetries() {
      return retries;
    }

    public void setRetries(int retries) {
      this.retries = retries;
    }
  }

  public static class RateLimit {
    private int replenishRate;
    private int burstCapacity;

    public int getReplenishRate() {
      return replenishRate;
    }

    public void setReplenishRate(int replenishRate) {
      this.replenishRate = replenishRate;
    }

    public int getBurstCapacity() {
      return burstCapacity;
    }

    public void setBurstCapacity(int burstCapacity) {
      this.burstCapacity = burstCapacity;
    }
  }

  public static class Services {
    private Service quantity;
    private Service auth;

    public Service getQuantity() {
      return quantity;
    }

    public void setQuantity(Service quantity) {
      this.quantity = quantity;
    }

    public Service getAuth() {
      return auth;
    }

    public void setAuth(Service auth) {
      this.auth = auth;
    }
  }

  public static class Service {
    private String name;
    private String url;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }
  }
}

