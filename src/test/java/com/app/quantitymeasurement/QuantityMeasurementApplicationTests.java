package com.app.quantitymeasurement;

import static org.assertj.core.api.Assertions.assertThat;

import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityInputDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuantityMeasurementApplicationTests {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private QuantityMeasurementRepository repository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private String baseUrl() {
    return "http://localhost:" + port + "/api/v1/quantities";
  }

  private QuantityInputDTO input(
      double thisValue, String thisUnit, String thisMeasurementType,
      double thatValue, String thatUnit, String thatMeasurementType) {
    QuantityInputDTO inputDTO = new QuantityInputDTO();
    inputDTO.setThisQuantityDTO(new QuantityDTO(thisValue, thisUnit, thisMeasurementType, null));
    inputDTO.setThatQuantityDTO(new QuantityDTO(thatValue, thatUnit, thatMeasurementType, null));
    return inputDTO;
  }

  private HttpEntity<QuantityInputDTO> jsonEntity(QuantityInputDTO body) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<>(body, headers);
  }

  @Test
  @Order(1)
  void contextLoads() {
    assertThat(restTemplate).isNotNull();
    assertThat(port).isGreaterThan(0);
  }

  @Test
  @Order(2)
  void compareEndpoint_ReturnsSuccess() {
    QuantityInputDTO body = input(1.0, "FEET", "LengthUnit", 12.0, "INCHES", "LengthUnit");

    ResponseEntity<QuantityMeasurementDTO> response = restTemplate.exchange(
        baseUrl() + "/compare",
        HttpMethod.POST,
        jsonEntity(body),
        QuantityMeasurementDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().isError()).isFalse();
    assertThat(response.getBody().getResultString()).isEqualTo("true");
  }

  @Test
  @Order(3)
  void addEndpoint_ReturnsSuccess() {
    QuantityInputDTO body = input(1.0, "GALLON", "VolumeUnit", 3.78541, "LITRE", "VolumeUnit");

    ResponseEntity<QuantityMeasurementDTO> response = restTemplate.exchange(
        baseUrl() + "/add",
        HttpMethod.POST,
        jsonEntity(body),
        QuantityMeasurementDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().isError()).isFalse();
    assertThat(response.getBody().getResultValue()).isNotNull();
  }

  @Test
  @Order(4)
  void operationHistoryEndpoint_ReturnsData() {
    ResponseEntity<QuantityMeasurementDTO[]> response = restTemplate.getForEntity(
        baseUrl() + "/history/operation/COMPARE",
        QuantityMeasurementDTO[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    List<QuantityMeasurementDTO> items = List.of(response.getBody());
    assertThat(items).isNotEmpty();
  }

  @Test
  @Order(5)
  void operationCountEndpoint_ReturnsValidCount() {
    ResponseEntity<Long> response = restTemplate.getForEntity(
        baseUrl() + "/count/COMPARE",
        Long.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody()).isGreaterThanOrEqualTo(0L);
  }

  @Test
  @Order(6)
  void addEndpoint_InvalidCategory_ReturnsBadRequest() {
    QuantityInputDTO body = input(1.0, "FEET", "LengthUnit", 1.0, "KILOGRAM", "WeightUnit");

    ResponseEntity<String> response = restTemplate.exchange(
        baseUrl() + "/add",
        HttpMethod.POST,
        jsonEntity(body),
        String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody()).contains("Quantity Measurement Error");
  }

  @Test
  @Order(7)
  @DisplayName("POST /convert - converts 100 Celsius to 212 Fahrenheit")
  void convertEndpoint_ReturnsSuccess() {
    QuantityInputDTO body = input(100.0, "CELSIUS", "TemperatureUnit", 0.0, "FAHRENHEIT", "TemperatureUnit");

    ResponseEntity<QuantityMeasurementDTO> response = restTemplate.exchange(
        baseUrl() + "/convert",
        HttpMethod.POST,
        jsonEntity(body),
        QuantityMeasurementDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getResultValue()).isEqualTo(212.0);
  }

  @Test
  @Order(8)
  @DisplayName("POST /subtract - 2 feet minus 12 inches equals 1 foot")
  void subtractEndpoint_ReturnsSuccess() {
    QuantityInputDTO body = input(2.0, "FEET", "LengthUnit", 12.0, "INCHES", "LengthUnit");

    ResponseEntity<QuantityMeasurementDTO> response = restTemplate.exchange(
        baseUrl() + "/subtract",
        HttpMethod.POST,
        jsonEntity(body),
        QuantityMeasurementDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getResultValue()).isEqualTo(1.0);
  }

  @Test
  @Order(9)
  @DisplayName("POST /divide - 1 yard divided by 1 foot equals 3")
  void divideEndpoint_ReturnsSuccess() {
    QuantityInputDTO body = input(1.0, "YARDS", "LengthUnit", 1.0, "FEET", "LengthUnit");

    ResponseEntity<QuantityMeasurementDTO> response = restTemplate.exchange(
        baseUrl() + "/divide",
        HttpMethod.POST,
        jsonEntity(body),
        QuantityMeasurementDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getResultValue()).isEqualTo(3.0);
  }

  @Test
  @Order(10)
  @DisplayName("GET /history/type/TemperatureUnit - returns non-empty history")
  void historyByTypeEndpoint_ReturnsData() {
    ResponseEntity<QuantityMeasurementDTO[]> response = restTemplate.getForEntity(
        baseUrl() + "/history/type/TemperatureUnit",
        QuantityMeasurementDTO[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(List.of(response.getBody())).isNotEmpty();
  }

  @Test
  @Order(11)
  @DisplayName("POST /divide by zero returns 500 and is captured in errored history")
  void divideByZero_CapturedInErroredHistory() {
    QuantityInputDTO body = input(1.0, "YARDS", "LengthUnit", 0.0, "FEET", "LengthUnit");

    ResponseEntity<String> divideResponse = restTemplate.exchange(
        baseUrl() + "/divide",
        HttpMethod.POST,
        jsonEntity(body),
        String.class);

    assertThat(divideResponse.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(divideResponse.getBody()).contains("Divide by zero");

    ResponseEntity<QuantityMeasurementDTO[]> historyResponse = restTemplate.getForEntity(
        baseUrl() + "/history/errored",
        QuantityMeasurementDTO[].class);

    assertThat(historyResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(historyResponse.getBody()).isNotNull();
    assertThat(List.of(historyResponse.getBody())).isNotEmpty();
  }

  @Test
  @Order(12)
  @DisplayName("POST /compare with invalid unit returns 400")
  void compare_InvalidUnit_ReturnsBadRequest() {
    QuantityInputDTO body = input(1.0, "FOOT", "LengthUnit", 12.0, "INCHES", "LengthUnit");

    ResponseEntity<String> response = restTemplate.exchange(
        baseUrl() + "/compare",
        HttpMethod.POST,
        jsonEntity(body),
        String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).contains("Unit must be valid for the specified measurement type");
  }

  @Test
  @Order(13)
  @DisplayName("POST /compare with invalid measurementType returns 400")
  void compare_InvalidMeasurementType_ReturnsBadRequest() {
    QuantityInputDTO body = input(1.0, "FEET", "Length", 12.0, "INCHES", "LengthUnit");

    ResponseEntity<String> response = restTemplate.exchange(
        baseUrl() + "/compare",
        HttpMethod.POST,
        jsonEntity(body),
        String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody())
        .contains("Measurement type must be LengthUnit, VolumeUnit, WeightUnit, or TemperatureUnit");
  }

  @Test
  @Order(14)
  @DisplayName("POST /compare - 1 foot does NOT equal 1 inch")
  void compare_FootNotEqualInch_ReturnsFalse() {
    QuantityInputDTO body = input(1.0, "FEET", "LengthUnit", 1.0, "INCHES", "LengthUnit");

    ResponseEntity<QuantityMeasurementDTO> response = restTemplate.exchange(
        baseUrl() + "/compare",
        HttpMethod.POST,
        jsonEntity(body),
        QuantityMeasurementDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getResultString()).isEqualTo("false");
  }

  @Test
  @Order(15)
  @DisplayName("POST /compare - 1 gallon equals 3.785 litres")
  void compare_GallonEqualsLitres_ReturnsTrue() {
    QuantityInputDTO body = input(1.0, "GALLON", "VolumeUnit", 3.785, "LITRE", "VolumeUnit");

    ResponseEntity<QuantityMeasurementDTO> response = restTemplate.exchange(
        baseUrl() + "/compare",
        HttpMethod.POST,
        jsonEntity(body),
        QuantityMeasurementDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getResultString()).isEqualTo("true");
  }

  @Test
  @Order(16)
  @DisplayName("POST /compare - 212 Fahrenheit equals 100 Celsius")
  void compare_FahrenheitEqualsCelsius_ReturnsTrue() {
    QuantityInputDTO body = input(212.0, "FAHRENHEIT", "TemperatureUnit", 100.0, "CELSIUS", "TemperatureUnit");

    ResponseEntity<QuantityMeasurementDTO> response = restTemplate.exchange(
        baseUrl() + "/compare",
        HttpMethod.POST,
        jsonEntity(body),
        QuantityMeasurementDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getResultString()).isEqualTo("true");
  }

  @Test
  @Order(17)
  @DisplayName("GET /history/operation/CONVERT - returns convert history")
  void historyByOperation_Convert_ReturnsData() {
    ResponseEntity<QuantityMeasurementDTO[]> response = restTemplate.getForEntity(
        baseUrl() + "/history/operation/CONVERT",
        QuantityMeasurementDTO[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(List.of(response.getBody())).isNotEmpty();
  }

  @Test
  @Order(18)
  @DisplayName("GET /count/DIVIDE - returns count of divide operations")
  void operationCount_Divide_ReturnsPositiveCount() {
    ResponseEntity<Long> response = restTemplate.getForEntity(
        baseUrl() + "/count/DIVIDE",
        Long.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody()).isGreaterThan(0L);
  }

  @Test
  void testSpringBootApplicationStarts() {
    assertThat(restTemplate).isNotNull();
    assertThat(port).isGreaterThan(0);
  }

  @Test
  void testRestEndpointCompareQuantities() {
    QuantityInputDTO body = input(1.0, "FEET", "LengthUnit", 12.0, "INCHES", "LengthUnit");

    ResponseEntity<QuantityMeasurementDTO> response = restTemplate.exchange(
        baseUrl() + "/compare",
        HttpMethod.POST,
        jsonEntity(body),
        QuantityMeasurementDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getResultString()).isNotNull();
  }

  @Test
  void testRestEndpointConvertQuantities() {
    QuantityInputDTO body = input(100.0, "CELSIUS", "TemperatureUnit", 0.0, "FAHRENHEIT", "TemperatureUnit");

    ResponseEntity<QuantityMeasurementDTO> response = restTemplate.exchange(
        baseUrl() + "/convert?targetUnit=FAHRENHEIT",
        HttpMethod.POST,
        jsonEntity(body),
        QuantityMeasurementDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getResultValue()).isEqualTo(212.0);
  }

  @Test
  void testRestEndpointAddQuantities() {
    QuantityInputDTO body = input(1.0, "FEET", "LengthUnit", 12.0, "INCHES", "LengthUnit");

    ResponseEntity<QuantityMeasurementDTO> response = restTemplate.exchange(
        baseUrl() + "/add?targetUnit=FEET",
        HttpMethod.POST,
        jsonEntity(body),
        QuantityMeasurementDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getResultValue()).isEqualTo(2.0);
  }

  @Test
  void testRestEndpointInvalidInput_Returns400() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    ResponseEntity<String> response = restTemplate.exchange(
        baseUrl() + "/compare",
        HttpMethod.POST,
        new HttpEntity<>("{ invalid-json }", headers),
        String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void testRestEndpointMissingParameter_Returns400() {
    ResponseEntity<String> response = restTemplate.getForEntity(
        baseUrl() + "/history/operation/INVALID_OPERATION",
        String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void testSwaggerUILoads() {
    ResponseEntity<String> response = restTemplate.getForEntity("/swagger-ui.html", String.class);

    assertThat(response.getStatusCode().is2xxSuccessful() || response.getStatusCode().is3xxRedirection()).isTrue();
    assertThat(response.getBody()).isNotNull();
  }

  @Test
  void testOpenAPIDocumentation() {
    ResponseEntity<String> response = restTemplate.getForEntity("/v3/api-docs", String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("/api/v1/quantities/compare");
    assertThat(response.getBody()).contains("/api/v1/quantities/add");
    assertThat(response.getBody()).contains("/api/v1/quantities/divide");
  }

  @Test
  void testActuatorHealthEndpoint() {
    ResponseEntity<String> response = restTemplate.getForEntity("/actuator/health", String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("UP");
  }

  @Test
  void testActuatorMetricsEndpoint() {
    ResponseEntity<String> response = restTemplate.getForEntity("/actuator/metrics", String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("names");
  }

  @Test
  void testJPARepositoryFindByOperation() {
    QuantityMeasurementEntity success = new QuantityMeasurementEntity();
    success.setThisValue(1.0);
    success.setThisUnit("FEET");
    success.setThisMeasurementType("LengthUnit");
    success.setThatValue(12.0);
    success.setThatUnit("INCHES");
    success.setThatMeasurementType("LengthUnit");
    success.setOperation("compare");
    success.setResultString("true");
    success.setError(false);
    repository.save(success);

    QuantityMeasurementEntity error = new QuantityMeasurementEntity();
    error.setThisValue(1.0);
    error.setThisUnit("FEET");
    error.setThisMeasurementType("LengthUnit");
    error.setOperation("add");
    error.setError(true);
    error.setErrorMessage("sample");
    repository.save(error);

    List<QuantityMeasurementEntity> compareRows = repository.findByOperation("compare");
    assertThat(compareRows).isNotEmpty();
    assertThat(compareRows).allMatch(row -> "compare".equals(row.getOperation()));
  }

  @Test
  void testJPARepositoryCustomQuery() {
    QuantityMeasurementEntity success = new QuantityMeasurementEntity();
    success.setThisValue(2.0);
    success.setThisUnit("FEET");
    success.setThisMeasurementType("LengthUnit");
    success.setOperation("compare");
    success.setError(false);
    success.setResultString("false");
    repository.save(success);

    QuantityMeasurementEntity errored = new QuantityMeasurementEntity();
    errored.setThisValue(1.0);
    errored.setThisUnit("FEET");
    errored.setThisMeasurementType("LengthUnit");
    errored.setOperation("compare");
    errored.setError(true);
    errored.setErrorMessage("sample error");
    repository.save(errored);

    List<QuantityMeasurementEntity> successfulCompare = repository.findSuccessfulByOperation("compare");
    List<QuantityMeasurementEntity> erroredRows = repository.findByErrorTrue();

    assertThat(successfulCompare).allMatch(row -> !row.isError());
    assertThat(erroredRows).anyMatch(QuantityMeasurementEntity::isError);
  }

  @Test
  void testContentNegotiation_JSON() {
    QuantityInputDTO body = input(1.0, "FEET", "LengthUnit", 12.0, "INCHES", "LengthUnit");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));

    ResponseEntity<String> response = restTemplate.exchange(
        baseUrl() + "/compare",
        HttpMethod.POST,
        new HttpEntity<>(body, headers),
        String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getHeaders().getContentType()).isNotNull();
    assertThat(response.getHeaders().getContentType().toString()).contains("application/json");
  }

  @Test
  void testExceptionHandling_GlobalHandler() {
    QuantityInputDTO body = input(1.0, "FEET", "LengthUnit", 0.0, "INCHES", "LengthUnit");

    ResponseEntity<String> response = restTemplate.exchange(
        baseUrl() + "/divide",
        HttpMethod.POST,
        jsonEntity(body),
        String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(response.getBody()).contains("Internal Server Error");
    assertThat(response.getBody()).contains("Divide by zero");
  }

  @Test
  void testRequestPathVariable_Extraction() {
    ResponseEntity<Long> response = restTemplate.getForEntity(baseUrl() + "/count/COMPARE", Long.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
  }

  @Test
  void testRequestQueryParameter_Extraction() {
    QuantityInputDTO body = input(1.0, "FEET", "LengthUnit", 12.0, "INCHES", "LengthUnit");

    ResponseEntity<QuantityMeasurementDTO> response = restTemplate.exchange(
        baseUrl() + "/compare?targetUnit=FEET",
        HttpMethod.POST,
        jsonEntity(body),
        QuantityMeasurementDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
  }

  @Test
  void testResponseSerialization_Object() {
    QuantityInputDTO body = input(1.0, "FEET", "LengthUnit", 12.0, "INCHES", "LengthUnit");

    ResponseEntity<String> response = restTemplate.exchange(
        baseUrl() + "/compare",
        HttpMethod.POST,
        jsonEntity(body),
        String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("resultString");
  }

  @Test
  void testIntegrationTest_MultipleOperations() {
    ResponseEntity<QuantityMeasurementDTO> compare = restTemplate.exchange(
        baseUrl() + "/compare",
        HttpMethod.POST,
        jsonEntity(input(1.0, "FEET", "LengthUnit", 12.0, "INCHES", "LengthUnit")),
        QuantityMeasurementDTO.class);

    ResponseEntity<QuantityMeasurementDTO> add = restTemplate.exchange(
        baseUrl() + "/add",
        HttpMethod.POST,
        jsonEntity(input(1.0, "FEET", "LengthUnit", 12.0, "INCHES", "LengthUnit")),
        QuantityMeasurementDTO.class);

    ResponseEntity<QuantityMeasurementDTO[]> history = restTemplate.getForEntity(
        baseUrl() + "/history/operation/ADD",
        QuantityMeasurementDTO[].class);

    assertThat(compare.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(add.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(history.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(history.getBody()).isNotNull();
  }

  @Test
  void testDatabaseInitialization_SchemaCreated() {
    Integer tableCount = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'quantity_measurement_entity'",
        Integer.class);

    Integer columnCount = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'quantity_measurement_entity'",
        Integer.class);

    Integer indexCount = jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM information_schema.statistics WHERE table_schema = DATABASE() AND table_name = 'quantity_measurement_entity'",
        Integer.class);

    assertThat(tableCount).isNotNull();
    assertThat(tableCount).isGreaterThan(0);
    assertThat(columnCount).isNotNull();
    assertThat(columnCount).isGreaterThan(5);
    assertThat(indexCount).isNotNull();
    assertThat(indexCount).isGreaterThan(0);
  }

  @Test
  void testHttpStatusCodes_Success() {
    ResponseEntity<QuantityMeasurementDTO> response = restTemplate.exchange(
        baseUrl() + "/compare",
        HttpMethod.POST,
        jsonEntity(input(1.0, "FEET", "LengthUnit", 12.0, "INCHES", "LengthUnit")),
        QuantityMeasurementDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void testHttpStatusCodes_ClientErrors() {
    ResponseEntity<String> badRequest = restTemplate.exchange(
        baseUrl() + "/compare",
        HttpMethod.POST,
        jsonEntity(input(1.0, "FOOT", "LengthUnit", 12.0, "INCHES", "LengthUnit")),
        String.class);

    ResponseEntity<String> notFound = restTemplate.getForEntity(
        "/api/v1/quantities/non-existing",
        String.class);

    assertThat(badRequest.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(notFound.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testHttpStatusCodes_ServerErrors() {
    ResponseEntity<String> serverError = restTemplate.exchange(
        baseUrl() + "/divide",
        HttpMethod.POST,
        jsonEntity(input(1.0, "FEET", "LengthUnit", 0.0, "INCHES", "LengthUnit")),
        String.class);

    assertThat(serverError.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  void testRestDocumentation_OperationDetails() {
    ResponseEntity<String> docs = restTemplate.getForEntity("/v3/api-docs", String.class);

    assertThat(docs.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(docs.getBody()).contains("Compare two quantities");
    assertThat(docs.getBody()).contains("operation");
    assertThat(docs.getBody()).contains("QuantityMeasurementDTO");
  }

  @Test
  @Disabled("Enable when XML HttpMessageConverters are configured")
  void testContentNegotiation_XML() {
  }

  @Test
  @Disabled("Enable when H2 console is configured for active profile")
  void testH2ConsoleLaunches() {
  }

  @Test
  @Disabled("Enable when active profile is switched to H2")
  void testH2DatabasePersistence() {
  }

  @Test
  @Disabled("Enable with dedicated transactional service method and rollback scenario")
  void testTransactionalRollback() {
  }

  @Test
  @Disabled("Enable when application-dev profile properties are available")
  void testProfileSpecificConfiguration_Development() {
  }

  @Test
  @Disabled("Enable with explicit prod-profile test container setup")
  void testProfileSpecificConfiguration_Production() {
  }

  @Test
  @Disabled("Current security config permits all endpoints")
  void testRESTEndpointSecurity_Unauthorized() {
  }

  @Test
  @Disabled("Current security config does not require tokens for /api/**")
  void testRESTEndpointSecurity_WithAuthentication() {
  }

  @Test
  @Disabled("Message conversion to DTO is already covered by endpoint tests")
  void testMessageConverter_JSONToObject() {
  }

  @Test
  @Disabled("Object to JSON conversion is already covered by endpoint tests")
  void testMessageConverter_ObjectToJSON() {
  }
}
