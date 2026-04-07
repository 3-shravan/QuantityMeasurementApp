package com.app.quantitymeasurement.auth.service;

import com.app.quantitymeasurement.auth.dto.JwtAuthenticationResponse;
import com.app.quantitymeasurement.auth.dto.OAuth2UserInfo;
import com.app.quantitymeasurement.auth.entity.User;
import com.app.quantitymeasurement.auth.repository.UserRepository;
import com.app.quantitymeasurement.auth.security.JwtTokenProvider;
import com.app.quantitymeasurement.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class OAuth2Service {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtTokenProvider tokenProvider;

  @Autowired
  private RestTemplate restTemplate;

  /**
   * Handle OAuth 2 login/registration
   * If user exists, login. If not, create new user.
   */
  public JwtAuthenticationResponse loginOrRegisterOAuth2User(OAuth2UserInfo oauth2UserInfo) {
    String email = oauth2UserInfo.getEmail();

    // Check if user already exists
    User user = userRepository.findByEmail(email)
        .orElseGet(() -> createNewOAuth2User(oauth2UserInfo));

    // Generate JWT token
    String token = tokenProvider.generateToken(user.getUsername());

    return JwtAuthenticationResponse.builder()
        .token(token)
        .username(user.getUsername())
        .userId(user.getId())
        .build();
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

    User user = User.builder()
        .username(finalUsername)
        .email(oauth2UserInfo.getEmail())
        .password("") // OAuth 2 users don't have password
        .enabled(true)
        .build();

    return userRepository.save(user);
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
  public OAuth2UserInfo getGoogleUserInfo(String accessToken) {
    try {
      String url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken;
      GoogleUserResponse response = restTemplate.getForObject(url, GoogleUserResponse.class);

      return OAuth2UserInfo.builder()
          .id(response.getId())
          .email(response.getEmail())
          .name(response.getName())
          .profilePictureUrl(response.getPicture())
          .provider("google")
          .build();
    } catch (Exception e) {
      throw new ValidationException("Failed to fetch Google user info: " + e.getMessage());
    }
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
