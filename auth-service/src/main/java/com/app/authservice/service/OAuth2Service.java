package com.app.authservice.service;

import com.app.authservice.application.port.OAuth2ApplicationService;
import com.app.authservice.dto.JwtAuthenticationResponse;
import com.app.authservice.dto.OAuth2UserInfo;
import com.app.authservice.entity.User;
import com.app.authservice.repository.UserRepository;
import com.app.authservice.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class OAuth2Service implements OAuth2ApplicationService {

  private static final String GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=";

  private final UserRepository userRepository;
  private final JwtTokenProvider tokenProvider;
  private final RestTemplate restTemplate;

  public OAuth2Service(
      UserRepository userRepository,
      JwtTokenProvider tokenProvider,
      RestTemplate restTemplate
  ) {
    this.userRepository = userRepository;
    this.tokenProvider = tokenProvider;
    this.restTemplate = restTemplate;
  }

  /**
   * Handle OAuth 2 login/registration
   * If user exists, login. If not, create new user.
   */
  @Override
  public JwtAuthenticationResponse loginOrRegisterOAuth2User(OAuth2UserInfo oauth2UserInfo) {
    String email = oauth2UserInfo.getEmail();

    User user = userRepository.findByEmail(email)
        .orElseGet(() -> createNewOAuth2User(oauth2UserInfo));

    String token = tokenProvider.generateToken(user.getUsername());
    return toJwtResponse(user, token);
  }

  /**
   * Create new user from OAuth 2 provider
   */
  private User createNewOAuth2User(OAuth2UserInfo oauth2UserInfo) {
    // Generate username from email if name not available
    String username = oauth2UserInfo.getName() != null
        ? oauth2UserInfo.getName().replaceAll("\\s+", "_").toLowerCase()
        : oauth2UserInfo.getEmail().split("@")[0];

    // Ensure username uniqueness
    String finalUsername = ensureUniqueUsername(username);

    return userRepository.save(toOAuthUser(oauth2UserInfo, finalUsername));
  }

  /**
   * Ensure username is unique by appending numbers if needed
   */
  private String ensureUniqueUsername(String baseUsername) {
    String username = baseUsername;
    int counter = 1;

    while (userRepository.existsByUsername(username)) {
      username = baseUsername + "_" + counter;
      counter++;
    }

    return username;
  }

  /**
   * Get Google user info
   */
  @Override
  public OAuth2UserInfo getGoogleUserInfo(String accessToken) {
    if (accessToken == null || accessToken.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Access token is required");
    }

    try {
      String url = GOOGLE_USER_INFO_URL + accessToken;
      GoogleUserResponse response = restTemplate.getForObject(url, GoogleUserResponse.class);

      if (response == null || response.getEmail() == null || response.getEmail().isBlank()) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Google access token");
      }

      return toOAuthUserInfo(response);
    } catch (ResponseStatusException ex) {
      throw ex;
    } catch (RestClientResponseException ex) {
      throw mapGoogleApiException(ex);
    } catch (ResourceAccessException ex) {
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Google OAuth service is unavailable", ex);
    } catch (Exception ex) {
      throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to validate Google access token", ex);
    }
  }

  private ResponseStatusException mapGoogleApiException(RestClientResponseException ex) {
    if (ex.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()
        || ex.getRawStatusCode() == HttpStatus.FORBIDDEN.value()) {
      return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Google access token", ex);
    }
    return new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to validate Google access token", ex);
  }

  private User toOAuthUser(OAuth2UserInfo oauth2UserInfo, String username) {
    return User.builder()
        .username(username)
        .email(oauth2UserInfo.getEmail())
        .password("")
        .enabled(true)
        .build();
  }

  private OAuth2UserInfo toOAuthUserInfo(GoogleUserResponse response) {
    return OAuth2UserInfo.builder()
        .id(response.getId())
        .email(response.getEmail())
        .name(response.getName())
        .profilePictureUrl(response.getPicture())
        .provider("google")
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

  // DTOs for OAuth2 responses
  public static class GoogleUserResponse {
    public String id;
    public String email;
    public String name;
    public String picture;

    public String getId() {
      return id;
    }

    public String getEmail() {
      return email;
    }

    public String getName() {
      return name;
    }

    public String getPicture() {
      return picture;
    }
  }
}
