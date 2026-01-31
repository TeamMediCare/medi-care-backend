package com.careplan.backend.medication.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "medication_consultation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MedicationConsultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String questionerName;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultationStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime answeredAt;

    @Builder
    public MedicationConsultation(String questionerName, String title, String content) {
        this.questionerName = questionerName;
        this.title = title;
        this.content = content;
        this.status = ConsultationStatus.WAITING;
        this.createdAt = LocalDateTime.now();
    }

    public void addAnswer(String answer) {
        this.answer = answer;
        this.status = ConsultationStatus.COMPLETED;
        this.answeredAt = LocalDateTime.now();
    }
}
