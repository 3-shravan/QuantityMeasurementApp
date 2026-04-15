package com.app.quantitymeasurement.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuth2LoginRequest {
  private String provider;
  private String accessToken;
}
