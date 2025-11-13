package com.nutricheck.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * AI 추천 주간 식단표 엔티티
 * AI-recommended weekly meal plan entity
 */
@Entity
@Table(name = "weekly_meal_plan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyMealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long planId;

    /**
     * 사용자 (다대일 관계)
     * User reference (Many-to-One)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 주의 시작일 (월요일)
     * Week start date (Monday)
     */
    @Column(name = "week_start_date", nullable = false)
    private LocalDate weekStartDate;

    /**
     * 식단 상세 목록 (일대다 관계)
     * Meal plan details (One-to-Many)
     */
    @OneToMany(mappedBy = "weeklyMealPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MealPlanDetail> mealPlanDetails = new ArrayList<>();

    /**
     * 생성일시
     * Creation timestamp
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}