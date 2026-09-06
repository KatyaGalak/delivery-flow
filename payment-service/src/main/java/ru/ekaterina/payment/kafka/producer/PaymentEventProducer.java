package ru.ekaterina.payment.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.ekaterina.payment.kafka.event.PaymentCompletedEvent;
import ru.ekaterina.payment.kafka.event.PaymentFailedEvent;

@Service
public class PaymentEventProducer {
    private static final Logger logger = LoggerFactory.getLogger(PaymentEventProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public PaymentEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        
        this.kafkaTemplate = kafkaTemplate;
        
        this.objectMapper = objectMapper;
    }

    public void sendPaymentSuccess(PaymentCompletedEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("payment-completed", event.getOrderId().toString(), message);

            logger.info("Sent payment-completed event for orderId: {}", event.getOrderId());
        } catch (Exception e) {
            logger.error("Error sending payment success event", e);
        }
    }

    public void sendPaymentFailure(PaymentFailedEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("payment-failed", event.getOrderId().toString(), message);

            logger.info("Sent payment-failed event for orderId: {}", event.getOrderId());
        } catch (Exception e) {
            logger.error("Error sending payment failure event", e);
        }
    }

    public void sendRefundCompleted(ru.ekaterina.payment.kafka.event.RefundCompletedEvent event) {
        try {
            String message = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("refund-completed", event.getOrderId().toString(), message);

            logger.info("Sent refund-completed event for orderId: {}", event.getOrderId());
        } catch (Exception e) {
            logger.error("Error sending refund-completed event", e);
        }
    }
}