package com.careplan.backend.domain.auth.controller;

import com.careplan.backend.domain.auth.dto.LoginRequest;
import com.careplan.backend.domain.auth.dto.LoginResponse;
import com.careplan.backend.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}