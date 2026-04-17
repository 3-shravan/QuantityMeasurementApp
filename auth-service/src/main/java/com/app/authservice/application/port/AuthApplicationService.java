package com.app.authservice.application.port;

import com.app.authservice.dto.JwtAuthenticationResponse;
import com.app.authservice.dto.LoginRequest;
import com.app.authservice.dto.SignUpRequest;

public interface AuthApplicationService {

  JwtAuthenticationResponse login(LoginRequest loginRequest);

  JwtAuthenticationResponse register(SignUpRequest signUpRequest);
}


