package ru.ekaterina.payment.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundCompletedEvent {
    private String correlationId;
    private Long orderId;
    private Long userId;
    private Double amount;
    private String reason;
    private String status;
}
