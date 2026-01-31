package com.careplan.backend.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;

    @Column(name = "employee_number", nullable = false, unique = true, length = 20)
    private String employeeNumber;

    @Column(nullable = false, length = 255)
    private String password; // BCrypt 해시 저장

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String department;

    @Column(length = 50)
    private String position;

    private LocalDate joinDate;
    private LocalDate birthDate;

    @Column(length = 10)
    private String gender;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "preferred_days", length = 100)
    private String preferredDays;

    @Column(name = "coverage_amount")
    private Integer coverageAmount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void updatePreferredDays(String preferredDays) {
        this.preferredDays = preferredDays;
    }

    public void updateCoverageAmount(Integer coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public enum Role {
        HR_MANAGER, EMPLOYEE, PHARMACIST, ADMIN
    }
}
