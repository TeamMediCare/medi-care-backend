package com.careplan.backend.domain.checkup.controller;

import com.careplan.backend.domain.checkup.dto.CheckupRecommendationResponse;
import com.careplan.backend.domain.checkup.service.CheckupRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checkup")
public class CheckupRecommendationController {

    private final CheckupRecommendationService recommendationService;

    // 직원용 병원 추천 Top 6
    @GetMapping("/recommendations")
    public CheckupRecommendationResponse recommend(
            @RequestParam(required = false) List<DayOfWeek> preferredDays,
            @RequestParam(required = false) String employeeNumber // 임시: 인증 붙이기 전까지
    ) {
        return recommendationService.recommendTop6(employeeNumber, preferredDays);
    }
}
