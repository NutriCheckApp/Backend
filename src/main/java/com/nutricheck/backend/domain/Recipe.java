package com.nutricheck.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 강아지 요리 레시피 엔티티
 * Dog recipe entity
 */
@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Long recipeId;

    /**
     * 레시피 이름
     * Recipe name
     */
    @Column(name = "recipe_name", nullable = false, length = 100)
    private String recipeName;

    /**
     * 레시피 설명
     * Recipe description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 총 칼로리 (kcal)
     * Total calories in kcal
     */
    @Column(name = "calories")
    private Double calories;

    /**
     * 탄수화물 (g)
     * Carbohydrate in grams
     */
    @Column(name = "carbohydrate")
    private Double carbohydrate;

    /**
     * 단백질 (g)
     * Protein in grams
     */
    @Column(name = "protein")
    private Double protein;

    /**
     * 지방 (g)
     * Fat in grams
     */
    @Column(name = "fat")
    private Double fat;

    /**
     * 식이섬유 (g)
     * Dietary fiber in grams
     */
    @Column(name = "fiber")
    private Double fiber;

    /**
     * 수분 (g)
     * Moisture in grams
     */
    @Column(name = "moisture")
    private Double moisture;

    // ========================================
    // 미네랄 / Minerals
    // ========================================

    /**
     * 칼슘 (mg) - 뼈, 치아 건강
     * Calcium in milligrams - bone and teeth health
     */
    @Column(name = "calcium")
    private Double calcium;

    /**
     * 인 (mg) - 뼈 건강, 칼슘과 균형 중요
     * Phosphorus in milligrams - bone health, balance with calcium
     */
    @Column(name = "phosphorus")
    private Double phosphorus;

    /**
     * 나트륨 (mg) - 전해질 균형
     * Sodium in milligrams - electrolyte balance
     */
    @Column(name = "sodium")
    private Double sodium;

    /**
     * 철분 (mg) - 혈액 건강
     * Iron in milligrams - blood health
     */
    @Column(name = "iron")
    private Double iron;

    /**
     * 아연 (mg) - 피부, 면역 체계
     * Zinc in milligrams - skin and immune system
     */
    @Column(name = "zinc")
    private Double zinc;

    // ========================================
    // 지방산 / Fatty Acids
    // ========================================

    /**
     * 오메가3 지방산 (g) - 피부, 털, 관절 건강
     * Omega-3 fatty acids in grams - skin, coat, joint health
     */
    @Column(name = "omega3")
    private Double omega3;

    /**
     * 오메가6 지방산 (g) - 피부, 털 건강
     * Omega-6 fatty acids in grams - skin and coat health
     */
    @Column(name = "omega6")
    private Double omega6;

    /**
     * 조리 시간 (분)
     * Cooking time in minutes
     */
    @Column(name = "cooking_time")
    private Integer cookingTime;

    /**
     * 난이도 (EASY, MEDIUM, HARD)
     * Difficulty level
     */
    @Column(name = "difficulty", length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Difficulty difficulty = Difficulty.EASY;

    /**
     * 레시피 이미지 URL
     * Recipe image URL
     */
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    /**
     * 적합한 강아지 크기 (소형, 중형, 대형, 전체)
     * Suitable pet size
     */
    @Column(name = "suitable_pet_size", length = 20)
    private String suitablePetSize;

    /**
     * 조리 방법 (텍스트 형태)
     * Cooking instructions in text format
     */
    @Column(name = "cooking_instructions", columnDefinition = "TEXT")
    private String cookingInstructions;

    /**
     * 레시피 재료 목록
     * Recipe ingredients list
     */
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    /**
     * 조리 단계 목록
     * Cooking steps list
     */
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<RecipeStep> steps = new ArrayList<>();

    /**
     * 생성일시
     * Creation timestamp
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 수정일시
     * Update timestamp
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 난이도 열거형
     * Difficulty enumeration
     */
    public enum Difficulty {
        EASY,      // 쉬움
        MEDIUM,    // 보통
        HARD       // 어려움
    }
}
