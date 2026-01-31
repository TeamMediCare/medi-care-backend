package com.careplan.backend.service;

import com.careplan.backend.dto.*;
import com.careplan.backend.entity.MedicationConsultation;
import com.careplan.backend.repository.MedicationConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MedicationConsultationService {

    private final MedicationConsultationRepository consultationRepository;

    @Transactional
    public Long createConsultation(ConsultationCreateRequest request) {
        MedicationConsultation consultation = MedicationConsultation.builder()
                .questionerName(request.getQuestionerName())
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        return consultationRepository.save(consultation).getId();
    }

    public List<ConsultationListResponse> getAllConsultations() {
        return consultationRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(ConsultationListResponse::new)
                .collect(Collectors.toList());
    }

    public ConsultationDetailResponse getConsultationDetail(Long id) {
        MedicationConsultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상담을 찾을 수 없습니다. id=" + id));

        return new ConsultationDetailResponse(consultation);
    }

    @Transactional
    public ConsultationDetailResponse addAnswer(Long id, ConsultationAnswerRequest request) {
        MedicationConsultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 상담을 찾을 수 없습니다. id=" + id));

        consultation.addAnswer(request.getAnswer());

        return new ConsultationDetailResponse(consultation);
    }
}
