package ru.ekaterina.gateway.kafka.message;

public class GetOrderMessage {
    public String correlationId;
    public Long orderId;

    public GetOrderMessage() {}

    public GetOrderMessage(String correlationId, Long orderId) {
        this.correlationId = correlationId;
        this.orderId = orderId;
    }
}