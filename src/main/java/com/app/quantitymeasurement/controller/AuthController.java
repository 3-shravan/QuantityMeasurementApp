package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.model.JwtAuthenticationResponse;
import com.app.quantitymeasurement.model.LoginRequest;
import com.app.quantitymeasurement.model.LogoutResponse;
import com.app.quantitymeasurement.model.SignUpRequest;
import com.app.quantitymeasurement.service.AuthService;
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
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "API for user authentication and registration")
public class AuthController {

  @Autowired
  private AuthService authService;

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
    String username = authentication != null ? authentication.getName() : "Unknown";

    SecurityContextHolder.clearContext();

    LogoutResponse response = LogoutResponse.builder()
        .message("Successfully logged out. Please remove the JWT token from client side.")
        .success(true)
        .username(username)
        .build();

    return ResponseEntity.ok(response);
  }
}
