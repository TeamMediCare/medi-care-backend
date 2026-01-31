package com.careplan.backend.domain.checkup.dto;

import lombok.Builder;

import java.util.List;

public record CheckupRecommendationResponse(
        List<HospitalRecommendationDto> hospitals
) {
    @Builder
    public record HospitalRecommendationDto(
            Long hospitalId,
            String hospitalName,
            String address,
            Double distanceKm,
            Integer copay,
            Integer bestSlotCount,
            Double rating,
            Integer reviewCount,
            Double totalScore,
            List<String> reasons
    ) {}
}

