package com.aditya.inventory.dto;

public class ProductStockQuantityDto {
  private String sku;

  private Integer stockQuantity;

  public ProductStockQuantityDto(String sku, Integer stockQuantity) {
    this.sku = sku;
    this.stockQuantity = stockQuantity;
  }

  public String getSku() {
    return this.sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public Integer getStockQuantity() {
    return this.stockQuantity;
  }

  public void setStockQuantity(Integer stockQuantity) {
    this.stockQuantity = stockQuantity;
  }
}
