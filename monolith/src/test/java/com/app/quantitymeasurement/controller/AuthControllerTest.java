package com.app.quantitymeasurement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.quantitymeasurement.auth.controller.AuthController;
import com.app.quantitymeasurement.auth.dto.LoginRequest;
import com.app.quantitymeasurement.auth.service.AuthService;
import com.app.quantitymeasurement.auth.service.OAuth2Service;
import com.app.quantitymeasurement.auth.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private AuthService authService;

  @MockitoBean
  private OAuth2Service oauth2Service;

  @MockitoBean
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void testLogin_InvalidCredentials_ReturnsUnauthorized() throws Exception {
    when(authService.login(any(LoginRequest.class))).thenThrow(new BadCredentialsException("Bad credentials"));

    LoginRequest loginRequest = LoginRequest.builder()
        .username("shantanu")
        .password("wrong-password")
        .build();

    mockMvc.perform(post("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.error").value("Unauthorized"))
        .andExpect(jsonPath("$.message").value("Invalid username or password"));
  }
}


