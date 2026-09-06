package ru.ekaterina.order.kafka.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ekaterina.order.dto.OrderItemDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String correlationId;
    private Long orderId;
    private Long userId;
    private Long restaurantId;
    private String status;
    private Double totalAmount;
    private String deliveryAddress;
    private List<OrderItemDto> items;
    private String error;
}