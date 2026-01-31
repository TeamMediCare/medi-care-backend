package com.careplan.backend.domain.auth.dto;

public record LoginResponse(
        Long userId,
        String role,
        String name,
        String department
) {}
