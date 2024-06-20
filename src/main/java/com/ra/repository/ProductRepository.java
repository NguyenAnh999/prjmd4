package com.ra.repository;

import com.ra.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findAllByCategoryCategoryId(Long id);
    List<Product> findTop10ByOrderByCreatedAtDesc();
    List<Product> findProductByProductNameContainingOrDescriptionContaining(String productName, String description);
}
