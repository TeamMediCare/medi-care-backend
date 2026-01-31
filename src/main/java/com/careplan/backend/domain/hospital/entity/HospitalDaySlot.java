package com.careplan.backend.domain.hospital.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "hospital_day_slot",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_hospital_day_slot_hospital_day",
                        columnNames = {"hospital_id", "day_of_week"}
                )
        },
        indexes = {
                @Index(name = "idx_hospital_day_slot_hospital_id", columnList = "hospital_id")
        }
)
public class HospitalDaySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_day_slot_id")
    private Long hospitalDaySlotId;

    @Column(name = "hospital_id", nullable = false)
    private Long hospitalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 10)
    private DayOfWeek dayOfWeek;

    // 해당 요일 예약 가능 슬롯 수
    @Column(name = "slot_count", nullable = false)
    private Integer slotCount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.slotCount == null) this.slotCount = 0;
    }

    public void updateSlotCount(Integer slotCount) {
        this.slotCount = slotCount;
    }
}
