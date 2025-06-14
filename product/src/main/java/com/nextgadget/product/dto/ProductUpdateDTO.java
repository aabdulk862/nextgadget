package com.nextgadget.product.dto;

public class ProductUpdateDTO {
    private String name;
    private String category;
    private Double price;
    private Integer stock;
    private String imageUrl;

    public ProductUpdateDTO() {
    }

    public ProductUpdateDTO(String name, String category, Double price, Integer stock, String imageUrl) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
