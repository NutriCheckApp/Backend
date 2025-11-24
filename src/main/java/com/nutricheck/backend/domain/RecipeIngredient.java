package com.nutricheck.backend.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 레시피 재료 엔티티
 * Recipe ingredient entity
 */
@Entity
@Table(name = "recipe_ingredients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Long ingredientId;

    /**
     * 소속 레시피
     * Parent recipe
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    @ToString.Exclude
    private Recipe recipe;

    /**
     * 재료명
     * Ingredient name
     */
    @Column(name = "ingredient_name", nullable = false, length = 100)
    private String ingredientName;

    /**
     * 재료 양
     * Ingredient amount
     */
    @Column(name = "amount")
    private Double amount;

    /**
     * 재료 단위 (g, ml, 개, 큰술 등)
     * Unit (g, ml, piece, tablespoon, etc.)
     */
    @Column(name = "unit", length = 20)
    private String unit;

    /**
     * 표시 순서
     * Display order
     */
    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;
}