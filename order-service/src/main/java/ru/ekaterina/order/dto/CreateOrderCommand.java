package ru.ekaterina.order.dto;

import java.util.List;

public class CreateOrderCommand {
    public String correlationId;
    public Long userId;
    public Long restaurantId;
    public String deliveryAddress;
    public List<OrderItemDto> items;

    public CreateOrderCommand() {}
}