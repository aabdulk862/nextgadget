package com.nextgadget.order.service;

import com.nextgadget.order.dto.OrderItemDTO;
import com.nextgadget.order.dto.OrderRequest;
import com.nextgadget.order.entity.Order;
import com.nextgadget.order.entity.OrderItem;
import com.nextgadget.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {
    private final OrderRepository orderRepo;

    public OrderService(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    public Order placeOrder(OrderRequest request, Long userId) {
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("PLACED");

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemDTO itemDTO : request.getItems()) {
            OrderItem item = new OrderItem();
            item.setProductId(itemDTO.getProductId());
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(itemDTO.getUnitPrice()); // TODO: fetch from product-service
            item.setOrder(order);
            total = total.add(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            order.getItems().add(item);
        }

        order.setTotal(total);
        return orderRepo.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }
}

