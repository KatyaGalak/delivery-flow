package ru.ekaterina.kitchendelivery.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProcessCommandProducer {
    private static final Logger logger = LoggerFactory.getLogger(ProcessCommandProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ProcessCommandProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendProcessPaymentCommand(String correlationId, Long orderId, Long userId, Double amount) {
        try {
            Map<String, Object> command = new HashMap<>();
            command.put("correlationId", correlationId);
            command.put("orderId", orderId);
            command.put("userId", userId);
            command.put("amount", amount);

            kafkaTemplate.send("process-payment-requests", orderId.toString(), objectMapper.writeValueAsString(command));
            logger.info("Sent process-payment command for orderId: {}", orderId);
        } catch (Exception e) {
            logger.error("Error sending payment command", e);
        }
    }

    public void sendRefundCommand(String correlationId, Long orderId, Long userId, Double amount, String reason) {
        try {
            Map<String, Object> command = new HashMap<>();
            command.put("correlationId", correlationId);
            command.put("orderId", orderId);
            command.put("userId", userId);
            command.put("amount", amount);
            command.put("reason", reason != null ? reason : "Order cancelled by timeout");

            kafkaTemplate.send("process-refund-requests", orderId.toString(), objectMapper.writeValueAsString(command));
            logger.info("Sent process-refund command for orderId: {}, amount: {}", orderId, amount);
        } catch (Exception e) {
            logger.error("Error sending refund command", e);
        }
    }

    public void sendOrderStatusChanged(Long orderId, String status, String reason) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("orderId", orderId);
            event.put("status", status);
            event.put("reason", reason);

            kafkaTemplate.send("order-status-changed", orderId.toString(), objectMapper.writeValueAsString(event));
            logger.info("Sent order-status-changed: orderId={}, status={}", orderId, status);
        } catch (Exception e) {
            logger.error("Error sending order-status-changed event for orderId={}", orderId, e);
        }
    }
}