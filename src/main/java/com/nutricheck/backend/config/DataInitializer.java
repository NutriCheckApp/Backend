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

//    private final MealTypeRepository mealTypeRepository;
//    private final EssentialRepository essentialRepository;
    private final UserRepository userRepository;
//    private final FoodRepository foodRepository;



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
                        .build();

                userRepository.save(testUser);
                log.info("Test user created: {}", testUser.getUsername());
            }

            /* ToDo: 레시피 데이터 임의로 추가 / Add Receipe Data
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
            }*/
        };
    }
}