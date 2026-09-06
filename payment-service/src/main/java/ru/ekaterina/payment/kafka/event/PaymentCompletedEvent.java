package ru.ekaterina.payment.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCompletedEvent {
    private String correlationId;
    private Long orderId;
    private Long userId;
    private Double amount;
    private String status;
}