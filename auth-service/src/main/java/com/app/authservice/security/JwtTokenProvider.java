package com.app.authservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  @Value("${app.jwt.secret:mySecretKeyForJwtTokenGenerationAndValidationPurposeOnly12345}")
  private String jwtSecret;

  @Value("${app.jwt.expiration:86400000}")
  private long jwtExpirationMs;

  /**
   * Generate JWT token from username
   */
  public String generateToken(String username) {
    return Jwts.builder()
        .subject(username)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
        .signWith(getSigningKey())
        .compact();
  }

  /**
   * Get username from JWT token
   */
  public String getUsernameFromToken(String token) {
    Claims claims = getAllClaimsFromToken(token);
    return claims.getSubject();
  }

  /**
   * Get expiration date from JWT token
   */
  public Date getExpirationDateFromToken(String token) {
    Claims claims = getAllClaimsFromToken(token);
    return claims.getExpiration();
  }

  /**
   * Validate JWT token
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Get all claims from token
   */
  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
   * Get signing key from secret
   */
  private SecretKey getSigningKey() {
    byte[] keyBytes = jwtSecret.getBytes();
    return Keys.hmacShaKeyFor(keyBytes);
  }
}

