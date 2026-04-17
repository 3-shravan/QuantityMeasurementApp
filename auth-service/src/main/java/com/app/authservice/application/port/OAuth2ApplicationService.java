package com.app.authservice.application.port;

import com.app.authservice.dto.JwtAuthenticationResponse;
import com.app.authservice.dto.OAuth2UserInfo;

public interface OAuth2ApplicationService {

  JwtAuthenticationResponse loginOrRegisterOAuth2User(OAuth2UserInfo oauth2UserInfo);

  OAuth2UserInfo getGoogleUserInfo(String accessToken);
}


