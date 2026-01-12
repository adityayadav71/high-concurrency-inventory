package com.aditya.inventory.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aditya.inventory.enums.ErrorCode;
import com.aditya.inventory.exception.EntityValidationException;
import com.aditya.inventory.exception.NotNullException;
import com.aditya.inventory.model.ProductStock;
import com.aditya.inventory.repository.ProductStockRepository;

@Service
public class ProductStockService {
    
  ProductStockRepository productStockRepository;

  public ProductStockService(ProductStockRepository productStockRepository) {
    this.productStockRepository = productStockRepository;
  }

  /**
   * This method returns a sequenced list of all ProductStock records stored in the ProductStockRepository.
   * Each element in the list collection can be uniquely identified by either its "sku" or its "id".
   * 
   * @return a {@link java.util.List} of all {@link com.aditya.inventory.model.ProductStock} in no particular order.
   * Returns an empty list when no ProductStocks found in the repository
   */ 
  public List<ProductStock> getAllProductStocks() {
    List<ProductStock> productStocks = this.productStockRepository.findAll();
    
    return productStocks;
  }
    
  /**
   * This method saves the provided ProductStock entity to the repository.
   * 
   * @return the created {@link com.aditya.inventory.model.ProductStock} record,
   * returns null when the repository failed to save..
   */ 
  public ProductStock createProductStock(ProductStock productStock) {
    this.validateProductStock(productStock);

    return this.productStockRepository.save(productStock);
  }

  public void validateProductStock(ProductStock productStock) throws NotNullException, EntityValidationException {     
    BigDecimal zero = BigDecimal.valueOf(0.00);
    BigDecimal stockPrice = productStock.getPrice();
    Integer stockQuantity = productStock.getQuantity();

    // 1. NULL value handling
    if(stockPrice == null) {
      throw new NotNullException("Stock price is required but no value found.");
    }

    if(stockQuantity == null) {
      throw new NotNullException("Stock quantity is required but no value found.");
    }

    // 2. NEGATIVE value handling
    if(stockPrice.compareTo(zero) < 0) {
      throw new EntityValidationException("Stock price cannot be less than zero.", ErrorCode.INVALID_PRICE);
    }
    
    if(stockQuantity < 0) {
      throw new EntityValidationException("Stock quantity cannot be less than zero.", ErrorCode.INVALID_QUANTITY);
    }

    // 3. ZERO value handling
    if(stockPrice.equals(zero)) {
      throw new EntityValidationException("Stock price needs to be greater than zero", ErrorCode.MISSING_PRICE);
    }

  }
}
