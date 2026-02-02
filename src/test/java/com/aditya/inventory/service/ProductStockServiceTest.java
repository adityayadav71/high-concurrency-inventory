package com.aditya.inventory.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aditya.inventory.exception.EntityValidationException;
import com.aditya.inventory.model.ProductStock;
import com.aditya.inventory.repository.ProductStockRepository;

@ExtendWith(MockitoExtension.class)
class ProductStockServiceTest {

  @Mock
  private ProductStockRepository productStockRepository;

  @InjectMocks
  private ProductStockService productStockService;

  @Test
  void testPurchaseProduct_Success() {
    // --- ARRANGE ---
    String sku = UUID.randomUUID().toString();
    
    Random random = new Random();
    String productStockName = "New Product " + random.nextInt(Integer.MAX_VALUE); 

    // Create Mock Stock with randomly generated name with 10 quantity.
    ProductStock mockProduct = new ProductStock(sku, productStockName, "Description", BigDecimal.valueOf(1000), 9, "Phones");

    when(productStockRepository.findBySku(sku)).thenReturn(Optional.of(mockProduct));

    when(productStockRepository.save(any(ProductStock.class))).thenAnswer(i -> i.getArguments()[0]);

    // --- ACT ---
    // Buy 5 Items
    ProductStock result = productStockService.buyProductFromProductStock(sku, 5);

    // --- ASSERT ---
    assertNotNull(result);
    assertEquals(4, result.getQuantity());

    verify(productStockRepository, times(1)).save(any(ProductStock.class));
  }

    @Test
  void testPurchaseProduct_InsufficientStock() {
    // --- ARRANGE ---
    String sku = UUID.randomUUID().toString();

    Random random = new Random();
    String productStockName = "New Product " + random.nextInt(Integer.MAX_VALUE); 
     
    ProductStock mockProduct = new ProductStock(sku, productStockName, "Description", BigDecimal.valueOf(1000), 2, "Phones");

    // When service asks for SKU, return the product with 2 items
    when(productStockRepository.findBySku(sku)).thenReturn(Optional.of(mockProduct));

    // --- ACT & ASSERT ---
    // Expect an EntityValidationException when we try to buy 5
    EntityValidationException exception = assertThrows(EntityValidationException.class, () -> {
      productStockService.buyProductFromProductStock(sku, 5);
    });

    String expectedMessage = "Invalid quantity provided. Resulting quantity: [-3] is negative after subtraction of quantity [5] from the original quantity: [2]";
    assertEquals(expectedMessage, exception.getMessage());

    verify(productStockRepository, never()).save(any());
  }

  @Test
  void testIncreaseProductStockQuantity_Positive() {
    // --- ARRANGE ---
    String sku = "TEST-SKU-1";

    // Create Mock Stock with randomly generated name with 10 quantity.
    ProductStock mockProduct = new ProductStock(sku, "Test Product", "Desc", BigDecimal.valueOf(1000), 14, "Phones");

    when(productStockRepository.findBySku(sku)).thenReturn(Optional.of(mockProduct));

    when(productStockRepository.save(any(ProductStock.class))).thenAnswer(i -> i.getArguments()[0]);

    // --- ACT ---
    // Increase Product Stock By 5 items
    ProductStock incResult = productStockService.increaseProductStockQuantity(sku, 5);
    
    // --- ASSERT ---
    assertNotNull(incResult);
    assertEquals(19, incResult.getQuantity());

    verify(productStockRepository, times(1)).save(any(ProductStock.class));
  }

  @Test
  void testIncreaseProductStockQuantity_Negative_Success() {
    // --- ARRANGE ---
    String sku = "TEST-SKU-2";

    // Create Mock Stock with randomly generated name with 10 quantity.
    ProductStock mockProduct = new ProductStock(sku, "Test Product", "Desc", BigDecimal.valueOf(1000), 14, "Phones");

    when(productStockRepository.findBySku(sku)).thenReturn(Optional.of(mockProduct));

    when(productStockRepository.save(any(ProductStock.class))).thenAnswer(i -> i.getArguments()[0]);

    // --- ACT ---
    // Decrease Product Stock By 5 items
    ProductStock decResult = productStockService.increaseProductStockQuantity(sku, -5);

    // --- ASSERT ---
    assertNotNull(decResult);
    assertEquals(9, decResult.getQuantity());

    verify(productStockRepository, times(1)).save(any(ProductStock.class));
  }

  @Test
  void testIncreaseProductStockQuantity_Negative_Failure() {
    // --- ARRANGE ---
    String sku = "TEST-SKU-2";

    // Create Mock Stock with randomly generated name with 10 quantity.
    ProductStock mockProduct = new ProductStock(sku, "Test Product", "Desc", BigDecimal.valueOf(1000), 14, "Phones");

    when(productStockRepository.findBySku(sku)).thenReturn(Optional.of(mockProduct));

    // --- ACT & ASSERT ---
    // Expect an EntityValidationException when we try to buy 15 items when only 14 ProductStocks are available
    EntityValidationException exception = assertThrows(EntityValidationException.class, () -> {
      productStockService.increaseProductStockQuantity(sku, -15);
    });

    String expectedMessage = "Invalid delta provided. Resulting quantity: [-1] is negative after addition of delta [-15] to the original quantity: [14]";
    assertEquals(expectedMessage, exception.getMessage());

    verify(productStockRepository, never()).save(any(ProductStock.class));
  }

}