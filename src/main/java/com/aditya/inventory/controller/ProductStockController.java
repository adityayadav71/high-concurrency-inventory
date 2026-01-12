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
  public List<ProductStock> getProductStocks() {
    List<ProductStock> productStocks = this.productStockService.getAllProductStocks();

    return productStocks;
  } 
  
  @PostMapping
  public ProductStock createProductStock(@RequestBody ProductStock productStock) {
    ProductStock createdStock = this.productStockService.createProductStock(productStock);

    return createdStock;
  } 
  
}
