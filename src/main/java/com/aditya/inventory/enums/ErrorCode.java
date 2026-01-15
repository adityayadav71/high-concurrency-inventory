package com.aditya.inventory.enums;

public enum ErrorCode {
  MISSING_PRICE("MISSING_PRICE", "Stock Price is required."),
  INVALID_PRICE("INVALID_PRICE", "The provided value for the stock price is invalid."),
  INVALID_QUANTITY("INVALID_QUANTITY", "The provided value for the stock quantity is invalid."),
  INVALID_DATA("INVALID_DATA", "The provided value is invalid."),
  ALREADY_EXISTS("ALREADY_EXISTS", "The provided entity already exists in the repository."),
  LACKS_REQUIRED_INPUT("LACKS_REQUIRED_INPUT", "The provided request payload lacks one or many of the required fields for completing this request."),
  NOT_FOUND("NOT_FOUND", "The requested data could not be found in the repository.");
  
  private String label;
  private String description;

  private ErrorCode(String label, String description) {
    this.label = label;
    this.description = description;
  }

  public String getLabel() {
    return this.label;
  }

  public String getDescription() {
    return this.description;
  }
}
