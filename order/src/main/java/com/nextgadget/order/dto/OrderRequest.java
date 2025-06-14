package com.nextgadget.order.dto;

import java.util.List;

public class OrderRequest {
    private List<OrderItemDTO> items;

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

