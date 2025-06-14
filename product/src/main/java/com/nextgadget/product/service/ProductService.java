package com.nextgadget.product.service;

import com.nextgadget.product.entity.Product;
import com.nextgadget.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        if (product.getImageUrl() == null || product.getImageUrl().isBlank()) {
            product.setImageUrl("https://i.imgur.com/RqnGoSd.png");
        }
        return productRepository.save(product);
    }

    public List<Product> search(String category) {
        return productRepository.findByCategoryContainingIgnoreCase(category);
    }

    // ✅ Update product details by ID
    public Product updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existing = productRepository.findById(id);
        if (existing.isPresent()) {
            Product product = existing.get();
            product.setName(updatedProduct.getName());
            product.setCategory(updatedProduct.getCategory());
            product.setPrice(updatedProduct.getPrice());
            product.setStock(updatedProduct.getStock());
            product.setImageUrl(
                    updatedProduct.getImageUrl() == null || updatedProduct.getImageUrl().isBlank()
                            ? product.getImageUrl()
                            : updatedProduct.getImageUrl()
            );
            return productRepository.save(product);
        } else {
            throw new RuntimeException("Product with ID " + id + " not found");
        }
    }

    // ✅ Delete discontinued product by ID
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product with ID " + id + " not found");
        }
        productRepository.deleteById(id);
    }
}
