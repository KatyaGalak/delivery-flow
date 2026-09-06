package ru.ekaterina.gateway.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "username is required")
        @Size(min = 3, max = 50, message = "username: from 3 to 50 characters")
        String username,

        @NotBlank(message = "password is required")
        @Size(min = 6, max = 100, message = "password: minimum 6 characters, maximum 100 character")
        String password,

        @NotBlank(message = "name is required")
        @Size(min = 3, max = 100, message = "name: minimum 6 characters, maximum 100 character")
        String name,

        @NotBlank(message = "email is required")
        @Email(message = "email: invalid format")
        String email,

        @NotBlank(message = "role is required")
        @Pattern(
                regexp = "ROLE_CUSTOMER|ROLE_COURIER|ROLE_RESTAURANT|ROLE_ADMIN",
                message = "role: role: allowed values are ROLE_CUSTOMER, ROLE_COURIER, ROLE_RESTAURANT, ROLE_ADMIN"
        )
        String role
) {}
