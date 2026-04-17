package com.app.authservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.authservice.dto.JwtAuthenticationResponse;
import com.app.authservice.dto.LoginRequest;
import com.app.authservice.dto.SignUpRequest;
import com.app.authservice.entity.User;
import com.app.authservice.repository.UserRepository;
import com.app.authservice.security.JwtTokenProvider;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtTokenProvider tokenProvider;

  @Mock
  private Authentication authentication;

  private AuthService authService;

  @BeforeEach
  void setUp() {
    authService = new AuthService(authenticationManager, userRepository, passwordEncoder, tokenProvider);
  }

  @Test
  void loginReturnsTokenForValidUsernameAndPassword() {
    LoginRequest request = LoginRequest.builder().username("jane").password("secret123").build();
    User user = User.builder().id(11L).username("jane").email("jane@example.com").password("hash").enabled(true).build();

    when(authenticationManager.authenticate(any())).thenReturn(authentication);
    when(authentication.getName()).thenReturn("jane");
    when(userRepository.findByUsername("jane")).thenReturn(Optional.of(user));
    when(tokenProvider.generateToken("jane")).thenReturn("jwt-token");

    JwtAuthenticationResponse response = authService.login(request);

    assertEquals("jwt-token", response.getToken());
    assertEquals("jane", response.getUsername());
    assertEquals("jane@example.com", response.getEmail());
    assertEquals(11L, response.getUserId());
    verify(authenticationManager).authenticate(any());
  }

  @Test
  void registerThrowsConflictWhenUsernameAlreadyExists() {
    SignUpRequest request = SignUpRequest.builder().username("taken").email("x@example.com").password("secret123").build();
    when(userRepository.existsByUsername("taken")).thenReturn(true);

    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> authService.register(request));

    assertEquals(HttpStatus.CONFLICT, HttpStatus.valueOf(ex.getStatusCode().value()));
    assertTrue(ex.getReason().contains("Username is already taken"));
  }

  @Test
  void registerThrowsConflictWhenEmailAlreadyExists() {
    SignUpRequest request = SignUpRequest.builder().username("free").email("used@example.com").password("secret123").build();
    when(userRepository.existsByUsername("free")).thenReturn(false);
    when(userRepository.existsByEmail("used@example.com")).thenReturn(true);

    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> authService.register(request));

    assertEquals(HttpStatus.CONFLICT, HttpStatus.valueOf(ex.getStatusCode().value()));
    assertTrue(ex.getReason().contains("Email is already registered"));
  }

  @Test
  void registerMapsDataIntegrityViolationToConflict() {
    SignUpRequest request = SignUpRequest.builder().username("free").email("free@example.com").password("secret123").build();

    when(userRepository.existsByUsername("free")).thenReturn(false);
    when(userRepository.existsByEmail("free@example.com")).thenReturn(false);
    when(passwordEncoder.encode("secret123")).thenReturn("encoded");
    when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException("duplicate"));

    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> authService.register(request));

    assertEquals(HttpStatus.CONFLICT, HttpStatus.valueOf(ex.getStatusCode().value()));
    assertTrue(ex.getReason().contains("Username or email is already registered"));
  }

  @Test
  void registerReturnsTokenForNewUser() {
    SignUpRequest request = SignUpRequest.builder().username("new_user").email("new@example.com").password("secret123").build();
    User saved = User.builder().id(17L).username("new_user").email("new@example.com").password("encoded").enabled(true).build();

    when(userRepository.existsByUsername("new_user")).thenReturn(false);
    when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
    when(passwordEncoder.encode("secret123")).thenReturn("encoded");
    when(userRepository.save(any(User.class))).thenReturn(saved);
    when(tokenProvider.generateToken("new_user")).thenReturn("new-user-token");

    JwtAuthenticationResponse response = authService.register(request);

    assertNotNull(response);
    assertEquals("new-user-token", response.getToken());
    assertEquals("new_user", response.getUsername());
    assertEquals("new@example.com", response.getEmail());
    assertEquals(17L, response.getUserId());
  }

  @Test
  void loginThrowsRuntimeExceptionWhenAuthenticatedUserMissingInRepository() {
    LoginRequest request = LoginRequest.builder().username("ghost").password("secret123").build();

    when(authenticationManager.authenticate(any())).thenReturn(authentication);
    when(authentication.getName()).thenReturn("ghost");
    when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

    RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.login(request));

    assertEquals("User not found", ex.getMessage());
  }
}

