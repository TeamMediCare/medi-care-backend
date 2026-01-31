package com.careplan.backend.auth.dto;

public record LoginResponse(
        Long userId,
        String role,
        String name,
        String department
) {}
