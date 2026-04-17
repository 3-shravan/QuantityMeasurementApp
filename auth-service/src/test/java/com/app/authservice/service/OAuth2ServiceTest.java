package com.app.authservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.authservice.dto.JwtAuthenticationResponse;
import com.app.authservice.dto.OAuth2UserInfo;
import com.app.authservice.entity.User;
import com.app.authservice.repository.UserRepository;
import com.app.authservice.security.JwtTokenProvider;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class OAuth2ServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private JwtTokenProvider tokenProvider;

  @Mock
  private RestTemplate restTemplate;

  private OAuth2Service oauth2Service;

  @BeforeEach
  void setUp() {
    oauth2Service = new OAuth2Service(userRepository, tokenProvider, restTemplate);
  }

  @Test
  void getGoogleUserInfoRejectsBlankAccessToken() {
    ResponseStatusException ex = assertThrows(ResponseStatusException.class,
        () -> oauth2Service.getGoogleUserInfo(" "));

    assertEquals(HttpStatus.BAD_REQUEST, HttpStatus.valueOf(ex.getStatusCode().value()));
    assertTrue(ex.getReason().contains("Access token is required"));
  }

  @Test
  void getGoogleUserInfoMapsUnauthorizedGoogleResponse() {
    HttpClientErrorException unauthorized = HttpClientErrorException.create(
        HttpStatus.UNAUTHORIZED, "Unauthorized", HttpHeaders.EMPTY, new byte[0], StandardCharsets.UTF_8);

    when(restTemplate.getForObject(any(String.class), any())).thenThrow(unauthorized);

    ResponseStatusException ex = assertThrows(ResponseStatusException.class,
        () -> oauth2Service.getGoogleUserInfo("invalid-token"));

    assertEquals(HttpStatus.UNAUTHORIZED, HttpStatus.valueOf(ex.getStatusCode().value()));
    assertTrue(ex.getReason().contains("Invalid Google access token"));
  }

  @Test
  void loginOrRegisterUsesExistingUserWhenEmailExists() {
    OAuth2UserInfo userInfo = OAuth2UserInfo.builder().email("john@example.com").name("John").build();
    User user = User.builder().id(5L).username("john").email("john@example.com").enabled(true).build();

    when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
    when(tokenProvider.generateToken("john")).thenReturn("oauth-jwt");

    JwtAuthenticationResponse response = oauth2Service.loginOrRegisterOAuth2User(userInfo);

    assertEquals("oauth-jwt", response.getToken());
    assertEquals("john", response.getUsername());
    assertEquals(5L, response.getUserId());
  }

  @Test
  void loginOrRegisterCreatesUserWhenEmailDoesNotExist() {
    OAuth2UserInfo userInfo = OAuth2UserInfo.builder().email("new@example.com").name("New User").build();
    User saved = User.builder().id(9L).username("new_user").email("new@example.com").enabled(true).build();

    when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
    when(userRepository.existsByUsername("new_user")).thenReturn(false);
    when(userRepository.save(any(User.class))).thenReturn(saved);
    when(tokenProvider.generateToken("new_user")).thenReturn("new-jwt");

    JwtAuthenticationResponse response = oauth2Service.loginOrRegisterOAuth2User(userInfo);

    assertEquals("new-jwt", response.getToken());
    assertEquals("new_user", response.getUsername());
    verify(userRepository).save(any(User.class));
  }

  @Test
  void loginOrRegisterAppendsCounterWhenGeneratedUsernameAlreadyExists() {
    OAuth2UserInfo userInfo = OAuth2UserInfo.builder().email("john2@example.com").name("John").build();
    User saved = User.builder().id(22L).username("john_1").email("john2@example.com").enabled(true).build();

    when(userRepository.findByEmail("john2@example.com")).thenReturn(Optional.empty());
    when(userRepository.existsByUsername("john")).thenReturn(true);
    when(userRepository.existsByUsername("john_1")).thenReturn(false);
    when(userRepository.save(any(User.class))).thenReturn(saved);
    when(tokenProvider.generateToken("john_1")).thenReturn("john-counter-token");

    JwtAuthenticationResponse response = oauth2Service.loginOrRegisterOAuth2User(userInfo);

    assertEquals("john_1", response.getUsername());
    assertEquals("john-counter-token", response.getToken());
  }

  @Test
  void getGoogleUserInfoRejectsResponseWithoutEmail() {
    OAuth2Service.GoogleUserResponse response = new OAuth2Service.GoogleUserResponse();
    response.id = "id-1";
    response.name = "No Mail";

    when(restTemplate.getForObject(any(String.class), any())).thenReturn(response);

    ResponseStatusException ex = assertThrows(ResponseStatusException.class,
        () -> oauth2Service.getGoogleUserInfo("token-without-email"));

    assertEquals(HttpStatus.UNAUTHORIZED, HttpStatus.valueOf(ex.getStatusCode().value()));
    assertTrue(ex.getReason().contains("Invalid Google access token"));
  }
}

