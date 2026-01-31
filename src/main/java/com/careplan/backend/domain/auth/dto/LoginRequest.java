package com.careplan.backend.domain.auth.dto;

public record LoginRequest(
        String employeeNumber,
        String password
) {}
