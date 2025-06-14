package com.nextgadget.product.repository;

import com.nextgadget.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryContainingIgnoreCase(String category);

    List<Product> findByNameContainingIgnoreCase(String name);
}
