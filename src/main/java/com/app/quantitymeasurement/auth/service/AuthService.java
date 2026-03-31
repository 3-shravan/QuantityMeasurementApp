package com.app.quantitymeasurement.auth.service;

import com.app.quantitymeasurement.auth.dto.JwtAuthenticationResponse;
import com.app.quantitymeasurement.auth.dto.LoginRequest;
import com.app.quantitymeasurement.auth.dto.SignUpRequest;
import com.app.quantitymeasurement.auth.entity.User;
import com.app.quantitymeasurement.auth.repository.UserRepository;
import com.app.quantitymeasurement.auth.security.JwtTokenProvider;
import com.app.quantitymeasurement.exception.ResourceNotFoundException;
import com.app.quantitymeasurement.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtTokenProvider tokenProvider;

  public JwtAuthenticationResponse login(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(),
            loginRequest.getPassword()));

    User user = userRepository.findByUsername(loginRequest.getUsername())
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    String token = tokenProvider.generateToken(authentication.getName());

    return JwtAuthenticationResponse.builder()
        .token(token)
        .username(user.getUsername())
        .userId(user.getId())
        .build();
  }

  public JwtAuthenticationResponse register(SignUpRequest signUpRequest) {
    // Check if username already exists
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      throw new ValidationException("Username is already taken");
    }

    // Check if email already exists
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      throw new ValidationException("Email is already registered");
    }

    // Create new user
    User user = User.builder()
        .username(signUpRequest.getUsername())
        .email(signUpRequest.getEmail())
        .password(passwordEncoder.encode(signUpRequest.getPassword()))
        .enabled(true)
        .build();

    User savedUser = userRepository.save(user);

    // Generate JWT token
    String token = tokenProvider.generateToken(savedUser.getUsername());

    return JwtAuthenticationResponse.builder()
        .token(token)
        .username(savedUser.getUsername())
        .userId(savedUser.getId())
        .build();
  }
}
