package ru.ekaterina.gateway.kafka.message;

import ru.ekaterina.gateway.dto.OrderItem;
import java.util.List;

public class CreateOrderMessage {
    public String correlationId;
    public Long userId;
    public Long restaurantId;
    public String deliveryAddress;
    public List<OrderItem> items;

    public CreateOrderMessage(String correlationId, Long userId, Long restaurantId,
                              String deliveryAddress, List<OrderItem> items) {
        this.correlationId = correlationId;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.deliveryAddress = deliveryAddress;
        this.items = items;
    }

    public CreateOrderMessage() {}
}
