package com.app.authservice.service;

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
public class AuthService {

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

  public JwtAuthenticationResponse login(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(),
            loginRequest.getPassword()));

    User user = userRepository.findByUsername(authentication.getName())
        .orElseThrow(() -> new RuntimeException("User not found"));

    String token = tokenProvider.generateToken(authentication.getName());

    return JwtAuthenticationResponse.builder()
        .token(token)
        .username(user.getUsername())
        .email(user.getEmail())
        .userId(user.getId())
        .build();
  }

  public JwtAuthenticationResponse register(SignUpRequest signUpRequest) {
    // Check if username already exists
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
    }

    // Check if email already exists
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
    }

    // Create new user
    User user = User.builder()
        .username(signUpRequest.getUsername())
        .email(signUpRequest.getEmail())
        .password(passwordEncoder.encode(signUpRequest.getPassword()))
        .enabled(true)
        .build();

    User savedUser;
    try {
      savedUser = userRepository.save(user);
    } catch (DataIntegrityViolationException ex) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username or email is already registered", ex);
    }

    // Generate JWT token
    String token = tokenProvider.generateToken(savedUser.getUsername());

    return JwtAuthenticationResponse.builder()
        .token(token)
        .username(savedUser.getUsername())
        .email(savedUser.getEmail())
        .userId(savedUser.getId())
        .build();
  }
}

