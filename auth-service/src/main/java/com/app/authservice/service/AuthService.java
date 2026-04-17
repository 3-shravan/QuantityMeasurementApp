package com.app.authservice.service;

import com.app.authservice.application.port.AuthApplicationService;
import com.app.authservice.dto.JwtAuthenticationResponse;
import com.app.authservice.dto.LoginRequest;
import com.app.authservice.dto.SignUpRequest;
import com.app.authservice.entity.User;
import com.app.authservice.repository.UserRepository;
import com.app.authservice.security.JwtTokenProvider;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class AuthService implements AuthApplicationService {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider tokenProvider;

  public AuthService(
      AuthenticationManager authenticationManager,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtTokenProvider tokenProvider
  ) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.tokenProvider = tokenProvider;
  }

  @Override
  public JwtAuthenticationResponse login(LoginRequest loginRequest) {
    Authentication authentication = authenticate(loginRequest);

    User user = userRepository.findByUsername(authentication.getName())
        .orElseThrow(() -> new RuntimeException("User not found"));

    String token = tokenProvider.generateToken(authentication.getName());
    return toJwtResponse(user, token);
  }

  @Override
  public JwtAuthenticationResponse register(SignUpRequest signUpRequest) {
    // Check if username already exists
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      throw conflict("Username is already taken");
    }

    // Check if email already exists
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      throw conflict("Email is already registered");
    }

    User user = toUser(signUpRequest);

    User savedUser;
    try {
      savedUser = userRepository.save(user);
    } catch (DataIntegrityViolationException ex) {
      throw conflict("Username or email is already registered", ex);
    }

    // Generate JWT token
    String token = tokenProvider.generateToken(savedUser.getUsername());

    return toJwtResponse(savedUser, token);
  }

  private Authentication authenticate(LoginRequest loginRequest) {
    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
  }

  private User toUser(SignUpRequest signUpRequest) {
    return User.builder()
        .username(signUpRequest.getUsername())
        .email(signUpRequest.getEmail())
        .password(passwordEncoder.encode(signUpRequest.getPassword()))
        .enabled(true)
        .build();
  }

  private JwtAuthenticationResponse toJwtResponse(User user, String token) {
    return JwtAuthenticationResponse.builder()
        .token(token)
        .username(user.getUsername())
        .email(user.getEmail())
        .userId(user.getId())
        .build();
  }

  private ResponseStatusException conflict(String message) {
    return new ResponseStatusException(HttpStatus.CONFLICT, message);
  }

  private ResponseStatusException conflict(String message, Throwable cause) {
    return new ResponseStatusException(HttpStatus.CONFLICT, message, cause);
  }
}

