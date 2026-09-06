package ru.ekaterina.kitchendelivery.kafka.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProcessEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(ProcessEventConsumer.class);

    private final RuntimeService runtimeService;
    private final ObjectMapper objectMapper;

    public ProcessEventConsumer(RuntimeService runtimeService, ObjectMapper objectMapper) {
        this.runtimeService = runtimeService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "order-created", groupId = "kitchen-delivery-group")
    public void consumeOrderCreated(String message) {
        try {
            JsonNode node = objectMapper.readTree(message);
            Long orderId = node.get("orderId").asLong();
            Long userId = node.get("userId").asLong();
            Double totalAmount = node.get("totalAmount").asDouble();

            Map<String, Object> variables = new HashMap<>();
            variables.put("orderId", orderId);
            variables.put("userId", userId);
            variables.put("totalAmount", totalAmount);

            runtimeService.startProcessInstanceByKey("DeliveryProcess", orderId.toString(), variables);
            logger.info("Started Camunda process DeliveryProcess for orderId: {}", orderId);
        } catch (Exception e) {
            logger.error("Error starting process on order-created event", e);
        }
    }

    @KafkaListener(topics = "payment-completed", groupId = "kitchen-delivery-group")
    public void consumePaymentCompleted(String message) {
        try {
            JsonNode node = objectMapper.readTree(message);
            Long orderId = node.get("orderId").asLong();

            runtimeService.createMessageCorrelation("PaymentReceivedMessage")
                    .processInstanceBusinessKey(orderId.toString())
                    .correlate();
            logger.info("Correlated PaymentReceivedMessage for orderId: {}", orderId);
        } catch (Exception e) {
            logger.error("Error correlating payment completed message", e);
        }
    }

    @KafkaListener(topics = "payment-failed", groupId = "kitchen-delivery-group")
    public void consumePaymentFailed(String message) {
        try {
            JsonNode node = objectMapper.readTree(message);
            Long orderId = node.get("orderId").asLong();
            String reason = node.has("reason") ? node.get("reason").asText() : "Payment failed";

            runtimeService.createMessageCorrelation("PaymentFailedMessage")
                    .processInstanceBusinessKey(orderId.toString())
                    .setVariable("failReason", reason)
                    .correlate();
            logger.info("Correlated PaymentFailedMessage for orderId: {}", orderId);
        } catch (Exception e) {
            logger.error("Error correlating payment failed message", e);
        }
    }
}