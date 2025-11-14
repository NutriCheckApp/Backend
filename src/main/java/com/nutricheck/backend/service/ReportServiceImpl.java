package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.*;
import com.nutricheck.backend.dto.DailyReportResponse;
import com.nutricheck.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 영양 분석 리포트 서비스 구현체
 * Nutritional analysis report service implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final AnalysisRepository analysisRepository;
    private final IntakeRepository intakeRepository;
    private final UserRepository userRepository;
    private final EssentialRepository essentialRepository;

    /**
     * 일일 영양 분석 리포트 생성 및 조회
     * Generate and retrieve daily nutritional analysis report
     */
    @Override
    @Transactional
    public DailyReportResponse getDailyReport(Long userId, LocalDate date) {
        // 기존 분석 결과가 있는지 확인 / Check if analysis exists
        Optional<Analysis> existingAnalysis = analysisRepository.findByUser_UserIdAndAnalysisDate(userId, date);

        if (existingAnalysis.isPresent()) {
            // 기존 분석 결과 반환 / Return existing analysis
            return convertToResponse(existingAnalysis.get());
        } else {
            // 새로운 분석 생성 / Create new analysis
            return regenerateDailyReport(userId, date);
        }
    }

    /**
     * 일일 영양 분석 리포트 재생성 (AI 분석 포함)
     * Regenerate daily report with AI analysis
     */
    @Override
    @Transactional
    public DailyReportResponse regenerateDailyReport(Long userId, LocalDate date) {
        // 사용자 조회 / Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 해당 날짜의 섭취 기록 조회 / Get intake records for the date
        List<Intake> intakes = intakeRepository.findByUser_UserIdAndIntakeDate(userId, date);

        // 총 영양소 계산 / Calculate total nutrients
        NutrientSummary summary = calculateTotalNutrients(intakes);

        // 권장 영양소 조회 / Get required nutrients
        Essential requiredNutrients = getRequiredNutrients(user);

        // 영양소 상태 평가 / Evaluate nutrient status
        Analysis analysis = createAnalysis(user, date, summary, requiredNutrients);

        // AI 분석 결과 생성 (더미 데이터) / Generate AI analysis (dummy data)
        generateAIAnalysis(analysis, summary, requiredNutrients);

        // 분석 결과 저장 / Save analysis
        Analysis savedAnalysis = analysisRepository.save(analysis);

        return convertToResponse(savedAnalysis);
    }

    /**
     * 섭취 기록으로부터 총 영양소 계산
     * Calculate total nutrients from intake records
     */
    private NutrientSummary calculateTotalNutrients(List<Intake> intakes) {
        NutrientSummary summary = new NutrientSummary();

        for (Intake intake : intakes) {
            Food food = intake.getFood();
            double ratio = intake.getIntakeAmount() / food.getServingSize();

            summary.totalCalories += food.getCalories() * ratio;
            summary.totalCarbohydrate += food.getCarbohydrate() * ratio;
            summary.totalProtein += food.getProtein() * ratio;
            summary.totalFat += food.getFat() * ratio;
            summary.totalSodium += food.getSodium() * ratio;
            summary.totalCholesterol += food.getCholesterol() * ratio;
            summary.totalCalcium += food.getCalcium() * ratio;
        }

        return summary;
    }

    /**
     * 사용자의 권장 영양소 조회
     * Get required nutrients for user
     */
    private Essential getRequiredNutrients(User user) {
        // 연령대 계산 / Calculate age range
        String ageRange = calculateAgeRange(user.getAge());

        // 권장 영양소 조회 / Find required nutrients
        Optional<Essential> essential = essentialRepository.findByAgeRangeAndGender(ageRange, user.getGender());

        if (essential.isPresent()) {
            return essential.get();
        } else {
            // 기본값 반환 (20대 남성 기준) / Return default values (based on male in 20s)
            log.warn("Required nutrients not found for age range: {} and gender: {}. Using default values.", ageRange, user.getGender());
            return createDefaultEssential();
        }
    }

    /**
     * 연령대 계산 (예: 25세 -> "20-29")
     * Calculate age range (e.g., 25 -> "20-29")
     */
    private String calculateAgeRange(Integer age) {
        if (age == null) {
            return "20-29";
        }
        int lowerBound = (age / 10) * 10;
        int upperBound = lowerBound + 9;
        return lowerBound + "-" + upperBound;
    }

    /**
     * 기본 권장 영양소 생성
     * Create default required nutrients
     */
    private Essential createDefaultEssential() {
        return Essential.builder()
                .reqCalories(2500.0)
                .reqCarbohydrate(325.0)
                .reqProtein(65.0)
                .reqFat(55.0)
                .reqSodium(2000.0)
                .reqCholesterol(300.0)
                .reqCalcium(800.0)
                .build();
    }

    /**
     * 분석 엔티티 생성
     * Create analysis entity
     */
    private Analysis createAnalysis(User user, LocalDate date, NutrientSummary summary, Essential required) {
        return Analysis.builder()
                .user(user)
                .analysisDate(date)
                .totalCalories(summary.totalCalories)
                .totalCarbohydrate(summary.totalCarbohydrate)
                .totalProtein(summary.totalProtein)
                .totalFat(summary.totalFat)
                .totalSodium(summary.totalSodium)
                .totalCholesterol(summary.totalCholesterol)
                .totalCalcium(summary.totalCalcium)
                .caloriesStatus(evaluateNutrientStatus(summary.totalCalories, required.getReqCalories()))
                .carbohydrateStatus(evaluateNutrientStatus(summary.totalCarbohydrate, required.getReqCarbohydrate()))
                .proteinStatus(evaluateNutrientStatus(summary.totalProtein, required.getReqProtein()))
                .fatStatus(evaluateNutrientStatus(summary.totalFat, required.getReqFat()))
                .sodiumStatus(evaluateNutrientStatus(summary.totalSodium, required.getReqSodium()))
                .cholesterolStatus(evaluateNutrientStatus(summary.totalCholesterol, required.getReqCholesterol()))
                .calciumStatus(evaluateNutrientStatus(summary.totalCalcium, required.getReqCalcium()))
                .build();
    }

    /**
     * 영양소 상태 평가 (OVER, NORMAL, UNDER)
     * Evaluate nutrient status
     */
    private Analysis.NutrientStatus evaluateNutrientStatus(Double actual, Double required) {
        if (required == null || actual == null) {
            return Analysis.NutrientStatus.NORMAL;
        }

        double ratio = actual / required;

        if (ratio > 1.2) {
            return Analysis.NutrientStatus.OVER;
        } else if (ratio < 0.8) {
            return Analysis.NutrientStatus.UNDER;
        } else {
            return Analysis.NutrientStatus.NORMAL;
        }
    }

    /**
     * AI 분석 결과 생성 (더미 데이터)
     * Generate AI analysis results (dummy data)
     */
    private void generateAIAnalysis(Analysis analysis, NutrientSummary summary, Essential required) {
        // AI 종합 분석 더미 데이터 / AI summary dummy data
        String aiSummary = generateAISummary(analysis, summary, required);
        analysis.setAiSummary(aiSummary);

        // AI 개선 제안 더미 데이터 / AI recommendations dummy data
        String aiRecommendations = generateAIRecommendations(analysis, summary, required);
        analysis.setAiRecommendations(aiRecommendations);
    }

    /**
     * AI 종합 분석 더미 데이터 생성
     * Generate AI summary dummy data
     */
    private String generateAISummary(Analysis analysis, NutrientSummary summary, Essential required) {
        StringBuilder sb = new StringBuilder();

        sb.append("## 오늘의 영양 분석 결과\n\n");

        // 칼로리 분석 / Calorie analysis
        if (analysis.getCaloriesStatus() == Analysis.NutrientStatus.OVER) {
            sb.append("📊 **칼로리**: 권장량보다 ").append(String.format("%.0f", summary.totalCalories - required.getReqCalories()))
                    .append("kcal 초과 섭취하셨습니다. 체중 증가에 주의가 필요합니다.\n\n");
        } else if (analysis.getCaloriesStatus() == Analysis.NutrientStatus.UNDER) {
            sb.append("📊 **칼로리**: 권장량보다 ").append(String.format("%.0f", required.getReqCalories() - summary.totalCalories))
                    .append("kcal 부족합니다. 에너지 섭취가 부족할 수 있습니다.\n\n");
        } else {
            sb.append("📊 **칼로리**: 권장량에 적합한 양을 섭취하셨습니다.\n\n");
        }

        // 단백질 분석 / Protein analysis
        if (analysis.getProteinStatus() == Analysis.NutrientStatus.UNDER) {
            sb.append("🥩 **단백질**: 부족한 상태입니다. 근육 건강과 면역력 유지를 위해 단백질 섭취를 늘려주세요.\n\n");
        } else {
            sb.append("🥩 **단백질**: 적절히 섭취하고 계십니다.\n\n");
        }

        // 나트륨 분석 / Sodium analysis
        if (analysis.getSodiumStatus() == Analysis.NutrientStatus.OVER) {
            sb.append("🧂 **나트륨**: 과다 섭취 상태입니다. 고혈압과 심혈관 질환 위험이 증가할 수 있습니다.\n\n");
        }

        sb.append("전반적으로 균형잡힌 식단을 유지하고 계십니다. 계속해서 건강한 식습관을 유지해주세요!");

        return sb.toString();
    }

    /**
     * AI 개선 제안 더미 데이터 생성
     * Generate AI recommendations dummy data
     */
    private String generateAIRecommendations(Analysis analysis, NutrientSummary summary, Essential required) {
        StringBuilder sb = new StringBuilder();

        sb.append("## 식단 개선 제안\n\n");

        // 칼로리 기반 제안 / Calorie-based recommendations
        if (analysis.getCaloriesStatus() == Analysis.NutrientStatus.OVER) {
            sb.append("### 1. 칼로리 조절\n");
            sb.append("- 간식 섭취를 줄이고, 한 끼 식사량을 10-15% 정도 줄여보세요.\n");
            sb.append("- 고칼로리 음식 대신 채소와 과일을 더 섭취하세요.\n\n");
        } else if (analysis.getCaloriesStatus() == Analysis.NutrientStatus.UNDER) {
            sb.append("### 1. 칼로리 보충\n");
            sb.append("- 건강한 간식(견과류, 과일)을 추가하세요.\n");
            sb.append("- 한 끼 식사에 탄수화물과 단백질을 골고루 섭취하세요.\n\n");
        }

        // 단백질 기반 제안 / Protein-based recommendations
        if (analysis.getProteinStatus() == Analysis.NutrientStatus.UNDER) {
            sb.append("### 2. 단백질 섭취 증가\n");
            sb.append("- 추천 식품: 닭가슴살, 계란, 두부, 생선, 콩류\n");
            sb.append("- 매 끼니마다 손바닥 크기만큼의 단백질 식품을 포함하세요.\n\n");
        }

        // 나트륨 기반 제안 / Sodium-based recommendations
        if (analysis.getSodiumStatus() == Analysis.NutrientStatus.OVER) {
            sb.append("### 3. 나트륨 줄이기\n");
            sb.append("- 가공식품과 인스턴트 식품을 피하세요.\n");
            sb.append("- 국물 음식의 국물은 적게 드세요.\n");
            sb.append("- 소금 대신 허브나 향신료로 간을 하세요.\n\n");
        }

        // 칼슘 기반 제안 / Calcium-based recommendations
        if (analysis.getCalciumStatus() == Analysis.NutrientStatus.UNDER) {
            sb.append("### 4. 칼슘 섭취 증가\n");
            sb.append("- 추천 식품: 우유, 요구르트, 치즈, 뼈째 먹는 생선, 케일, 브로콜리\n");
            sb.append("- 하루 2-3회 유제품을 섭취하세요.\n\n");
        }

        sb.append("### 💡 추가 팁\n");
        sb.append("- 규칙적인 식사 시간을 유지하세요.\n");
        sb.append("- 충분한 수분 섭취를 잊지 마세요 (하루 2L 이상).\n");
        sb.append("- 주 3-4회 이상 규칙적인 운동을 병행하세요.");

        return sb.toString();
    }

    /**
     * Analysis 엔티티를 DailyReportResponse로 변환
     * Convert Analysis entity to DailyReportResponse
     */
    private DailyReportResponse convertToResponse(Analysis analysis) {
        // 권장 영양소 조회 / Get required nutrients
        Essential required = getRequiredNutrients(analysis.getUser());

        return DailyReportResponse.builder()
                .analysisId(analysis.getAnalysisId())
                .userId(analysis.getUser().getUserId())
                .analysisDate(analysis.getAnalysisDate())
                // 실제 섭취량 / Actual intake
                .totalCalories(analysis.getTotalCalories())
                .totalCarbohydrate(analysis.getTotalCarbohydrate())
                .totalProtein(analysis.getTotalProtein())
                .totalFat(analysis.getTotalFat())
                .totalSodium(analysis.getTotalSodium())
                .totalCholesterol(analysis.getTotalCholesterol())
                .totalCalcium(analysis.getTotalCalcium())
                // 권장 섭취량 / Required intake
                .reqCalories(required.getReqCalories())
                .reqCarbohydrate(required.getReqCarbohydrate())
                .reqProtein(required.getReqProtein())
                .reqFat(required.getReqFat())
                .reqSodium(required.getReqSodium())
                .reqCholesterol(required.getReqCholesterol())
                .reqCalcium(required.getReqCalcium())
                // 상태 / Status
                .caloriesStatus(analysis.getCaloriesStatus().name())
                .carbohydrateStatus(analysis.getCarbohydrateStatus().name())
                .proteinStatus(analysis.getProteinStatus().name())
                .fatStatus(analysis.getFatStatus().name())
                .sodiumStatus(analysis.getSodiumStatus().name())
                .cholesterolStatus(analysis.getCholesterolStatus().name())
                .calciumStatus(analysis.getCalciumStatus().name())
                // AI 분석 결과 / AI analysis
                .aiSummary(analysis.getAiSummary())
                .aiRecommendations(analysis.getAiRecommendations())
                .build();
    }

    /**
     * 영양소 합계를 저장하는 내부 클래스
     * Internal class to store nutrient totals
     */
    private static class NutrientSummary {
        double totalCalories = 0.0;
        double totalCarbohydrate = 0.0;
        double totalProtein = 0.0;
        double totalFat = 0.0;
        double totalSodium = 0.0;
        double totalCholesterol = 0.0;
        double totalCalcium = 0.0;
    }
}