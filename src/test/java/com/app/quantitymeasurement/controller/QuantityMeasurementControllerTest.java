package com.app.quantitymeasurement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.quantitymeasurement.config.SecurityConfig;
import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(QuantityMeasurementController.class)
@Import(SecurityConfig.class)
class QuantityMeasurementControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private IQuantityMeasurementService service;

  @Test
  void compareQuantities_ReturnsOk() throws Exception {
    QuantityMeasurementDTO response = QuantityMeasurementDTO.builder()
        .operation("compare")
        .resultString("true")
        .error(false)
        .build();

    when(service.compare(any(QuantityDTO.class), any(QuantityDTO.class))).thenReturn(response);

    String payload = """
        {
          "thisQuantityDTO": {"value": 1.0, "unit": "FEET", "measurementType": "LengthUnit"},
          "thatQuantityDTO": {"value": 12.0, "unit": "INCHES", "measurementType": "LengthUnit"}
        }
        """;

    mockMvc.perform(post("/api/v1/quantities/compare")
        .contentType(MediaType.APPLICATION_JSON)
        .content(payload))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.operation").value("compare"))
        .andExpect(jsonPath("$.resultString").value("true"));

    Mockito.verify(service).compare(any(QuantityDTO.class), any(QuantityDTO.class));
  }

  @Test
  void getOperationHistory_ReturnsOk() throws Exception {
    QuantityMeasurementDTO row = QuantityMeasurementDTO.builder()
        .operation("add")
        .resultValue(2.0)
        .error(false)
        .build();

    when(service.getOperationHistory(any())).thenReturn(List.of(row));

    mockMvc.perform(get("/api/v1/quantities/history/operation/ADD"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].operation").value("add"));
  }

  @Test
  void compareQuantities_InvalidInput_ReturnsBadRequest() throws Exception {
    String payload = """
        {
          "thisQuantityDTO": {"value": 1.0, "unit": "FOOT", "measurementType": "LengthUnit"},
          "thatQuantityDTO": {"value": 12.0, "unit": "INCHES", "measurementType": "LengthUnit"}
        }
        """;

    mockMvc.perform(post("/api/v1/quantities/compare")
        .contentType(MediaType.APPLICATION_JSON)
        .content(payload))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Quantity Measurement Error"));
  }
}
