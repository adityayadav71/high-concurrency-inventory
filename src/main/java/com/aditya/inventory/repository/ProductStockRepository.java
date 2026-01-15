package com.aditya.inventory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aditya.inventory.model.ProductStock;

@Repository
public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
  Optional<ProductStock> findBySku(String sku);
}
