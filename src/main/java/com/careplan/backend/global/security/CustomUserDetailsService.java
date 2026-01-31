package com.careplan.backend.global.security;

import com.careplan.backend.user.entity.User;
import com.careplan.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String employeeNumber) throws UsernameNotFoundException {
        User user = userRepository.findByEmployeeNumber(employeeNumber)
                .orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
        return new CustomUserDetails(user);
    }
}
