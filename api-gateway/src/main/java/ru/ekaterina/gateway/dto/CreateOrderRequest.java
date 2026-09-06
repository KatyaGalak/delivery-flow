package ru.ekaterina.gateway.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CreateOrderRequest(
        @NotNull(message = "restaurantId is required")
        @Positive(message = "restaurantID must be > 0")
        Long restaurantId,

        @NotBlank(message = "deliveryAddress is required")
        String deliveryAddress,

        @NotEmpty(message = "items: list cannot be empty")
        List<@Valid OrderItem> items
) {}
