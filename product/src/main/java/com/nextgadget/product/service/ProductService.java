package com.nextgadget.product.service;

import com.nextgadget.product.entity.Product;
import com.nextgadget.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository repo) {
        this.productRepository = repo;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public List<Product> search(String category) {
        return productRepository.findByCategoryContainingIgnoreCase(category);
    }
}
