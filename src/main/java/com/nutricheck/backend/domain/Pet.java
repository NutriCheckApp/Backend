package com.nutricheck.backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 반려견 정보 엔티티
 * Pet information entity
 */
@Entity
@Table(name = "pets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long petId;

    /**
     * 반려견 이름
     * Pet name / (선택) Optional Column
     */
    @Column(name = "pet_name", nullable = false, length = 50)
    private String petName;

    /**
     * 반려견 품종 / (선택) Optional Column
     * Pet breed
     */
    @Column(name = "pet_breed", length = 50)
    private String petBreed;

    /**
     * 반려견 크기 (소형, 중형, 대형) / (선택) Optional Column
     * Pet size (small, medium, large)
     */
    @Column(name = "pet_size", length = 20)
    private String petSize;

    /**
     * 반려견 무게 (kg)
     * Pet weight in kilograms
     */
    @Column(name = "pet_weight")
    @Positive
    private Double petWeight;

    /**
     * 반려견 성별
     * Pet gender
     */
    @Column(name = "pet_gender", length = 20)
    @Enumerated(EnumType.STRING)
    private Gender petGender;

    /**
     * 반려견 나이 (개월 수)
     * Pet age in months
     */
    @Column(name = "pet_age")
    @Positive
    private Integer petAge;

    /**
     * 생애 단계
     * Life stage
     */
    @Column(name = "life_stage", length = 30)
    @Enumerated(EnumType.STRING)
    private PetLifeStage lifeStage;

    /**
     * 활동 수준
     * Activity level
     */
    @Column(name = "activity_level", length = 30)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PetActivityLevel activityLevel = PetActivityLevel.NORMAL;

    /**
     * 하루 권장 칼로리 (kcal)
     * Daily recommended calories
     */
    @Column(name = "daily_calories")
    private Double dailyCalories;

    /**
     * 하루 권장 조단백질량(g)
     * Daily recommended Crude Protein(g)
     */
    @Column(name = "daily_CrudeProtein")
    private Double dailyCrudeProtein;

    /**
     * 하루 권장 조지방량(g)
     * Daily recommended Crude Fat(g)
     */
    @Column(name = "daily_CrudeFat")
    private Double dailyCrudeFat;

    /**
     * 하루 권장 조섬유량(mg)
     * Daily recommended Crude Fiber(mg)
     */
    @Column(name = "daily_CrudeFiber")
    private Double dailyCrudeFiber;

    /**
     * 하루 권장 칼슘량(g)
     * Daily recommended daily_Calcium(g)
     */
    @Column(name = "daily_Calcium")
    private Double dailyCalcium;

    /**
     * 소유자 (사용자)
     * Owner (User)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

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
}
