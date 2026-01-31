package com.careplan.backend.medication.dto;

import com.careplan.backend.medication.entity.MedicationConsultation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "복약 상담 목록 응답")
@Getter
public class ConsultationListResponse {

    @Schema(description = "상담 ID", example = "1")
    private final Long id;

    @Schema(description = "질문자 이름", example = "홍길동")
    private final String questionerName;

    @Schema(description = "질문 제목", example = "타이레놀 복용 관련 질문")
    private final String title;

    @Schema(description = "상태", example = "답변 대기")
    private final String status;

    @Schema(description = "등록일시", example = "2025-01-15T10:30:00")
    private final LocalDateTime createdAt;

    public ConsultationListResponse(MedicationConsultation consultation) {
        this.id = consultation.getId();
        this.questionerName = consultation.getQuestionerName();
        this.title = consultation.getTitle();
        this.status = consultation.getStatus().getDescription();
        this.createdAt = consultation.getCreatedAt();
    }
}
