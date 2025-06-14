package com.nextgadget.order.controller;

import com.nextgadget.order.dto.OrderRequest;
import com.nextgadget.order.entity.Order;
import com.nextgadget.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest request, @RequestHeader("user-id") Long userId) {
        return ResponseEntity.ok(orderService.placeOrder(request, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
}
