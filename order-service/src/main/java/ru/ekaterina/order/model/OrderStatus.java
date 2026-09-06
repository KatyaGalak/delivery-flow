package ru.ekaterina.order.model;

public enum OrderStatus {
    CREATED,
    PAID,
    PREPARING,
    READY_FOR_PICKUP,
    IN_DELIVERY,
    DELIVERED,
    CANCELLED
}
