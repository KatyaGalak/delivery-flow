package ru.ekaterina.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentCommand {
    private String correlationId;
    private Long orderId;
    private Long userId;
    private Double amount;
}