package com.app.quantityservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.quantityservice.exception.QuantityMeasurementException;
import com.app.quantityservice.model.QuantityDTO;
import com.app.quantityservice.model.QuantityMeasurementDTO;
import com.app.quantityservice.model.QuantityMeasurementEntity;
import com.app.quantityservice.repository.QuantityMeasurementRepository;
import com.app.quantityservice.security.SecurityUtil;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QuantityMeasurementServiceImplTest {

  @Mock
  private QuantityMeasurementRepository repository;

  @Mock
  private SecurityUtil securityUtil;

  private QuantityMeasurementServiceImpl service;

  @BeforeEach
  void setUp() {
    service = new QuantityMeasurementServiceImpl(repository, securityUtil);
  }

  @Test
  void addPersistsResultForAuthenticatedUser() {
    QuantityDTO first = quantity(3.0, "FEET", "LengthUnit");
    QuantityDTO second = quantity(6.0, "INCHES", "LengthUnit");

    when(securityUtil.getCurrentUsername()).thenReturn("jane");
    when(repository.save(any(QuantityMeasurementEntity.class))).thenAnswer(invocation -> {
      QuantityMeasurementEntity entity = invocation.getArgument(0);
      entity.setId(101L);
      entity.setCreatedAt(LocalDateTime.now());
      return entity;
    });

    QuantityMeasurementDTO response = service.add(first, second);

    assertEquals("add", response.getOperation());
    assertEquals("FEET", response.getResultUnit());
    assertTrue(response.getResultValue() > 3.0);
    verify(repository).save(any(QuantityMeasurementEntity.class));
  }

  @Test
  void divideByZeroThrowsBusinessExceptionAndStoresError() {
    QuantityDTO first = quantity(10.0, "FEET", "LengthUnit");
    QuantityDTO second = quantity(0.0, "FEET", "LengthUnit");

    when(securityUtil.getCurrentUsername()).thenReturn("jane");

    QuantityMeasurementException ex = assertThrows(
        QuantityMeasurementException.class,
        () -> service.divide(first, second)
    );

    assertTrue(ex.getMessage().contains("divide Error"));
    assertTrue(ex.getMessage().toLowerCase().contains("zero"));
    verify(repository).save(any(QuantityMeasurementEntity.class));
  }

  @Test
  void getOperationHistoryReturnsEmptyListWhenUserIsGuest() {
    when(securityUtil.getCurrentUsername()).thenReturn(null);

    List<QuantityMeasurementDTO> result = service.getOperationHistory("add");

    assertTrue(result.isEmpty());
  }

  private QuantityDTO quantity(double value, String unit, String measurementType) {
    QuantityDTO dto = new QuantityDTO();
    dto.setValue(value);
    dto.setUnit(unit);
    dto.setMeasurementType(measurementType);
    return dto;
  }
}


