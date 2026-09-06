package ru.ekaterina.order.dto;

public class GetOrderCommand {
    public String correlationId;
    public Long orderId;

    public GetOrderCommand() {}

    public GetOrderCommand(String correlationId, Long orderId) {
        this.correlationId = correlationId;
        this.orderId = orderId;
    }
}

