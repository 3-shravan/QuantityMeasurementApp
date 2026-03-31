package com.app.quantitymeasurement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtAuthenticationResponse {
  private String token;
  @Default
  private String type = "Bearer";
  private String username;
  private Long userId;
}
