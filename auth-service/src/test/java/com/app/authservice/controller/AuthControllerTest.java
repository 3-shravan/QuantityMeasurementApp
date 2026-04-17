package com.app.authservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.authservice.application.port.AuthApplicationService;
import com.app.authservice.application.port.OAuth2ApplicationService;
import com.app.authservice.dto.JwtAuthenticationResponse;
import com.app.authservice.security.AuthEndpointPolicy;
import com.app.authservice.security.JwtTokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthApplicationService authApplicationService;

  @MockBean
  private OAuth2ApplicationService oAuth2ApplicationService;

  @MockBean
  private JwtTokenProvider jwtTokenProvider;

  @MockBean
  private UserDetailsService userDetailsService;

  @MockBean
  private AuthEndpointPolicy authEndpointPolicy;

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void loginReturnsOkAndTokenPayload() throws Exception {
    JwtAuthenticationResponse response = JwtAuthenticationResponse.builder()
        .token("jwt")
        .username("jane")
        .email("jane@example.com")
        .userId(10L)
        .build();

    when(authApplicationService.login(any())).thenReturn(response);

    mockMvc.perform(post("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"jane\",\"password\":\"secret123\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("jwt"))
        .andExpect(jsonPath("$.username").value("jane"))
        .andExpect(jsonPath("$.email").value("jane@example.com"));
  }

  @Test
  void registerReturnsCreated() throws Exception {
    JwtAuthenticationResponse response = JwtAuthenticationResponse.builder()
        .token("reg-jwt")
        .username("new_user")
        .email("new@example.com")
        .userId(17L)
        .build();

    when(authApplicationService.register(any())).thenReturn(response);

    mockMvc.perform(post("/api/v1/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"new_user\",\"email\":\"new@example.com\",\"password\":\"secret123\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.token").value("reg-jwt"));
  }

  @Test
  void loginValidationErrorReturnsBadRequest() throws Exception {
    mockMvc.perform(post("/api/v1/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"password\":\"secret123\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.message").exists());
  }

  @Test
  void logoutReturnsAuthenticatedUsername() throws Exception {
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken("jane", "N/A"));

    mockMvc.perform(post("/api/v1/auth/logout")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.username").value("jane"));
  }
}


