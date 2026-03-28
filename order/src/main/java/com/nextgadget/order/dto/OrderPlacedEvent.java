package com.nextgadget.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderPlacedEvent {
    private Long orderId;
    private Long userId;
    private BigDecimal totalAmount;
    private LocalDateTime timestamp;

    public OrderPlacedEvent() {
    }

    public OrderPlacedEvent(Long orderId, Long userId, BigDecimal totalAmount, LocalDateTime timestamp) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.timestamp = timestamp;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
