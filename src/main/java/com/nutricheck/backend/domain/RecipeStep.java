package com.nutricheck.backend.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 레시피 조리 단계 엔티티
 * Recipe cooking step entity
 */
@Entity
@Table(name = "recipe_steps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RecipeStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "step_id")
    private Long stepId;

    /**
     * 소속 레시피
     * Parent recipe
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    @ToString.Exclude
    private Recipe recipe;

    /**
     * 단계 번호
     * Step number
     */
    @Column(name = "step_number", nullable = false)
    private Integer stepNumber;

    /**
     * 단계 설명
     * Step instruction
     */
    @Column(name = "instruction", columnDefinition = "TEXT", nullable = false)
    private String instruction;

    /**
     * 단계 이미지 URL (선택)
     * Step image URL (optional)
     */
    @Column(name = "image_url", length = 500)
    private String imageUrl;

    /**
     * 예상 소요 시간 (분)
     * Estimated time in minutes
     */
    @Column(name = "estimated_time")
    private Integer estimatedTime;
}