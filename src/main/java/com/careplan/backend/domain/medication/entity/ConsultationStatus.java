package com.careplan.backend.domain.medication.entity;

public enum ConsultationStatus {
    WAITING("답변 대기"),
    COMPLETED("답변 완료");

    private final String description;

    ConsultationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
