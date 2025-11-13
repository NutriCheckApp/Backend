package com.nutricheck.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 식품 섭취 기록 엔티티
 * Food intake record entity
 */
@Entity
@Table(name = "food_intake",
        indexes = {
                @Index(name = "idx_intake_user_date", columnList = "user_id, intake_date"),
                @Index(name = "idx_intake_date", columnList = "intake_date")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Intake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intake_id")
    private Long intakeId;

    /**
     * 사용자 (다대일 관계)
     * User reference (Many-to-One)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 식품 (다대일 관계)
     * Food reference (Many-to-One)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    /**
     * 식사 시간대 (다대일 관계)
     * Meal type reference (Many-to-One)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_type_id", nullable = false)
    private MealType mealType;

    /**
     * 섭취 날짜
     * Intake date
     */
    @Column(name = "intake_date", nullable = false)
    private LocalDate intakeDate;

    /**
     * 섭취 시간 (정확한 시간 기록)
     * Intake time (precise timestamp)
     */
    @Column(name = "intake_time")
    private LocalDateTime intakeTime;

    /**
     * 섭취량 (g)
     * Intake amount in grams
     */
    @Column(name = "intake_amount", nullable = false)
    private Double intakeAmount;

    /**
     * 섭취 날짜 자동 설정 (시간으로부터)
     * Automatically set intake date from intake time
     */
    @PrePersist
    public void prePersist() {
        if (intakeTime != null && intakeDate == null) {
            intakeDate = intakeTime.toLocalDate();
        }
        if (intakeTime == null) {
            intakeTime = LocalDateTime.now();
            intakeDate = LocalDate.now();
        }
    }
}
