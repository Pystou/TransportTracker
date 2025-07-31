package com.transporttracker.tracker.dto;

public record UserDTO(
        Long id,
        String email,
        String role
) {}
