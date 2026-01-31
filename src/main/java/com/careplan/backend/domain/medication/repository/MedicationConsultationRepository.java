package com.careplan.backend.domain.medication.repository;

import com.careplan.backend.domain.medication.entity.ConsultationStatus;
import com.careplan.backend.domain.medication.entity.MedicationConsultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationConsultationRepository extends JpaRepository<MedicationConsultation, Long> {

    List<MedicationConsultation> findAllByOrderByCreatedAtDesc();

    List<MedicationConsultation> findByStatusOrderByCreatedAtDesc(ConsultationStatus status);
}
