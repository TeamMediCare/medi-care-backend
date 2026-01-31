package com.careplan.backend.domain.medication.dto;

import com.careplan.backend.domain.medication.entity.MedicationConsultation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "복약 상담 상세 응답")
@Getter
public class ConsultationDetailResponse {

    @Schema(description = "상담 ID", example = "1")
    private final Long id;

    @Schema(description = "질문자 이름", example = "홍길동")
    private final String questionerName;

    @Schema(description = "질문 제목", example = "타이레놀 복용 관련 질문")
    private final String title;

    @Schema(description = "질문 내용", example = "타이레놀을 하루에 몇 번까지 복용해도 되나요?")
    private final String content;

    @Schema(description = "답변 내용", example = "타이레놀은 하루 최대 4g까지 복용 가능합니다.")
    private final String answer;

    @Schema(description = "상태", example = "답변 완료")
    private final String status;

    @Schema(description = "등록일시", example = "2025-01-15T10:30:00")
    private final LocalDateTime createdAt;

    @Schema(description = "답변일시", example = "2025-01-15T14:20:00")
    private final LocalDateTime answeredAt;

    public ConsultationDetailResponse(MedicationConsultation consultation) {
        this.id = consultation.getId();
        this.questionerName = consultation.getQuestionerName();
        this.title = consultation.getTitle();
        this.content = consultation.getContent();
        this.answer = consultation.getAnswer();
        this.status = consultation.getStatus().getDescription();
        this.createdAt = consultation.getCreatedAt();
        this.answeredAt = consultation.getAnsweredAt();
    }
}
