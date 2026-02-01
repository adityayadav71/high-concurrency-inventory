package com.aditya.inventory.dto;

public class ProductStockQuantityModifyDto {

  private Integer delta;

  public ProductStockQuantityModifyDto(String sku, Integer delta) {
    this.delta = delta;
  }

  public Integer getDelta() {
    return delta;
  }

  public void setDelta(Integer delta) {
    this.delta = delta;
  }
  
}
