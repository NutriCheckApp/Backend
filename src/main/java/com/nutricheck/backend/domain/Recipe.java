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
     * 조단백질 (g)
     * CrudeProtein in grams
     */
    @Column(name = "CrudeProtein")
    private Double protein;

    /**
     * 조지방 (g)
     * CrudeFat in grams
     */
    @Column(name = "CrudeFat")
    private Double fat;

    /**
     * 조섬유질 (g)
     * CrudeFiber in grams
     */
    @Column(name = "CrudeFiber")
    private Double fiber;

    /**
     * 칼슘 (mg) - 뼈, 치아 건강
     * Calcium in milligrams - bone and teeth health
     */
    @Column(name = "calcium")
    private Double calcium;

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
