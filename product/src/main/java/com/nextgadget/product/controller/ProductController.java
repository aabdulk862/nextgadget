package com.nextgadget.product.controller;

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

    @GetMapping
    public List<Product> getAll() {
        return productService.getAll();
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return productService.save(product);
    }

    @GetMapping("/search")
    public List<Product> search(@RequestParam String category) {
        return productService.search(category);
    }
}
