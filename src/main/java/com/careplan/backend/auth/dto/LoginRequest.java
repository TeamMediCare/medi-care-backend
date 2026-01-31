package com.careplan.backend.auth.dto;

public record LoginRequest(
        String employeeNumber,
        String password
) {}
