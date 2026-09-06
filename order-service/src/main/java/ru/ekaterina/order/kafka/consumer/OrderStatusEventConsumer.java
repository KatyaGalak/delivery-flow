package ru.ekaterina.order.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ekaterina.order.kafka.event.OrderStatusChangedEvent;
import ru.ekaterina.order.model.OrderStatus;
import ru.ekaterina.order.service.OrderService;

@Service
public class OrderStatusEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(OrderStatusEventConsumer.class);

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public OrderStatusEventConsumer(OrderService orderService, ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "order-status-changed", groupId = "order-service-group")
    public void onStatusChanged(String message) {
        try {
            OrderStatusChangedEvent event = objectMapper.readValue(message, OrderStatusChangedEvent.class);
            logger.info("Received order-status-changed: orderId={}, status={}", event.getOrderId(), event.getStatus());

            OrderStatus newStatus = OrderStatus.valueOf(event.getStatus());
            orderService.updateStatus(event.getOrderId(), newStatus);
        } catch (Exception e) {
            logger.error("Error processing order-status-changed: {}", message, e);
        }
    }
}
