package com.careplan.backend.auth.service;

import com.careplan.backend.auth.dto.LoginRequest;
import com.careplan.backend.auth.dto.LoginResponse;
import com.careplan.backend.user.entity.User;
import com.careplan.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest request) {
        // 1) 스프링 시큐리티 인증(비번 검증)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.employeeNumber(), request.password())
        );

        // 2) 인증 성공이면 user 조회해서 응답
        User user = userRepository.findByEmployeeNumber(request.employeeNumber())
                .orElseThrow(() -> new IllegalStateException("USER_NOT_FOUND_BUT_AUTHENTICATED"));

        return new LoginResponse(
                user.getId(),
                user.getRole().name(),
                user.getName(),
                user.getDepartment()
        );
    }
}
