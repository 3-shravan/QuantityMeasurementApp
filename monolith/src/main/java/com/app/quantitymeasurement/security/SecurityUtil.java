package com.app.quantitymeasurement.security;

import com.app.quantitymeasurement.auth.entity.User;
import com.app.quantitymeasurement.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Utility class for security-related operations.
 * Provides methods to retrieve current authenticated user information from
 * SecurityContext.
 */
@Component
@RequiredArgsConstructor
public class SecurityUtil {

  private final UserRepository userRepository;

  /**
   * Get the username of the currently authenticated user.
   *
   * @return the username if user is authenticated, null otherwise
   */
  public static String getCurrentUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    }

    Object principal = authentication.getPrincipal();
    if (principal instanceof UserDetails) {
      return ((UserDetails) principal).getUsername();
    } else if (principal instanceof String) {
      return (String) principal;
    }

    return null;
  }

  /**
   * Check if the current user is authenticated.
   *
   * @return true if user is authenticated, false otherwise
   */
  public static boolean isAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null && authentication.isAuthenticated();
  }

  /**
   * Get the UserDetails of the currently authenticated user.
   *
   * @return UserDetails if user is authenticated, null otherwise
   */
  public static UserDetails getCurrentUserDetails() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    }

    Object principal = authentication.getPrincipal();
    if (principal instanceof UserDetails) {
      return (UserDetails) principal;
    }

    return null;
  }

  /**
   * Get the user ID of the currently authenticated user.
   *
   * @return user ID if user is authenticated, null otherwise
   */
  public Long getCurrentUserId() {
    String username = getCurrentUsername();
    if (username == null) {
      return null;
    }

    return userRepository.findByUsername(username)
        .map(User::getId)
        .orElse(null);
  }
}
