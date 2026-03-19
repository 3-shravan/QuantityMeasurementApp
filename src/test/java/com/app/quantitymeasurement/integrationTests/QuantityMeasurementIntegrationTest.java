package com.app.quantitymeasurement.integrationTests;

import static org.assertj.core.api.Assertions.assertThat;

import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuantityMeasurementIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void compareEndpoint_ReturnsSuccess() {
    String payload = """
        {
          "thisQuantityDTO": {"value": 1.0, "unit": "FEET", "measurementType": "LengthUnit"},
          "thatQuantityDTO": {"value": 12.0, "unit": "INCHES", "measurementType": "LengthUnit"}
        }
        """;

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    ResponseEntity<QuantityMeasurementDTO> response = restTemplate.exchange(
        "/api/v1/quantities/compare",
        HttpMethod.POST,
        new HttpEntity<>(payload, headers),
        QuantityMeasurementDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().isError()).isFalse();
  }

  @Test
  void addEndpoint_InvalidCategory_ReturnsBadRequest() {
    String payload = """
        {
          "thisQuantityDTO": {"value": 1.0, "unit": "FEET", "measurementType": "LengthUnit"},
          "thatQuantityDTO": {"value": 1.0, "unit": "KILOGRAM", "measurementType": "WeightUnit"}
        }
        """;

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    ResponseEntity<String> response = restTemplate.exchange(
        "/api/v1/quantities/add",
        HttpMethod.POST,
        new HttpEntity<>(payload, headers),
        String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody()).contains("Quantity Measurement Error");
  }
}
