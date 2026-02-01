package com.aditya.inventory.util;

import java.time.LocalDateTime;

public class StandardResponse<T> {

  private boolean success;
  private String message;
  private LocalDateTime timestamp;
  private T data;

  public StandardResponse(boolean success, String message, T data) {
    this.success = success;
    this.message = message;
    this.timestamp = LocalDateTime.now();
    this.data = data;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

}
