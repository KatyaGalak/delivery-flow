package ru.ekaterina.payment.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.ekaterina.payment.dto.ProcessPaymentCommand;
import ru.ekaterina.payment.kafka.event.PaymentCompletedEvent;
import ru.ekaterina.payment.kafka.event.PaymentFailedEvent;
import ru.ekaterina.payment.kafka.producer.PaymentEventProducer;
import ru.ekaterina.payment.service.PaymentService;
import ru.ekaterina.payment.dto.ProcessRefundCommand;
import ru.ekaterina.payment.kafka.event.RefundCompletedEvent;

@Service
public class PaymentCommandConsumer {
    private static final Logger logger = LoggerFactory.getLogger(PaymentCommandConsumer.class);

    private final PaymentService paymentService;
    private final PaymentEventProducer eventProducer;
    private final ObjectMapper objectMapper;

    public PaymentCommandConsumer(PaymentService paymentService,
                                  PaymentEventProducer eventProducer,
                                  ObjectMapper objectMapper) {
        this.paymentService = paymentService;
        this.eventProducer = eventProducer;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "process-payment-requests", groupId = "payment-service-group")
    public void consumePaymentCommand(String message) {
        try {
            ProcessPaymentCommand command = objectMapper.readValue(message, ProcessPaymentCommand.class);
            logger.info("Received process-payment command for orderId: {}", command.getOrderId());

            boolean success = paymentService.processPayment(command);

            if (success) {
                PaymentCompletedEvent event = PaymentCompletedEvent.builder()
                        .correlationId(command.getCorrelationId())
                        .orderId(command.getOrderId())
                        .userId(command.getUserId())
                        .amount(command.getAmount())
                        .status("SUCCESS")
                        .build();
                eventProducer.sendPaymentSuccess(event);
            } else {
                PaymentFailedEvent event = PaymentFailedEvent.builder()
                        .correlationId(command.getCorrelationId())
                        .orderId(command.getOrderId())
                        .userId(command.getUserId())
                        .reason("Insufficient funds")
                        .status("FAILED")
                        .build();
                eventProducer.sendPaymentFailure(event);
            }

        } catch (Exception e) {
            logger.error("Error processing payment message", e);
        }
    }

    @KafkaListener(topics = "process-refund-requests", groupId = "payment-service-group")
    public void consumeRefundCommand(String message) {
        try {
            ProcessRefundCommand command =
                    objectMapper.readValue(message, ProcessRefundCommand.class);
            logger.info("Received process-refund command for orderId: {}", command.getOrderId());

            paymentService.processRefund(command);

            RefundCompletedEvent event =
                    ru.ekaterina.payment.kafka.event.RefundCompletedEvent.builder()
                            .correlationId(command.getCorrelationId())
                            .orderId(command.getOrderId())
                            .userId(command.getUserId())
                            .amount(command.getAmount())
                            .reason(command.getReason())
                            .status("REFUNDED")
                            .build();
            eventProducer.sendRefundCompleted(event);
        } catch (Exception e) {
            logger.error("Error processing refund message", e);
        }
    }
}