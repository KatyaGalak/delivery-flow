package ru.ekaterina.order.dto;

public record OrderItemDto(
        Long menuItemId,
        String name,
        Integer quantity,
        Double price
) {}