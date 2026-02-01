package com.aditya.inventory.dto;

public class ProductStockPurchaseDto {

  private Integer quantity;

  public ProductStockPurchaseDto(String sku, Integer quantity) {
    this.quantity = quantity;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }
  
}
