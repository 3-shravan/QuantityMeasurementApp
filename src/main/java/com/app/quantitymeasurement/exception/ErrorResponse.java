package com.app.quantitymeasurement.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

  private LocalDateTime timestamp;
  private int status;
  private String error;
  private String message;
  private String path;

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private final ErrorResponse response = new ErrorResponse();

    public Builder timestamp(LocalDateTime timestamp) {
      response.setTimestamp(timestamp);
      return this;
    }

    public Builder status(int status) {
      response.setStatus(status);
      return this;
    }

    public Builder error(String error) {
      response.setError(error);
      return this;
    }

    public Builder message(String message) {
      response.setMessage(message);
      return this;
    }

    public Builder path(String path) {
      response.setPath(path);
      return this;
    }

    public ErrorResponse build() {
      return response;
    }
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
