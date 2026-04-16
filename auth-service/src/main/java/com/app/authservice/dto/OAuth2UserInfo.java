package com.app.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuth2UserInfo {
  private String id;
  private String email;
  private String name;
  private String profilePictureUrl;
  private String provider;
}

