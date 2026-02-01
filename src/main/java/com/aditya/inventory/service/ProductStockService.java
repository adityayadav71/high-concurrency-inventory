package com.aditya.inventory.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.aditya.inventory.enums.ErrorCode;
import com.aditya.inventory.exception.EntityNotFoundException;
import com.aditya.inventory.exception.EntityValidationException;
import com.aditya.inventory.exception.NotNullException;
import com.aditya.inventory.model.ProductStock;
import com.aditya.inventory.repository.ProductStockRepository;

import jakarta.persistence.EntityManager;

@Service
public class ProductStockService {

  EntityManager entityManager;

  ProductStockRepository productStockRepository;

  public ProductStockService(ProductStockRepository productStockRepository, EntityManager entityManager) {
    this.productStockRepository = productStockRepository;
    this.entityManager = entityManager;
  }

  /**
   * This method returns a sequenced list of all ProductStock records stored in
   * the ProductStockRepository.
   * Each element in the list collection can be uniquely identified by either its
   * "sku" or its "id".
   * 
   * @return a {@link java.util.List} of all
   *         {@link com.aditya.inventory.model.ProductStock} in no particular
   *         order.
   *         Returns an empty list when no ProductStocks found in the repository
   */
  public List<ProductStock> getAllProductStocks() {
    List<ProductStock> productStocks = this.productStockRepository.findAll();

    return productStocks;
  }

  /**
   * This method finds the ProductStock record by the given sku.
   * 
   * @return {@link com.aditya.inventory.model.ProductStock}.
   * @throws EntityNotFoundException when no ProductStock with the given sku could be found in the repository.
   */
  public ProductStock getProductStockBySku(String sku) {
    Optional<ProductStock> productStockOptional = this.productStockRepository.findBySku(sku);

    if(!productStockOptional.isPresent()) throw new EntityNotFoundException("Could not find product stock with the sku: " + sku + " in the inventory. Please check GET /productStocks to retrieve available product stocks.", ErrorCode.NOT_FOUND);

    ProductStock productStock = productStockOptional.get();

    return productStock;
  }

  /**
   * This method returns the current available quantity of the ProductStock record found by the given sku 
   * in the inventory.
   * 
   * @return Quantity as an Integer value.
   * @throws EntityNotFoundException when no ProductStock with the given sku could be found in the repository.
   */
  public Integer getProductStockQuantityBySku(String sku) {
    Optional<ProductStock> productStockOptional = this.productStockRepository.findBySku(sku);

    if(!productStockOptional.isPresent()) throw new EntityNotFoundException("Could not find product stock with the sku: " + sku + " in the inventory. Please check GET /productStocks to retrieve available product stocks.", ErrorCode.NOT_FOUND);

    ProductStock productStock = productStockOptional.get();

    Integer productStockQuantity = productStock.getQuantity();

    return productStockQuantity;
  }

  /**
   * This method saves the provided ProductStock entity to the repository.
   * 
   * @return the created {@link com.aditya.inventory.model.ProductStock} record,
   *         returns null when the repository failed to save..
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
    if (stockPrice == null) {
      throw new NotNullException("Stock price is required but no value found.");
    }

    if (stockQuantity == null) {
      throw new NotNullException("Stock quantity is required but no value found.");
    }

    // 2. NEGATIVE value handling
    if (stockPrice.compareTo(zero) < 0) {
      throw new EntityValidationException("Stock price cannot be less than zero.", ErrorCode.INVALID_PRICE);
    }

    if (stockQuantity < 0) {
      throw new EntityValidationException("Stock quantity cannot be less than zero.", ErrorCode.INVALID_QUANTITY);
    }

    // 3. ZERO value handling
    if (stockPrice.equals(zero)) {
      throw new EntityValidationException("Stock price needs to be greater than zero", ErrorCode.MISSING_PRICE);
    }

  }

  /**
   * This method adjusts the current quantity value of the given ProductStock
   * (identified by its SKU) by a given "delta" amount.
   * 
   * @param sku   the sku value of the ProductStock in the inventory
   * @param delta the value of the quantity to increase/decrease the ProductStock by, can be a negative or a positive integer value
   * @return the updated {@link com.aditya.inventory.model.ProductStock} record
   */
  public ProductStock increaseProductStockQuantity(String sku, Integer delta) {
    if(delta == null) {
      throw new NotNullException("Delta is missing or null");
    }

    Optional<ProductStock> productStockOptional = this.productStockRepository.findBySku(sku);

    // Product Stock with given SKU not found
    if(!productStockOptional.isPresent()) {
      throw new EntityNotFoundException(
        "Couldn't find product stock with sku: " + sku + ". Please recheck SKU value using GET /productStocks to retreive available product stocks.", 
        ErrorCode.NOT_FOUND
      );
    }
    
    ProductStock productStock = productStockOptional.get();
    
    // Calculate the new stock quantity after addition of the given delta to the original quantity
    Integer originalStockQuantity = productStock.getQuantity();
    Integer newStockQuantity = originalStockQuantity + delta;
    
    if (newStockQuantity < 0) {
      throw new EntityValidationException(
        "Invalid delta provided. Resulting quantity: [" + newStockQuantity + "] is negative after addition of delta ["+ delta +"] to the original quantity: [" + originalStockQuantity + "]",
        ErrorCode.INVALID_DATA
      );
    }

    // Update product stock quantity if it passed the negative validation check      
    productStock.setQuantity(newStockQuantity);

    return this.productStockRepository.save(productStock);
  }

}
