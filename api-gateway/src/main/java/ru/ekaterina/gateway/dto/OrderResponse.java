package ru.ekaterina.gateway.dto;

import java.util.List;

public record OrderResponse(
    Long orderId,
    Long userId,
    Long restaurantId,
    String status,
    Double totalAmount,
    String deliveryAddress,
    List<OrderItem> items
) {}
