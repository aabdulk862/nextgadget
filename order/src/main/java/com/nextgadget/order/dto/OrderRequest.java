package com.nextgadget.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class OrderRequest {
    @NotEmpty(message = "Order must contain at least one item")
    private List<@Valid OrderItemDTO> items;

    public OrderRequest() {
    }

    public OrderRequest(List<OrderItemDTO> items) {
        this.items = items;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }
}

