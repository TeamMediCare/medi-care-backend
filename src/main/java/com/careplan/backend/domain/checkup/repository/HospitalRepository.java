package com.careplan.backend.domain.checkup.repository;

import com.careplan.backend.domain.hospital.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    List<Hospital> findAllByCompanyContractedTrue();
}

