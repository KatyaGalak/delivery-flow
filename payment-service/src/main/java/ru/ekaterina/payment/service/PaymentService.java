package ru.ekaterina.payment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.ekaterina.payment.dto.ProcessPaymentCommand;
import ru.ekaterina.payment.dto.ProcessRefundCommand;

import java.util.Random;

@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final Random random = new Random();

    public boolean processPayment(ProcessPaymentCommand command) {
        logger.info("Processing payment for orderId: {}, amount: {}", command.getOrderId(), command.getAmount());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean isSuccess = random.nextInt(100) < 95;

        if (isSuccess) {
            logger.info("Payment SUCCESSFUL for orderId: {}", command.getOrderId());
        } else {
            logger.warn("Payment FAILED for orderId: {} (Insufficient funds simulation)", command.getOrderId());
        }

        return isSuccess;
    }

    public boolean processRefund(ProcessRefundCommand command) {
        logger.info("Processing REFUND for orderId: {}, amount: {}, reason: {}",
                command.getOrderId(), command.getAmount(), command.getReason());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        logger.info("REFUND SUCCESSFUL for orderId: {}", command.getOrderId());
        return true;
    }
}