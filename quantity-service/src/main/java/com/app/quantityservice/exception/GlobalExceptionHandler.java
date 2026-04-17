package com.app.quantityservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

  private ErrorResponse buildError(HttpStatus status, String message, HttpServletRequest request) {
    return ErrorResponse.builder()
        .timestamp(Instant.now())
        .status(status.value())
        .error(status.getReasonPhrase())
        .message(message)
        .path(request.getRequestURI())
        .build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex,
      HttpServletRequest request) {

    String message = ex.getBindingResult()
        .getAllErrors()
        .stream()
        .map(error -> error instanceof FieldError fieldError ? fieldError.getDefaultMessage() : error.getDefaultMessage())
        .collect(Collectors.joining(", "));

    if (message == null || message.isBlank()) {
      message = "Invalid request payload";
    }

    return buildResponse(HttpStatus.BAD_REQUEST, message, request);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex,
      HttpServletRequest request) {

    return buildResponse(HttpStatus.BAD_REQUEST, "Malformed JSON request", request);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex,
      HttpServletRequest request) {

    return buildResponse(HttpStatus.BAD_REQUEST, "Invalid value for parameter: " + ex.getName(), request);
  }

  @ExceptionHandler(QuantityMeasurementException.class)
  public ResponseEntity<ErrorResponse> handleQuantityException(
      QuantityMeasurementException ex,
      HttpServletRequest request) {

    return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse> handleAuthenticationException(
      AuthenticationException ex,
      HttpServletRequest request) {

    return buildResponse(HttpStatus.UNAUTHORIZED, "Authentication failed", request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error", request);
  }

  private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, HttpServletRequest request) {
    return ResponseEntity.status(status).body(buildError(status, message, request));
  }
}

