package ru.ekaterina.gateway.dto;

import java.util.Set;

public record UserDTO(
    Long id,
    String username,
    String name,
    String email,
    Set<String> roles
) {}
