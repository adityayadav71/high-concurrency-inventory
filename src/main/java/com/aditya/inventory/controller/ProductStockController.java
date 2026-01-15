package com.aditya.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aditya.inventory.model.ProductStock;
import com.aditya.inventory.service.ProductStockService;

@RestController
@RequestMapping("/productStocks")
public class ProductStockController {

  ProductStockService productStockService;

  @Autowired
  public ProductStockController(ProductStockService productStockService) {
    this.productStockService = productStockService;
  }

  @GetMapping
  public StandardResponse<List<ProductStock>> getProductStocks() {
    List<ProductStock> productStocks = this.productStockService.getAllProductStocks();

    LocalDateTime timestamp = LocalDateTime.now();
    
    return new StandardResponse<List<ProductStock>>(true, "All product stocks in the inventory have been retrieved successfully.", timestamp, productStocks);
  } 

  @GetMapping("/{sku}")
  public StandardResponse<ProductStock> getProductStock(@PathVariable String sku) {
    ProductStock productStock = this.productStockService.getProductStockBySku(sku);

    LocalDateTime timestamp = LocalDateTime.now();

    return new StandardResponse<ProductStock>(true, "Stock with sku: " + sku + " retrieved successfully.", timestamp, productStock);
  } 
 
  @GetMapping("/{sku}/quantity")
  public StandardResponse<ProductStockQuantityDto> getProductStockQuantity(@PathVariable String sku) {
    Integer productStockQuantity = this.productStockService.getProductStockQuantityBySku(sku);

    LocalDateTime timestamp = LocalDateTime.now();

    ProductStockQuantityDto stockQuantityResponse = new ProductStockQuantityDto(sku, productStockQuantity);

    return new StandardResponse<ProductStockQuantityDto>(true, "Stock quantity for sku: " + sku + " retrieved successfully", timestamp, stockQuantityResponse);
  } 
  
  @PostMapping
  public ProductStock createProductStock(@RequestBody ProductStock productStock) {
    ProductStock createdStock = this.productStockService.createProductStock(productStock);

    return createdStock;
  } 
  
}
