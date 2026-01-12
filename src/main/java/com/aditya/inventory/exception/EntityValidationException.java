package com.aditya.inventory.exception;

import com.aditya.inventory.enums.ErrorCode;

public class EntityValidationException extends RuntimeException {
  final ErrorCode errorCode;

  public EntityValidationException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public ErrorCode getErrorCode() {
    return this.errorCode;
  }
}
