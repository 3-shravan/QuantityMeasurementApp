package com.app.quantitymeasurement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.auth.security.JwtAuthenticationFilter;
import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityInputDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(QuantityMeasurementController.class)
@AutoConfigureMockMvc(addFilters = false)
class QuantityMeasurementControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private IQuantityMeasurementService service;

  @MockitoBean
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  private QuantityInputDTO quantityInput;
  private QuantityMeasurementDTO measurementResult;

  @BeforeEach
  void setUp() {
    quantityInput = new QuantityInputDTO();
    quantityInput.setThisQuantityDTO(new QuantityDTO(1.0, "FEET", "LengthUnit", null));
    quantityInput.setThatQuantityDTO(new QuantityDTO(12.0, "INCHES", "LengthUnit", null));

    measurementResult = QuantityMeasurementDTO.builder()
        .operation("compare")
        .resultString("true")
        .error(false)
        .build();
  }

  @Test
  void testCompareQuantities_Success() throws Exception {
    when(service.compare(any(QuantityDTO.class), any(QuantityDTO.class))).thenReturn(measurementResult);

    mockMvc.perform(post("/api/v1/quantities/compare")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(quantityInput)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.operation").value("compare"))
        .andExpect(jsonPath("$.resultString").value("true"));

    verify(service).compare(any(QuantityDTO.class), any(QuantityDTO.class));
  }

  @Test
  void testAddQuantities_Success() throws Exception {
    QuantityMeasurementDTO addResult = QuantityMeasurementDTO.builder()
        .operation("add")
        .resultValue(2.0)
        .resultUnit("FEET")
        .resultMeasurementType("LengthUnit")
        .error(false)
        .build();

    when(service.add(any(QuantityDTO.class), any(QuantityDTO.class))).thenReturn(addResult);

    mockMvc.perform(post("/api/v1/quantities/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(quantityInput)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.resultValue").value(2.0));

    verify(service).add(any(QuantityDTO.class), any(QuantityDTO.class));
  }

  @Test
  void testGetOperationHistory_Success() throws Exception {
    when(service.getOperationHistory(eq("add"))).thenReturn(List.of());

    mockMvc.perform(get("/api/v1/quantities/history/operation/ADD"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(0));

    verify(service).getOperationHistory(eq("add"));
  }

  @Test
  void testGetOperationCount_Success() throws Exception {
    when(service.getOperationCount(eq("ADD"))).thenReturn(0L);

    mockMvc.perform(get("/api/v1/quantities/count/ADD")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("0"));

    verify(service).getOperationCount(eq("ADD"));
  }

  @Test
  void testCompareQuantities_InvalidInput_ReturnsBadRequest() throws Exception {
    QuantityInputDTO invalidInput = new QuantityInputDTO();
    invalidInput.setThisQuantityDTO(new QuantityDTO(1.0, "FOOT", "LengthUnit", null));
    invalidInput.setThatQuantityDTO(new QuantityDTO(12.0, "INCHES", "LengthUnit", null));

    mockMvc.perform(post("/api/v1/quantities/compare")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidInput)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Quantity Measurement Error"));
  }

  @Test
  void testCompareQuantities_ServiceException_ReturnsBadRequest() throws Exception {
    when(service.compare(any(QuantityDTO.class), any(QuantityDTO.class)))
        .thenThrow(new QuantityMeasurementException("compare Error: test"));

    mockMvc.perform(post("/api/v1/quantities/compare")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(quantityInput)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Quantity Measurement Error"));
  }

  @Test
  void testCompareQuantities_UnexpectedException_ReturnsInternalServerError() throws Exception {
    when(service.compare(any(QuantityDTO.class), any(QuantityDTO.class)))
        .thenThrow(new RuntimeException("unexpected"));

    mockMvc.perform(post("/api/v1/quantities/compare")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(quantityInput)))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.error").value("Internal Server Error"));
  }

  @Test
  void testMockMvc_ComparisonTest() throws Exception {
    when(service.compare(any(QuantityDTO.class), any(QuantityDTO.class))).thenReturn(measurementResult);

    mockMvc.perform(post("/api/v1/quantities/compare")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(quantityInput)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.resultString").value("true"));
  }

  @Test
  void testMockMvc_ResponseAssertion() throws Exception {
    when(service.compare(any(QuantityDTO.class), any(QuantityDTO.class))).thenReturn(measurementResult);

    mockMvc.perform(post("/api/v1/quantities/compare")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(quantityInput)))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.operation").value("compare"))
        .andExpect(jsonPath("$.error").value(false));
  }
}
