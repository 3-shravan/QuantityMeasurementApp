package com.app.authservice.controller;

import com.app.authservice.dto.JwtAuthenticationResponse;
import com.app.authservice.dto.LoginRequest;
import com.app.authservice.dto.LogoutResponse;
import com.app.authservice.dto.OAuth2LoginRequest;
import com.app.authservice.dto.OAuth2UserInfo;
import com.app.authservice.dto.SignUpRequest;
import com.app.authservice.service.AuthService;
import com.app.authservice.service.OAuth2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/v1/auth", "/auth-service/api/v1/auth"})
@Tag(name = "Authentication", description = "API for user authentication and registration")
public class AuthController {

  @Autowired
  private AuthService authService;

  @Autowired
  private OAuth2Service oauth2Service;

  @PostMapping("/login")
  @Operation(summary = "Login user", description = "Authenticate user with username and password, returns JWT token")
  public ResponseEntity<JwtAuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
    JwtAuthenticationResponse response = authService.login(loginRequest);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/register")
  @Operation(summary = "Register new user", description = "Create a new user account and returns JWT token")
  public ResponseEntity<JwtAuthenticationResponse> register(@Valid @RequestBody SignUpRequest signUpRequest) {
    JwtAuthenticationResponse response = authService.register(signUpRequest);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PostMapping("/logout")
  @Operation(summary = "Logout user", description = "Logout user and invalidate session")
  @SecurityRequirement(name = "Bearer Authentication")
  public ResponseEntity<LogoutResponse> logout(HttpServletRequest request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = resolveUsername(authentication);

    SecurityContextHolder.clearContext();

    LogoutResponse response = LogoutResponse.builder()
        .message("Successfully logged out. Please remove the JWT token from client side.")
        .success(true)
        .username(username)
        .build();

    return ResponseEntity.ok(response);
  }

  private String resolveUsername(Authentication authentication) {
    if (authentication == null) {
      return "Unknown";
    }

    try {
      String name = authentication.getName();
      return (name == null || name.isBlank()) ? "Unknown" : name;
    } catch (Exception ex) {
      return "Unknown";
    }
  }

  @PostMapping("/oauth2/google")
  @Operation(summary = "Login with Google", description = "Authenticate user with Google OAuth 2 access token")
  public ResponseEntity<JwtAuthenticationResponse> loginWithGoogle(@Valid @RequestBody OAuth2LoginRequest request) {
    OAuth2UserInfo userInfo = oauth2Service.getGoogleUserInfo(request.getAccessToken());
    JwtAuthenticationResponse response = oauth2Service.loginOrRegisterOAuth2User(userInfo);
    return ResponseEntity.ok(response);
  }
}

