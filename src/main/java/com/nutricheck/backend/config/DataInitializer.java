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
    private final RecipeRepository recipeRepository;



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

            // 레시피 데이터 초기화 / Initialize recipe data
            if (recipeRepository.count() == 0) {
                log.info("Initializing recipes...");
                initializeRecipes();
                log.info("12 recipes initialized successfully");
            }
        };
    }

    /**
     * 12개 레시피 초기화
     * Initialize 12 recipes
     */
    private void initializeRecipes() {
        // 1. 단호박빵
        createRecipe1();
        // 2. 쿠키
        createRecipe2();
        // 3. 당근케이크
        createRecipe3();
        // 4. 마가레뜨
        createRecipe4();
        // 5. 햄버그스테이크
        createRecipe5();
        // 6. 참치 샐러드
        createRecipe6();
        // 7. 참치 볶음밥
        createRecipe7();
        // 8. 배잡채
        createRecipe8();
        // 9. 부침개
        createRecipe9();
        // 10. 치즈핫도그
        createRecipe10();
        // 11. 사라다
        createRecipe11();
        // 12. 안티에이징스무디
        createRecipe12();
    }

    private void createRecipe1() {
        Recipe recipe = Recipe.builder()
                .recipeName("단호박빵")
                .description("강아지를 위한 건강한 단호박빵")
                .calories(200.1)
                .protein(10.28)
                .fat(10.53)
                .calcium(129.7)
                .cookingTime(20)
                .difficulty(Recipe.Difficulty.EASY)
                .build();

        // 재료 추가
        recipe.getIngredients().add(createIngredient(recipe, "단호박", 100.0, "g", 1));
        recipe.getIngredients().add(createIngredient(recipe, "쌀가루", 7.0, "g", 2));
        recipe.getIngredients().add(createIngredient(recipe, "달걀", 1.0, "개", 3));
        recipe.getIngredients().add(createIngredient(recipe, "식물성 오일", 3.0, "g", 4));
        recipe.getIngredients().add(createIngredient(recipe, "펫 밀크", 10.0, "g", 5));

        // 조리 단계 추가
        recipe.getSteps().add(createStep(recipe, 1, "단호박을 쪄주고 으깬다."));
        recipe.getSteps().add(createStep(recipe, 2, "계란에서 노른자를 분리하여 추가하여 섞는다."));
        recipe.getSteps().add(createStep(recipe, 3, "펫 밀크 10g을 추가하여 섞는다."));
        recipe.getSteps().add(createStep(recipe, 4, "쌀가루 7g을 추가하여 섞는다."));
        recipe.getSteps().add(createStep(recipe, 5, "식물성 오일 3g을 추가하여 섞는다."));
        recipe.getSteps().add(createStep(recipe, 6, "짤주머니에 반죽을 소분하여 넣고 에어프라이어에 170도 10분을 굽는다."));

        recipeRepository.save(recipe);
    }

    private void createRecipe2() {
        Recipe recipe = Recipe.builder()
                .recipeName("쿠키")
                .description("강아지를 위한 수제 쿠키")
                .calories(1302.8)
                .protein(27.56)
                .fat(53.56)
                .calcium(78.0)
                .cookingTime(30)
                .difficulty(Recipe.Difficulty.EASY)
                .build();

        recipe.getIngredients().add(createIngredient(recipe, "계란", 2.0, "개", 1));
        recipe.getIngredients().add(createIngredient(recipe, "쌀가루", 220.0, "g", 2));
        recipe.getIngredients().add(createIngredient(recipe, "식물성 오일", 40.0, "g", 3));
        recipe.getIngredients().add(createIngredient(recipe, "물", 30.0, "g", 4));

        recipe.getSteps().add(createStep(recipe, 1, "달걀 2개를 풀고 오일을 40g 넣어 섞는다."));
        recipe.getSteps().add(createStep(recipe, 2, "쌀가루 220g을 넣고 반죽으로 만든다."));
        recipe.getSteps().add(createStep(recipe, 3, "물 30g을 넣는다."));
        recipe.getSteps().add(createStep(recipe, 4, "반죽을 밀대로 밀고 쿠키틀로 찍는다."));
        recipe.getSteps().add(createStep(recipe, 5, "예열된 오븐에 180도 20분을 굽니다."));

        recipeRepository.save(recipe);
    }

    private void createRecipe3() {
        Recipe recipe = Recipe.builder()
                .recipeName("당근케이크")
                .description("오리 안심과 당근이 들어간 건강한 케이크")
                .calories(350.9)
                .protein(16.89)
                .fat(15.38)
                .calcium(46.3)
                .cookingTime(40)
                .difficulty(Recipe.Difficulty.MEDIUM)
                .build();

        recipe.getIngredients().add(createIngredient(recipe, "오리 안심", 40.0, "g", 1));
        recipe.getIngredients().add(createIngredient(recipe, "당근", 30.0, "g", 2));
        recipe.getIngredients().add(createIngredient(recipe, "쌀가루", 40.0, "g", 3));
        recipe.getIngredients().add(createIngredient(recipe, "계란", 1.0, "개", 4));
        recipe.getIngredients().add(createIngredient(recipe, "식물성 오일", 5.0, "g", 5));
        recipe.getIngredients().add(createIngredient(recipe, "물", 10.0, "g", 6));

        recipe.getSteps().add(createStep(recipe, 1, "오리 안심과 당근 갈기"));
        recipe.getSteps().add(createStep(recipe, 2, "계란에서 노른자만 분리"));
        recipe.getSteps().add(createStep(recipe, 3, "노른자에 오일 5g 넣고 섞기"));
        recipe.getSteps().add(createStep(recipe, 4, "쌀가루를 추가하여 반죽으로 만들기"));
        recipe.getSteps().add(createStep(recipe, 5, "물 10g을 추가하기"));
        recipe.getSteps().add(createStep(recipe, 6, "갈은 당근과 오리고기를 반죽에 넣고 섞기"));
        recipe.getSteps().add(createStep(recipe, 7, "컵케이크 틀에 반죽을 넣고 오븐 170도 20-30분 굽기"));

        recipeRepository.save(recipe);
    }

    private void createRecipe4() {
        Recipe recipe = Recipe.builder()
                .recipeName("마가레뜨")
                .description("고구마가 들어간 건강한 과자")
                .calories(350.9)
                .protein(16.89)
                .fat(15.38)
                .calcium(46.3)
                .cookingTime(25)
                .difficulty(Recipe.Difficulty.MEDIUM)
                .build();

        recipe.getIngredients().add(createIngredient(recipe, "쌀가루", 110.0, "g", 1));
        recipe.getIngredients().add(createIngredient(recipe, "식물성 오일", 10.0, "g", 2));
        recipe.getIngredients().add(createIngredient(recipe, "계란", 1.5, "개", 3));
        recipe.getIngredients().add(createIngredient(recipe, "고구마", 40.0, "g", 4));
        recipe.getIngredients().add(createIngredient(recipe, "물", 15.0, "g", 5));

        recipe.getSteps().add(createStep(recipe, 1, "계란과 오일을 섞는다."));
        recipe.getSteps().add(createStep(recipe, 2, "쌀가루를 추가하여 반죽을 만든다."));
        recipe.getSteps().add(createStep(recipe, 3, "물을 추가한다."));
        recipe.getSteps().add(createStep(recipe, 4, "고구마를 으깨 반죽과 섞는다."));
        recipe.getSteps().add(createStep(recipe, 5, "반죽을 떼어내어 계란물을 앞뒤로 묻힌다."));
        recipe.getSteps().add(createStep(recipe, 6, "오븐 160도로 앞면 10분, 뒷면 3분을 굽는다."));

        recipeRepository.save(recipe);
    }

    private void createRecipe5() {
        Recipe recipe = Recipe.builder()
                .recipeName("햄버그스테이크")
                .description("닭고기로 만든 건강한 햄버그스테이크")
                .calories(140.0)
                .protein(16.6)
                .fat(7.6)
                .calcium(8.3)
                .cookingTime(15)
                .difficulty(Recipe.Difficulty.EASY)
                .build();

        recipe.getIngredients().add(createIngredient(recipe, "닭 안심", 40.0, "g", 1));
        recipe.getIngredients().add(createIngredient(recipe, "부챗살", 35.0, "g", 2));
        recipe.getIngredients().add(createIngredient(recipe, "무염버터", 5.0, "g", 3));

        recipe.getSteps().add(createStep(recipe, 1, "닭 안심 40g, 부챗살 35g을 잘게 다진다."));
        recipe.getSteps().add(createStep(recipe, 2, "다져진 고기를 섞어 동그랗게 뭉친다."));
        recipe.getSteps().add(createStep(recipe, 3, "양 손바닥을 오가며 30초 정도 치댄다."));
        recipe.getSteps().add(createStep(recipe, 4, "중심부를 눌러 움푹 들어가게 만든다"));
        recipe.getSteps().add(createStep(recipe, 5, "달궈진 팬에 무염버터를 두르고 익힌다."));
        recipe.getSteps().add(createStep(recipe, 6, "1분 동안 굽고 뒤집어 뚜껑을 덮고 6분간 굽는다."));

        recipeRepository.save(recipe);
    }

    private void createRecipe6() {
        Recipe recipe = Recipe.builder()
                .recipeName("참치 샐러드")
                .description("신선한 채소와 참치로 만든 샐러드")
                .calories(59.0)
                .protein(9.7)
                .fat(1.5)
                .calcium(11.0)
                .cookingTime(10)
                .difficulty(Recipe.Difficulty.EASY)
                .build();

        recipe.getIngredients().add(createIngredient(recipe, "참치(물담금)", 40.0, "g", 1));
        recipe.getIngredients().add(createIngredient(recipe, "오이", 20.0, "g", 2));
        recipe.getIngredients().add(createIngredient(recipe, "토마토", 20.0, "g", 3));
        recipe.getIngredients().add(createIngredient(recipe, "양상추", 10.0, "g", 4));
        recipe.getIngredients().add(createIngredient(recipe, "카놀라유", 1.0, "g", 5));

        recipe.getSteps().add(createStep(recipe, 1, "참치캔을 끓는 물에 10초 정도 살짝 데쳐 기름과 염분을 완전히 제거한다."));
        recipe.getSteps().add(createStep(recipe, 2, "토마토, 오이, 양상추를 강아지가 먹기 좋은 크기로 잘게 썬다."));
        recipe.getSteps().add(createStep(recipe, 3, "볼에 모든 식재료(참치 + 채소)를 담고 가볍게 섞어 준 뒤, 아주 소량의 카놀라유를 뿌린다."));

        recipeRepository.save(recipe);
    }

    private void createRecipe7() {
        Recipe recipe = Recipe.builder()
                .recipeName("참치 볶음밥")
                .description("참치와 야채가 들어간 영양만점 볶음밥")
                .calories(176.0)
                .protein(12.2)
                .fat(1.8)
                .calcium(16.0)
                .cookingTime(15)
                .difficulty(Recipe.Difficulty.EASY)
                .build();

        recipe.getIngredients().add(createIngredient(recipe, "참치(물담금)", 40.0, "g", 1));
        recipe.getIngredients().add(createIngredient(recipe, "양상추", 40.0, "g", 2));
        recipe.getIngredients().add(createIngredient(recipe, "빨강 파프리카", 30.0, "g", 3));
        recipe.getIngredients().add(createIngredient(recipe, "즉석밥", 80.0, "g", 4));
        recipe.getIngredients().add(createIngredient(recipe, "카놀라유", 1.0, "g", 5));

        recipe.getSteps().add(createStep(recipe, 1, "참치캔을 끓는 물에 10초 정도 데쳐 기름과 염분을 완전히 제거한다."));
        recipe.getSteps().add(createStep(recipe, 2, "양상추 40g, 빨강 파프리카 30g을 잘게 썬 뒤 참치와 함께 팬에 넣고 가볍게 볶는다."));
        recipe.getSteps().add(createStep(recipe, 3, "재료가 적당히 익으면 즉석밥 약 80g을 넣고 한 번 더 볶아 완성한다."));

        recipeRepository.save(recipe);
    }

    private void createRecipe8() {
        Recipe recipe = Recipe.builder()
                .recipeName("배잡채")
                .description("배와 소고기로 만든 건강한 잡채")
                .calories(61.0)
                .protein(4.25)
                .fat(1.82)
                .calcium(14.7)
                .cookingTime(20)
                .difficulty(Recipe.Difficulty.EASY)
                .build();

        recipe.getIngredients().add(createIngredient(recipe, "소고기", 17.0, "g", 1));
        recipe.getIngredients().add(createIngredient(recipe, "배", 20.0, "g", 2));
        recipe.getIngredients().add(createIngredient(recipe, "감자", 17.0, "g", 3));
        recipe.getIngredients().add(createIngredient(recipe, "브로콜리", 20.0, "g", 4));

        recipe.getSteps().add(createStep(recipe, 1, "고기를 채썰기해 완전히 익을 때까지 볶는다."));
        recipe.getSteps().add(createStep(recipe, 2, "익힌 배와 감자를 채썰기한다."));
        recipe.getSteps().add(createStep(recipe, 3, "고기와 감자, 배를 모두 섞어 그릇에 담는다."));

        recipeRepository.save(recipe);
    }

    private void createRecipe9() {
        Recipe recipe = Recipe.builder()
                .recipeName("부침개")
                .description("닭간과 브로콜리로 만든 영양 부침개")
                .calories(365.4)
                .protein(33.54)
                .fat(12.26)
                .calcium(69.9)
                .cookingTime(120)
                .difficulty(Recipe.Difficulty.MEDIUM)
                .build();

        recipe.getIngredients().add(createIngredient(recipe, "쌀가루", 30.0, "g", 1));
        recipe.getIngredients().add(createIngredient(recipe, "닭간", 140.0, "g", 2));
        recipe.getIngredients().add(createIngredient(recipe, "브로콜리", 50.0, "g", 3));
        recipe.getIngredients().add(createIngredient(recipe, "계란", 1.0, "개", 4));

        recipe.getSteps().add(createStep(recipe, 1, "차가운 물과 식초를 사용해 닭간의 핏물, 기름, 불순물을 제거한다(1시간 30분)."));
        recipe.getSteps().add(createStep(recipe, 2, "브로콜리를 살짝 데쳐준다."));
        recipe.getSteps().add(createStep(recipe, 3, "믹서기에 닭고기와 브로콜리를 넣고 갈아주고 볼에 넣는다."));
        recipe.getSteps().add(createStep(recipe, 4, "쌀가루 30g도 볼에 넣고 젓는다."));
        recipe.getSteps().add(createStep(recipe, 5, "계란 1알도 볼에 넣고 저어 부침개 반죽을 완성한다."));
        recipe.getSteps().add(createStep(recipe, 6, "달궈진 프라이팬에 원하는 사이즈의 부침개 반죽을 부워 구워준다."));
        recipe.getSteps().add(createStep(recipe, 7, "적절한 양만큼 나누어 강아지에게 제공한다."));

        recipeRepository.save(recipe);
    }

    private void createRecipe10() {
        Recipe recipe = Recipe.builder()
                .recipeName("치즈핫도그")
                .description("사슴고기와 치즈가 들어간 특별한 핫도그")
                .calories(1113.0)
                .protein(88.07)
                .fat(26.04)
                .calcium(269.6)
                .cookingTime(60)
                .difficulty(Recipe.Difficulty.HARD)
                .build();

        recipe.getIngredients().add(createIngredient(recipe, "쌀가루", 130.0, "g", 1));
        recipe.getIngredients().add(createIngredient(recipe, "고구마", 100.0, "g", 2));
        recipe.getIngredients().add(createIngredient(recipe, "사슴고기", 200.0, "g", 3));
        recipe.getIngredients().add(createIngredient(recipe, "계란", 2.0, "개", 4));
        recipe.getIngredients().add(createIngredient(recipe, "단호박가루", 3.0, "g", 5));
        recipe.getIngredients().add(createIngredient(recipe, "치즈", 1.0, "장", 6));

        recipe.getSteps().add(createStep(recipe, 1, "사슴고기를 식초물에 담가 20~30분동안 소독한다."));
        recipe.getSteps().add(createStep(recipe, 2, "쌀가루를 채에 쳐 볼에 담아준다."));
        recipe.getSteps().add(createStep(recipe, 3, "계란 2알도 볼에 넣고 섞어서 반죽을 만든다."));
        recipe.getSteps().add(createStep(recipe, 4, "고구마를 익힌 다음 으깨준다."));
        recipe.getSteps().add(createStep(recipe, 5, "고구마를 반죽과 잘 섞어준다."));
        recipe.getSteps().add(createStep(recipe, 6, "단호박 가루 3g을 넣고 섞어준다."));
        recipe.getSteps().add(createStep(recipe, 7, "완성된 최종 반죽을 동일한 크기로 나눠 준비해준다."));
        recipe.getSteps().add(createStep(recipe, 8, "소독한 사슴고기를 분쇄기에 단 다음, 반죽 개수에 맞춰 고기를 나눠준다."));
        recipe.getSteps().add(createStep(recipe, 9, "고기 위에 치즈를 감싸고, 반죽을 감싸서 핫도그 모양을 잡아준다."));
        recipe.getSteps().add(createStep(recipe, 10, "에어프라이어 100도에서 앞면 20분 뒷면 10분을 돌려준다."));
        recipe.getSteps().add(createStep(recipe, 11, "적절한 양으로 강아지에게 제공한다."));

        recipeRepository.save(recipe);
    }

    private void createRecipe11() {
        Recipe recipe = Recipe.builder()
                .recipeName("사라다")
                .description("감자, 당근, 사과로 만든 간단한 샐러드")
                .calories(457.8)
                .protein(9.91)
                .fat(0.90)
                .calcium(69.3)
                .cookingTime(20)
                .difficulty(Recipe.Difficulty.EASY)
                .build();

        recipe.getIngredients().add(createIngredient(recipe, "감자", 3.0, "알", 1));
        recipe.getIngredients().add(createIngredient(recipe, "당근", 0.67, "개", 2));
        recipe.getIngredients().add(createIngredient(recipe, "사과", 1.0, "개", 3));

        recipe.getSteps().add(createStep(recipe, 1, "감자를 푹 쪄준다."));
        recipe.getSteps().add(createStep(recipe, 2, "감자 껍질을 벗겨 손으로 잘 으깨준다."));
        recipe.getSteps().add(createStep(recipe, 3, "사과 껍질을 벗기 다음, 먹기 좋은 사이즈로 잘라준다."));
        recipe.getSteps().add(createStep(recipe, 4, "당근도 깨끗하게 씻고, 커팅해준다."));
        recipe.getSteps().add(createStep(recipe, 5, "볼에 모든 재료를 넣고 섞어준다."));
        recipe.getSteps().add(createStep(recipe, 6, "적절한 양을 강아지에게 제공한다."));

        recipeRepository.save(recipe);
    }

    private void createRecipe12() {
        Recipe recipe = Recipe.builder()
                .recipeName("안티에이징스무디")
                .description("항산화 성분이 풍부한 건강 스무디")
                .calories(173.2)
                .protein(8.26)
                .fat(3.05)
                .calcium(108.0)
                .cookingTime(15)
                .difficulty(Recipe.Difficulty.MEDIUM)
                .build();

        recipe.getIngredients().add(createIngredient(recipe, "바나나", 100.0, "g", 1));
        recipe.getIngredients().add(createIngredient(recipe, "블루베리", 50.0, "g", 2));
        recipe.getIngredients().add(createIngredient(recipe, "두부", 50.0, "g", 3));
        recipe.getIngredients().add(createIngredient(recipe, "가루젤라틴", 1.0, "티스푼", 4));
        recipe.getIngredients().add(createIngredient(recipe, "우유", 20.0, "ml", 5));

        recipe.getSteps().add(createStep(recipe, 1, "젤라틴을 먼저 젤라틴의 3배정도 되는 양의 물에 불려준다.(5분)"));
        recipe.getSteps().add(createStep(recipe, 2, "두부를 조각조각 잘라서 뜨거운 물에 넣어 데친다."));
        recipe.getSteps().add(createStep(recipe, 3, "두부가 익었다 싶으면 믹서기에 건져서 물에 불려 놓은 젤라틴과 섞어준다."));
        recipe.getSteps().add(createStep(recipe, 4, "그 위로 우유, 블루베리, 바나나를 넣어준다.(젤라틴이 굳기 전에 빨리 해야함)"));
        recipe.getSteps().add(createStep(recipe, 5, "재료를 다 넣었으면 믹서기로 갈아준다."));
        recipe.getSteps().add(createStep(recipe, 6, "(선택) 냉장고에 약 1시간 정도 넣었다가 꺼내주면 푸딩 같은 질감으로 완성된다."));
        recipe.getSteps().add(createStep(recipe, 7, "적절한 양을 강아지에게 제공한다."));

        recipeRepository.save(recipe);
    }

    /**
     * RecipeIngredient 생성 헬퍼 메서드
     */
    private RecipeIngredient createIngredient(Recipe recipe, String name, Double amount, String unit, int order) {
        return RecipeIngredient.builder()
                .recipe(recipe)
                .ingredientName(name)
                .amount(amount)
                .unit(unit)
                .displayOrder(order)
                .build();
    }

    /**
     * RecipeStep 생성 헬퍼 메서드
     */
    private RecipeStep createStep(Recipe recipe, int stepNumber, String instruction) {
        return RecipeStep.builder()
                .recipe(recipe)
                .stepNumber(stepNumber)
                .instruction(instruction)
                .build();
    }
}