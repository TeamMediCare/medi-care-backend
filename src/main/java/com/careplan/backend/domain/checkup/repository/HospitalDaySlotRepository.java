package com.careplan.backend.domain.checkup.repository;

import com.careplan.backend.domain.hospital.entity.HospitalDaySlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HospitalDaySlotRepository extends JpaRepository<HospitalDaySlot, Long> {
    List<HospitalDaySlot> findAllByHospitalIdIn(List<Long> hospitalIds);
}

