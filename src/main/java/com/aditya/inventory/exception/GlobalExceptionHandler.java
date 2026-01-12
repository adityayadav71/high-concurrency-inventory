package com.aditya.inventory.exception;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aditya.inventory.enums.ErrorCode;

@RestControllerAdvice
public class GlobalExceptionHandler {
  
  @ExceptionHandler(EntityValidationException.class)
  public ResponseEntity<Map<String, Object>> handleValidationException(EntityValidationException e) {
    Map<String, Object> response = new HashMap<>();
    
    ZoneId systemZone = ZoneId.systemDefault();
    response.put("timestamp", LocalDateTime.now(systemZone));
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("code", e.getErrorCode());
    response.put("error", "Validation Error");
    response.put("message", e.getMessage());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Map<String, Object>> handleAlreadyExistsException(DataIntegrityViolationException e) {
    Map<String, Object> response = new HashMap<>();
    
    ZoneId systemZone = ZoneId.systemDefault();

    if(e.getMessage() != null && e.getMessage().contains("constraint")) {
      response.put("timestamp", LocalDateTime.now(systemZone));
      response.put("status", HttpStatus.CONFLICT.value());
      response.put("code", ErrorCode.ALREADY_EXISTS);
      response.put("error", "Duplicate Entry Error");
      response.put("message", e.getMessage());
      
      return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    response.put("timestamp", LocalDateTime.now(systemZone));
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("code", ErrorCode.INVALID_DATA);
    response.put("error", "Data Integrity Violation Error");
    response.put("message", e.getMessage());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotNullException.class)
  public ResponseEntity<Map<String, Object>> handleNotNullException(NotNullException e) {
    Map<String, Object> response = new HashMap<>();
    
    ZoneId systemZone = ZoneId.systemDefault();
    response.put("timestamp", LocalDateTime.now(systemZone));
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("code", ErrorCode.LACKS_REQUIRED_INPUT);
    response.put("error", "Missing Input Error");
    response.put("message", e.getMessage());

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, Object>> handleGenericException(RuntimeException e) {
    Map<String, Object> response = new HashMap<>();
    
    ZoneId systemZone = ZoneId.systemDefault();
    response.put("timestamp", LocalDateTime.now(systemZone));
    response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.put("error", "Internal Server Error");
    response.put("message", e.getMessage());

    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
