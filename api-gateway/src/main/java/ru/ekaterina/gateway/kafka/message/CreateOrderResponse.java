package ru.ekaterina.gateway.kafka.message;

import ru.ekaterina.gateway.dto.OrderItem;
import java.util.List;

public class CreateOrderResponse {
    public String correlationId;
    public Long orderId;
    public Long userId;
    public Long restaurantId;
    public String status;
    public Double totalAmount;
    public String deliveryAddress;
    public List<OrderItem> items;
    public String error;

    public CreateOrderResponse() {}
}
