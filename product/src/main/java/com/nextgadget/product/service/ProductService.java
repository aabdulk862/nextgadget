package com.nextgadget.product.service;

import com.nextgadget.product.dto.ProductUpdateDTO;
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

    // ✅ Get all products
    public List<Product> getAll() {
        return productRepository.findAll();
    }
    // ✅ Save a new product
    public Product save(Product product) {
        if (product.getImageUrl() == null || product.getImageUrl().isBlank()) {
            product.setImageUrl("https://i.imgur.com/RqnGoSd.png");
        }
        return productRepository.save(product);
    }
    // ✅ Search products by category
    public List<Product> search(String category) {
        return productRepository.findByCategoryContainingIgnoreCase(category);
    }

    // ✅ Get a single product by ID
    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with ID " + id + " not found"));
    }

    // ✅ Get only products that are in stock
    public List<Product> getAvailableProducts() {
        return productRepository.findAll().stream()
                .filter(product -> product.getStock() > 0)
                .toList();
    }

    // ✅ Search products by name (case-insensitive, partial match)
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    // ✅ Filter by category and stock
    public List<Product> filterAvailableByCategory(String category) {
        return productRepository.findByCategoryContainingIgnoreCase(category).stream()
                .filter(product -> product.getStock() > 0)
                .toList();
    }

    // ✅ Update product details by ID
    public Product updateProduct(Long id, ProductUpdateDTO updateDto) {
        return productRepository.findById(id).map(product -> {
            if (updateDto.getName() != null && !updateDto.getName().isBlank()) {
                product.setName(updateDto.getName());
            }
            if (updateDto.getCategory() != null && !updateDto.getCategory().isBlank()) {
                product.setCategory(updateDto.getCategory());
            }
            if (updateDto.getPrice() != null) {
                product.setPrice(updateDto.getPrice());  // assuming Product.price is also Double
            }
            if (updateDto.getStock() != null) {
                product.setStock(updateDto.getStock());
            }
            if (updateDto.getImageUrl() != null && !updateDto.getImageUrl().isBlank()) {
                product.setImageUrl(updateDto.getImageUrl());
            }
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product with ID " + id + " not found"));
    }


    // ✅ Delete discontinued product by ID
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product with ID " + id + " not found");
        }
        productRepository.deleteById(id);
    }
}
