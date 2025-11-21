package com.nutricheck.backend.config;

import com.nutricheck.backend.domain.*;
import com.nutricheck.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 초기 데이터 설정
 * Initial data configuration
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final MealTypeRepository mealTypeRepository;
    private final EssentialRepository essentialRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    /**
     * 식사 시간대 초기 데이터 삽입
     * Initialize meal type data
     */
    @Bean
    public CommandLineRunner initMealTypes() {
        return args -> {
            if (mealTypeRepository.count() == 0) {
                log.info("Initializing meal types...");

                // 아침 / Breakfast
                MealType breakfast = MealType.builder()
                        .mealName("BREAKFAST")
                        .displayName("아침")
                        .build();

                // 점심 / Lunch
                MealType lunch = MealType.builder()
                        .mealName("LUNCH")
                        .displayName("점심")
                        .build();

                // 저녁 / Dinner
                MealType dinner = MealType.builder()
                        .mealName("DINNER")
                        .displayName("저녁")
                        .build();

                // 간식 / Snack
                MealType snack = MealType.builder()
                        .mealName("SNACK")
                        .displayName("간식")
                        .build();

                mealTypeRepository.save(breakfast);
                mealTypeRepository.save(lunch);
                mealTypeRepository.save(dinner);
                mealTypeRepository.save(snack);

                log.info("Meal types initialized successfully");
            }
        };
    }

    /**
     * 권장 영양소 초기 데이터 삽입
     * Initialize required nutrients data
     */
    @Bean
    public CommandLineRunner initRequiredNutrients() {
        return args -> {
            if (essentialRepository.count() == 0) {
                log.info("Initializing required nutrients...");

                // 20대 남성 / Male in 20s
                Essential male20s = Essential.builder()
                        .ageRange("20-29")
                        .gender("M")
                        .reqCalories(2500.0)
                        .reqCarbohydrate(325.0)
                        .reqProtein(65.0)
                        .reqFat(55.0)
                        .reqSaturatedFat(15.0)
                        .reqTransFat(2.0)
                        .reqSugar(50.0)
                        .reqSodium(2000.0)
                        .reqCholesterol(300.0)
                        .reqCalcium(800.0)
                        .reqFiber(25.0)
                        .build();

                // 20대 여성 / Female in 20s
                Essential female20s = Essential.builder()
                        .ageRange("20-29")
                        .gender("F")
                        .reqCalories(2000.0)
                        .reqCarbohydrate(260.0)
                        .reqProtein(55.0)
                        .reqFat(45.0)
                        .reqSaturatedFat(12.0)
                        .reqTransFat(2.0)
                        .reqSugar(40.0)
                        .reqSodium(1500.0)
                        .reqCholesterol(300.0)
                        .reqCalcium(800.0)
                        .reqFiber(20.0)
                        .build();

                essentialRepository.save(male20s);
                essentialRepository.save(female20s);

                log.info("Required nutrients initialized successfully");
            }
        };
    }

    /**
     * 테스트용 샘플 데이터 삽입
     * Initialize sample data for testing
     */


    @Bean
    public CommandLineRunner initSampleData() {
        return args -> {

            if (userRepository.count() == 0) {
                log.info("Initializing sample data...");

                // 테스트 사용자 생성 / Create test user
                User testUser = User.builder()
                        .username("testuser")
                        .name("testusername")
                        .password("password123") // TODO: 실제 환경에서는 암호화 필요 / Should be encrypted in production
                        .email("test@example.com")
                        .age(25)
                        .gender("M")
                        .build();

                userRepository.save(testUser);
                log.info("Test user created: {}", testUser.getUsername());
            }

            if (foodRepository.count() == 0) {
                log.info("Initializing sample foods...");

                // 백미밥 / White rice
                Food rice = Food.builder()
                        .foodName("백미밥")
                        .servingSize(100.0)
                        .servingUnit("g")
                        .calories(130.0)
                        .carbohydrate(28.7)
                        .protein(2.5)
                        .fat(0.3)
                        .saturatedFat(0.1)
                        .transFat(0.0)
                        .unsaturatedFat(0.2)
                        .sugar(0.0)
                        .sodium(5.0)
                        .cholesterol(0.0)
                        .calcium(3.0)
                        .fiber(0.3)
                        .build();

                // 김치찌개 / Kimchi stew
                Food kimchiStew = Food.builder()
                        .foodName("김치찌개")
                        .servingSize(100.0)
                        .servingUnit("g")
                        .calories(50.0)
                        .carbohydrate(5.2)
                        .protein(4.1)
                        .fat(2.8)
                        .saturatedFat(0.8)
                        .transFat(0.0)
                        .unsaturatedFat(1.5)
                        .sugar(1.2)
                        .sodium(450.0)
                        .cholesterol(15.0)
                        .calcium(40.0)
                        .fiber(1.0)
                        .build();

                // 닭가슴살 / Chicken breast
                Food chickenBreast = Food.builder()
                        .foodName("닭가슴살")
                        .servingSize(100.0)
                        .servingUnit("g")
                        .calories(165.0)
                        .carbohydrate(0.0)
                        .protein(31.0)
                        .fat(3.6)
                        .saturatedFat(1.0)
                        .transFat(0.0)
                        .unsaturatedFat(2.0)
                        .sugar(0.0)
                        .sodium(74.0)
                        .cholesterol(85.0)
                        .calcium(15.0)
                        .fiber(0.0)
                        .build();

                // 사과 / Apple
                Food apple = Food.builder()
                        .foodName("사과")
                        .servingSize(100.0)
                        .servingUnit("g")
                        .calories(52.0)
                        .carbohydrate(14.0)
                        .protein(0.3)
                        .fat(0.2)
                        .saturatedFat(0.0)
                        .transFat(0.0)
                        .unsaturatedFat(0.1)
                        .sugar(10.4)
                        .sodium(1.0)
                        .cholesterol(0.0)
                        .calcium(6.0)
                        .fiber(2.4)
                        .build();

                // 계란 / Egg
                Food egg = Food.builder()
                        .foodName("계란")
                        .servingSize(50.0)
                        .servingUnit("g")
                        .calories(78.0)
                        .carbohydrate(0.6)
                        .protein(6.3)
                        .fat(5.3)
                        .saturatedFat(1.6)
                        .transFat(0.0)
                        .unsaturatedFat(3.0)
                        .sugar(0.6)
                        .sodium(71.0)
                        .cholesterol(186.0)
                        .calcium(28.0)
                        .fiber(0.0)
                        .build();

                foodRepository.save(rice);
                foodRepository.save(kimchiStew);
                foodRepository.save(chickenBreast);
                foodRepository.save(apple);
                foodRepository.save(egg);

                log.info("Sample foods initialized successfully");
            }
        };
    }
}