package ru.ekaterina.gateway.kafka.producer;

import ru.ekaterina.gateway.kafka.event.UserRegisteredEvent;
import ru.ekaterina.gateway.kafka.message.CreateOrderMessage;
import ru.ekaterina.gateway.kafka.message.GetOrderMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class Producer {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public static final String USER_REGISTERED_TOPIC = "user-registered";
    public static final String CREATE_ORDER_REQUEST_TOPIC = "create-order-requests";
    public static final String GET_ORDER_REQUEST_TOPIC = "get-order-requests";

    public Producer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendUserRegisteredEvent(UserRegisteredEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(USER_REGISTERED_TOPIC, event.userId.toString(), message);

            logger.info("Sent UserRegisteredEvent for user: {}", event.userId);
        } catch (Exception e) {
            logger.error("Error sending UserRegisteredEvent", e);
        }
    }

    public void sendCreateOrderRequest(CreateOrderMessage request) {
        try {
            String message = objectMapper.writeValueAsString(request);
            kafkaTemplate.send(CREATE_ORDER_REQUEST_TOPIC, request.correlationId, message);

            logger.info("Sent CreateOrderMessage with correlation id: {}", request.correlationId);
        } catch (Exception e) {
            logger.error("Error sending CreateOrderMessage", e);
        }
    }

    public void sendGetOrderRequest(String correlationId, Long orderId) {
        try {
            GetOrderMessage request = new GetOrderMessage(correlationId, orderId);
            String message = objectMapper.writeValueAsString(request);
            kafkaTemplate.send(GET_ORDER_REQUEST_TOPIC, correlationId, message);

            logger.info("Sent GetOrderMessage with correlation id: {}", correlationId);
        } catch (Exception e) {
            logger.error("Error sending GetOrderMessage", e);
        }
    }
}
