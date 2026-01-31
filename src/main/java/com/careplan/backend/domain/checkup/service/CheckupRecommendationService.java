package com.careplan.backend.domain.checkup.service;

import com.careplan.backend.domain.checkup.dto.CheckupRecommendationResponse;
import com.careplan.backend.domain.checkup.repository.HospitalDaySlotRepository;
import com.careplan.backend.domain.checkup.repository.HospitalRepository;
import com.careplan.backend.domain.hospital.Hospital;
import com.careplan.backend.domain.hospital.entity.HospitalDaySlot;
import com.careplan.backend.domain.user.entity.User;
import com.careplan.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheckupRecommendationService {

    private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;
    private final HospitalDaySlotRepository hospitalDaySlotRepository;

    // 가중치
    private static final double W_DIST = 0.25;
    private static final double W_TIME = 0.25;
    private static final double W_COST = 0.35;
    private static final double W_RATE = 0.15;

    public CheckupRecommendationResponse recommendTop6(String employeeNumber, List<DayOfWeek> preferredDays) {
        // 1) 사용자 조회
        // TODO: 나중에 인증 붙이면 employeeNumber 없이 principal에서 꺼내기
        User user = userRepository.findByEmployeeNumber(employeeNumber)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. employeeNumber=" + employeeNumber));

        // 2) 선호 요일 결정 (요청값 우선, 없으면 user 기본값, 그것도 없으면 전체요일)
        List<DayOfWeek> days = resolvePreferredDays(user, preferredDays);

        // 3) 후보 병원: 회사 제휴 병원만
        List<Hospital> candidates = hospitalRepository.findAllByCompanyContractedTrue();

        if (candidates.isEmpty()) {
            return new CheckupRecommendationResponse(List.of());
        }

        // 4) 병원별 요일 슬롯 한 번에 조회해서 Map으로 만들기 (N+1 방지)
        List<Long> hospitalIds = candidates.stream().map(Hospital::getHospitalId).toList();
        List<HospitalDaySlot> slots = hospitalDaySlotRepository.findAllByHospitalIdIn(hospitalIds);

        Map<Long, Map<DayOfWeek, Integer>> slotMap = buildSlotMap(slots);

        // 5) 점수 계산 + 추천 이유 생성
        List<ScoredHospital> scored = new ArrayList<>();

        for (Hospital h : candidates) {
            // (선택) 선호 요일 중 하나라도 슬롯이 있는 병원만 남기고 싶으면 여기서 필터
            int bestSlot = bestSlotCount(slotMap.get(h.getHospitalId()), days);
            // if (bestSlot == 0) continue; // 필요하면 활성화

            double distKm = distanceKm(user.getLatitude(), user.getLongitude(), h.getLatitude(), h.getLongitude());

            // 비용: (패키지 가격 - 지원금)
            int packagePrice = resolvePackagePrice(h);
            int coverage = resolveCoverageAmount(user);              // TODO: 복지금/지원금 정책 반영
            int copay = Math.max(packagePrice - coverage, 0);

            // rating, reviewCount
            double rating = h.getRating() == null ? 0.0 : h.getRating();
            int reviewCount = h.getReviewCount() == null ? 0 : h.getReviewCount();

            // 점수 4개
            double sDist = scoreDistance(distKm);
            double sTime = scoreTime(bestSlot);
            double sCost = scoreCost(copay);
            double sRate = scoreRate(rating, reviewCount);

            double total = W_DIST * sDist + W_TIME * sTime + W_COST * sCost + W_RATE * sRate;

            List<String> reasons = buildReasons(distKm, copay, bestSlot, rating, reviewCount, days);

            scored.add(new ScoredHospital(h, distKm, copay, bestSlot, rating, reviewCount, total, reasons));
        }

        // 6) 정렬 후 Top6
        List<CheckupRecommendationResponse.HospitalRecommendationDto> top6 = scored.stream()
                .sorted(Comparator.comparingDouble(ScoredHospital::totalScore).reversed())
                .limit(6)
                .map(ScoredHospital::toDto)
                .toList();

        return new CheckupRecommendationResponse(top6);
    }

    // 선호 요일
    private List<DayOfWeek> resolvePreferredDays(User user, List<DayOfWeek> preferredDays) {
        if (preferredDays != null && !preferredDays.isEmpty()) return preferredDays;

        // TODO: user에 preferredDays 저장해뒀으면 그걸 파싱해서 반환
        // 예: "MON,WED,SAT" 같은 문자열 컬럼
        if (user.getPreferredDays() != null && !user.getPreferredDays().isBlank()) {
            return Arrays.stream(user.getPreferredDays().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .map(DayOfWeek::valueOf)
                    .toList();
        }

        return List.of(DayOfWeek.values());
    }

    // 슬롯 Map 구성
    private Map<Long, Map<DayOfWeek, Integer>> buildSlotMap(List<HospitalDaySlot> slots) {
        Map<Long, Map<DayOfWeek, Integer>> map = new HashMap<>();
        for (HospitalDaySlot s : slots) {
            map.computeIfAbsent(s.getHospitalId(), k -> new EnumMap<>(DayOfWeek.class))
                    .put(s.getDayOfWeek(), s.getSlotCount());
        }
        return map;
    }

    private int bestSlotCount(Map<DayOfWeek, Integer> daySlots, List<DayOfWeek> preferredDays) {
        if (daySlots == null) return 0;
        int max = 0;
        for (DayOfWeek d : preferredDays) {
            max = Math.max(max, daySlots.getOrDefault(d, 0));
        }
        return max;
    }

    // 가격/지원금: 일단 임시
    private int resolvePackagePrice(Hospital h) {
        // TODO: 진짜 패키지 계산으로 교체
        // 1) hospital.basic_package_price 컬럼을 만들었다면 그걸 쓰고
        // 2) 또는 hospital_checkup_item + checkup_item.is_basic 합산
        return h.getBasicPackagePrice() != null ? h.getBasicPackagePrice() : 200_000; // 임시값
    }

    private int resolveCoverageAmount(User user) {
        // TODO: users.coverage_amount 또는 welfare_budget 정책 반영
        return user.getCoverageAmount() != null ? user.getCoverageAmount() : 150_000; // 임시값
    }

    // 점수 계산
    private double scoreDistance(double dKm) {
        return 1.0 / (1.0 + Math.pow(dKm / 3.0, 2));
    }

    private double scoreTime(int maxSlot) {
        return Math.min(maxSlot / 10.0, 1.0);
    }

    private double scoreCost(int copay) {
        return 1.0 / (1.0 + (copay / 20000.0));
    }

    private double scoreRate(double rating, int reviewCount) {
        double ratingScore = rating / 5.0;
        double trust = 1.0 - Math.exp(-reviewCount / 50.0);
        return ratingScore * trust;
    }

    // 거리 계산 (Haversine)
    private double distanceKm(Double lat1, Double lon1, Double lat2, Double lon2) {
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
            return 9999.0; // 위치 정보 없으면 사실상 추천 제외되는 효과
        }
        double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    // 추천 이유
    private List<String> buildReasons(double distKm, int copay, int bestSlot, double rating, int reviewCount, List<DayOfWeek> days) {
        List<String> reasons = new ArrayList<>();

        if (copay == 0) reasons.add("회사 지원 적용 시 본인부담금 0원");
        else reasons.add("예상 본인부담금 " + copay + "원");

        if (distKm < 9990) reasons.add(String.format("현재 위치에서 %.1fkm 거리", distKm));

        if (bestSlot > 0) {
            String dayText = days.stream().map(Enum::name).collect(Collectors.joining(","));
            reasons.add("선호 요일(" + dayText + ") 기준 예약 가능 슬롯 최대 " + bestSlot + "개");
        } else {
            reasons.add("선호 요일 기준 예약 가능 슬롯 정보가 부족합니다");
        }

        if (reviewCount > 0) reasons.add(String.format("평점 %.1f (리뷰 %d건)", rating, reviewCount));
        else reasons.add(String.format("평점 %.1f", rating));

        return reasons;
    }

    // 내부 DTO
    private record ScoredHospital(
            Hospital hospital,
            double distanceKm,
            int copay,
            int bestSlotCount,
            double rating,
            int reviewCount,
            double totalScore,
            List<String> reasons
    ) {
        public double totalScore() { return totalScore; }

        public CheckupRecommendationResponse.HospitalRecommendationDto toDto() {
            return CheckupRecommendationResponse.HospitalRecommendationDto.builder()
                    .hospitalId(hospital.getHospitalId())
                    .hospitalName(hospital.getHospitalName())
                    .address(hospital.getAddress())
                    .distanceKm(distanceKm >= 9990 ? null : distanceKm)
                    .copay(copay)
                    .bestSlotCount(bestSlotCount)
                    .rating(rating)
                    .reviewCount(reviewCount)
                    .totalScore(totalScore)
                    .reasons(reasons)
                    .build();
        }
    }
}

