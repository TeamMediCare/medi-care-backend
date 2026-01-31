package com.careplan.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "복약 상담 답변 요청")
@Getter
@NoArgsConstructor
public class ConsultationAnswerRequest {

    @Schema(description = "답변 내용", example = "타이레놀은 하루 최대 4g까지 복용 가능합니다. 보통 1회 500mg~1g을 4~6시간 간격으로 복용하시면 됩니다.")
    @NotBlank(message = "답변 내용은 필수입니다.")
    private String answer;
}
