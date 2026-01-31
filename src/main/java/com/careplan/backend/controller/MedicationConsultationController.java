package com.careplan.backend.controller;

import com.careplan.backend.dto.*;
import com.careplan.backend.service.MedicationConsultationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "복약 상담", description = "복약 상담 관련 API")
@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
public class MedicationConsultationController {

    private final MedicationConsultationService consultationService;

    @Operation(summary = "복약 상담 등록", description = "새로운 복약 상담 질문을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "상담 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping
    public ResponseEntity<Long> createConsultation(@Valid @RequestBody ConsultationCreateRequest request) {
        Long id = consultationService.createConsultation(request);
        return ResponseEntity.created(URI.create("/api/consultations/" + id)).body(id);
    }

    @Operation(summary = "복약 상담 목록 조회", description = "모든 복약 상담 목록을 조회합니다. 질문자 이름, 제목, 상태값을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<List<ConsultationListResponse>> getAllConsultations() {
        return ResponseEntity.ok(consultationService.getAllConsultations());
    }

    @Operation(summary = "복약 상담 상세 조회", description = "특정 복약 상담의 상세 내용을 조회합니다. 질문 내용과 답변을 확인할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "상담을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConsultationDetailResponse> getConsultationDetail(
            @Parameter(description = "상담 ID", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(consultationService.getConsultationDetail(id));
    }

    @Operation(summary = "복약 상담 답변 등록", description = "복약 상담에 답변을 등록합니다. 답변 등록 시 상태가 '답변 완료'로 변경됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "답변 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "상담을 찾을 수 없음")
    })
    @PostMapping("/{id}/answer")
    public ResponseEntity<ConsultationDetailResponse> addAnswer(
            @Parameter(description = "상담 ID", required = true) @PathVariable Long id,
            @Valid @RequestBody ConsultationAnswerRequest request) {
        return ResponseEntity.ok(consultationService.addAnswer(id, request));
    }
}
