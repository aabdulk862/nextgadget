package com.nextgadget.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextgadget.order.dto.OrderItemDTO;
import com.nextgadget.order.dto.OrderPlacedEvent;
import com.nextgadget.order.dto.OrderRequest;
import com.nextgadget.order.entity.Order;
import com.nextgadget.order.entity.OrderItem;
import com.nextgadget.order.exception.ResourceNotFoundException;
import com.nextgadget.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private static final String ORDER_EVENTS_TOPIC = "order-events";

    private final OrderRepository orderRepo;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepo, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.orderRepo = orderRepo;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
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
        Order savedOrder = orderRepo.save(order);

        try {
            OrderPlacedEvent event = new OrderPlacedEvent(
                    savedOrder.getId(),
                    savedOrder.getUserId(),
                    savedOrder.getTotal(),
                    LocalDateTime.now()
            );
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(ORDER_EVENTS_TOPIC, String.valueOf(savedOrder.getId()), eventJson);
            log.info("Published order-placed event for order {}", savedOrder.getId());
        } catch (Exception e) {
            log.warn("Failed to publish order-placed event for order {}: {}", savedOrder.getId(), e.getMessage());
        }

        return savedOrder;
    }

    public Order getOrderById(Long id) {
        return orderRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }
}

