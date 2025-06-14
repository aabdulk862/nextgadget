package com.nextgadget.product.controller;

import com.nextgadget.product.dto.ProductUpdateDTO;
import com.nextgadget.product.entity.Product;
import com.nextgadget.product.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService service) {
        this.productService = service;
    }

    // ✅ Get all products (public)
    @GetMapping
    public List<Product> getAll() {
        return productService.getAll();
    }

    // ✅ Get product by ID (public)
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getById(id);
    }

    // ✅ Get only available products (public)
    @GetMapping("/available")
    public List<Product> getAvailableProducts() {
        return productService.getAvailableProducts();
    }

    // ✅ Search products by category (public)
    @GetMapping("/by-category")
    public List<Product> searchByCategory(@RequestParam String category) {
        return productService.search(category);
    }

    // ✅ Search products by name (public)
    @GetMapping("/by-name")
    public List<Product> searchByName(@RequestParam String name) {
        return productService.searchByName(name);
    }

    // ✅ Create a new product (admin)
    @PostMapping
    public Product create(@RequestBody Product product) {
        return productService.save(product);
    }

    // ✅ Update a product (admin)
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody ProductUpdateDTO product) {
        return productService.updateProduct(id, product);
    }

    // ✅ Delete a product (admin)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }


}
