package ru.ekaterina.gateway.dto;

public record AuthResponse(
    String token,
    UserDTO user
) {}
