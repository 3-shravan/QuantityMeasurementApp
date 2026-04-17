package com.app.quantityservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.quantityservice.application.port.QuantityApplicationService;
import com.app.quantityservice.exception.QuantityMeasurementException;
import com.app.quantityservice.model.QuantityMeasurementDTO;
import com.app.quantityservice.security.JwtAuthenticationFilter;
import com.app.quantityservice.security.JwtTokenProvider;
import com.app.quantityservice.security.QuantityEndpointPolicy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(QuantityMeasurementController.class)
@AutoConfigureMockMvc(addFilters = false)
class QuantityMeasurementControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private QuantityApplicationService quantityApplicationService;

  @MockBean
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @MockBean
  private JwtTokenProvider jwtTokenProvider;

  @MockBean
  private QuantityEndpointPolicy quantityEndpointPolicy;

  @Test
  void compareReturnsOkForValidPayload() throws Exception {
    QuantityMeasurementDTO response = QuantityMeasurementDTO.builder()
        .operation("compare")
        .resultString("true")
        .error(false)
        .build();

    when(quantityApplicationService.compare(any(), any())).thenReturn(response);

    mockMvc.perform(post("/api/v1/quantities/compare")
            .contentType(MediaType.APPLICATION_JSON)
            .content(validPayload()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.operation").value("compare"))
        .andExpect(jsonPath("$.resultString").value("true"));
  }

  @Test
  void compareReturnsBadRequestForInvalidPayload() throws Exception {
    String invalid = """
        {
          "thisQuantityDTO": {
            "value": 5,
            "measurementType": "LengthUnit"
          },
          "thatQuantityDTO": {
            "value": 2,
            "unit": "INCHES",
            "measurementType": "LengthUnit"
          }
        }
        """;

    mockMvc.perform(post("/api/v1/quantities/compare")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalid))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.message").exists());
  }

  @Test
  void compareBusinessErrorReturnsBadRequestContract() throws Exception {
    when(quantityApplicationService.compare(any(), any()))
        .thenThrow(new QuantityMeasurementException("Cannot compare categories"));

    mockMvc.perform(post("/api/v1/quantities/compare")
            .contentType(MediaType.APPLICATION_JSON)
            .content(validPayload()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.message").value("Cannot compare categories"));
  }

  private String validPayload() {
    return """
        {
          "thisQuantityDTO": {
            "value": 12,
            "unit": "INCHES",
            "measurementType": "LengthUnit"
          },
          "thatQuantityDTO": {
            "value": 1,
            "unit": "FEET",
            "measurementType": "LengthUnit"
          }
        }
        """;
  }
}


