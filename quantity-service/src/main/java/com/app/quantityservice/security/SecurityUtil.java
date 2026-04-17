package com.app.quantityservice.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.util.StringUtils;

@Component
public class SecurityUtil {

  private final JwtTokenProvider tokenProvider;

  public SecurityUtil(JwtTokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  public String getCurrentUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
      String name = authentication.getName();
      if (StringUtils.hasText(name) && !"anonymousUser".equals(name)) {
        return name;
      }

      Object principal = authentication.getPrincipal();
      if (principal instanceof UserDetails userDetails) {
        return userDetails.getUsername();
      }
      if (principal instanceof String principalText && StringUtils.hasText(principalText)
          && !"anonymousUser".equals(principalText)) {
        return principalText;
      }
    }

    return resolveUsernameFromBearerToken();
  }

  private String resolveUsernameFromBearerToken() {
    RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
    if (!(attributes instanceof ServletRequestAttributes servletAttributes)) {
      return null;
    }

    HttpServletRequest request = servletAttributes.getRequest();
    if (request == null) {
      return null;
    }

    String authorization = request.getHeader("Authorization");
    if (!StringUtils.hasText(authorization) || !authorization.startsWith("Bearer ")) {
      return null;
    }

    String token = authorization.substring(7);
    if (!StringUtils.hasText(token) || !tokenProvider.validateToken(token)) {
      return null;
    }

    try {
      return tokenProvider.getUsernameFromToken(token);
    } catch (Exception ex) {
      return null;
    }
  }
}
