package ru.ekaterina.order.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.ekaterina.order.kafka.message.OrderResponse;

@Service
public class OrderResponseProducer {
    private static final Logger logger = LoggerFactory.getLogger(OrderResponseProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OrderResponseProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendCreateOrderResponse(String correlationId, OrderResponse response) {
        try {
            String message = objectMapper.writeValueAsString(response);
            kafkaTemplate.send("create-order-responses", correlationId, message);
            logger.info("Sent create-order-response for correlationId: {}", correlationId);
        } catch (Exception e) {
            logger.error("Error sending create-order-response", e);
        }
    }

    public void sendGetOrderResponse(String correlationId, OrderResponse response) {
        try {
            String message = objectMapper.writeValueAsString(response);
            kafkaTemplate.send("get-order-responses", correlationId, message);
            logger.info("Sent get-order-response for correlationId: {}", correlationId);
        } catch (Exception e) {
            logger.error("Error sending get-order-response", e);
        }
    }
}