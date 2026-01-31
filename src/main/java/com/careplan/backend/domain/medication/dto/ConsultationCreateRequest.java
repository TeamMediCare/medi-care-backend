package com.careplan.backend.domain.medication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "복약 상담 등록 요청")
@Getter
@NoArgsConstructor
public class ConsultationCreateRequest {

    @Schema(description = "질문자 이름", example = "홍길동")
    @NotBlank(message = "질문자 이름은 필수입니다.")
    private String questionerName;

    @Schema(description = "질문 제목", example = "타이레놀 복용 관련 질문")
    @NotBlank(message = "질문 제목은 필수입니다.")
    private String title;

    @Schema(description = "질문 내용", example = "타이레놀을 하루에 몇 번까지 복용해도 되나요?")
    @NotBlank(message = "질문 내용은 필수입니다.")
    private String content;
}
