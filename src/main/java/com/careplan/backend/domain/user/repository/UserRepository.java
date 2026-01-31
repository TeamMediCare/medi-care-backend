package com.careplan.backend.domain.user.repository;

import com.careplan.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmployeeNumber(String employeeNumber);
}
