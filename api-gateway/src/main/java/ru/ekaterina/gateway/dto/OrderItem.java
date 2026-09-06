package ru.ekaterina.gateway.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderItem(
        @NotNull(message = "menuItemId is required")
        Long menuItemId,

        @NotBlank(message = "name is required")
        String name,

        @NotNull(message = "quantity is required")
        @Min(value = 1, message = "quantity: min 1")
        Integer quantity,

        @NotNull(message = "price is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "price must be >= 0")
        Double price
) {}
