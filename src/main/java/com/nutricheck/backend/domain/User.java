package com.nutricheck.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 사용자 정보 엔티티
 * User information entity
 */
@Entity
@Table(name = "user_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    /**
     * 사용자 이름 (로그인 ID)
     * Username for login
     */
    @Column(nullable = false, unique = true, length = 30)
    private String username;

    /**
     * 사용자 실명
     * User's real name
     */
    @Column(nullable = false, length = 50)
    private String name;

     /**
     * 비밀번호 (암호화 저장 권장)
     * Password (should be encrypted)
     */
    @Column(nullable = false)
    private String password;

    /**
     * 이메일
     * Email address
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * 나이
     * Age
     */
    private Integer age;

    /**
     * 성별 (M: 남성, F: 여성)
     * Gender (M: Male, F: Female)
     */
    @Column(length = 1)
    private String gender;

    /**
     * 키 (cm)
     * Height in centimeters
     */
    private Double height;

    /**
     * 현재 체중 (kg)
     * Current weight in kilograms
     */
    private Double weight;

    /**
     * 목표 체중 (kg)
     * Goal weight in kilograms
     */
    @Column(name = "goal_weight")
    private Double goalWeight;

    /**
     * 목표 유형 (DIET: 다이어트, GAIN: 증량, MAINTAIN: 유지)
     * Goal type (DIET, GAIN, MAINTAIN)
     */
    @Column(name = "goal_type", length = 20)
    @Enumerated(EnumType.STRING)
    private GoalType goalType;

    /**
     * 활동 수준
     * Activity level
     */
    @Column(name = "activity_level", length = 20)
    private String activityLevel;

    /**
     * 기초대사량 (Basal Metabolic Rate)
     * BMR in kcal
     */
    private Double bmr;

    /**
     * 활동대사량 (Total Daily Energy Expenditure)
     * TDEE in kcal
     */
    private Double tdee;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    /**
     * 목표 유형 열거형
     * Goal type enumeration
     */
    public enum GoalType {
        DIET,      // 다이어트 / Weight loss
        GAIN,      // 증량 / Weight gain
        MAINTAIN   // 유지 / Maintain weight
    }
}
