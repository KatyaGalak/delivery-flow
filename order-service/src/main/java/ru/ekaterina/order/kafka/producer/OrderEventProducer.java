package ru.ekaterina.order.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.ekaterina.order.kafka.event.OrderCreatedEvent;
import ru.ekaterina.order.model.Order;

@Service
public class OrderEventProducer {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OrderEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendOrderCreatedEvent(Order order) {
        try {
            OrderCreatedEvent event = OrderCreatedEvent.builder()
                    .orderId(order.getId())
                    .userId(order.getUserId())
                    .totalAmount(order.getTotalAmount())
                    .status(order.getStatus().name())
                    .build();

            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("order-created", order.getId().toString(), message);
            logger.info("Published order-created event for orderId: {}", order.getId());
        } catch (Exception e) {
            logger.error("Error sending order-created event", e);
        }
    }
}