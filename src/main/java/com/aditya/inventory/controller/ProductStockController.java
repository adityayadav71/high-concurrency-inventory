package com.aditya.inventory.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aditya.inventory.dto.ProductStockQuantityDto;
import com.aditya.inventory.dto.ProductStockQuantityModifyDto;
import com.aditya.inventory.model.ProductStock;
import com.aditya.inventory.service.ProductStockService;
import com.aditya.inventory.util.StandardResponse;

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

    return new StandardResponse<List<ProductStock>>(true,
        "All product stocks in the inventory have been retrieved successfully.", productStocks);
  }

  @GetMapping("/{sku}")
  public StandardResponse<ProductStock> getProductStock(@PathVariable String sku) {
    ProductStock productStock = this.productStockService.getProductStockBySku(sku);

    return new StandardResponse<ProductStock>(true, "Stock with sku: " + sku + " retrieved successfully.", productStock);
  }

  @GetMapping("/{sku}/quantity")
  public StandardResponse<ProductStockQuantityDto> getProductStockQuantity(@PathVariable String sku) {
    Integer productStockQuantity = this.productStockService.getProductStockQuantityBySku(sku);

    ProductStockQuantityDto stockQuantityResponse = new ProductStockQuantityDto(sku, productStockQuantity);

    return new StandardResponse<ProductStockQuantityDto>(true,
        "Stock quantity for sku: " + sku + " retrieved successfully", stockQuantityResponse);
  }

  @PostMapping
  public StandardResponse<ProductStock> createProductStock(@RequestBody ProductStock productStock) {
    ProductStock createdStock = this.productStockService.createProductStock(productStock);

    return new StandardResponse<ProductStock>(true,
        "Product stock created with sku: " + createdStock.getSku() + " successfully.", createdStock);
  }

  @PatchMapping("/{sku}/quantity")
  public StandardResponse<ProductStock> increaseProductStockQuantity(@PathVariable String sku,
      @RequestBody ProductStockQuantityModifyDto requestBody) {
    ProductStock updatedStock = this.productStockService.increaseProductStockQuantity(sku, requestBody.getDelta());

    return new StandardResponse<ProductStock>(true, "Stock quantity updated for sku: " + sku + " successfully.", updatedStock);
  }

}
