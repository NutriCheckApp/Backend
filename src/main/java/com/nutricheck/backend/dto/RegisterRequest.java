package com.nutricheck.backend.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * 회원가입 요청 DTO
 * User registration request DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    /**
     * 사용자 이름 (로그인 ID)
     * Username for login
     */
    @NotBlank(message = "Username required")
    private String username;

    /**
     * 비밀번호
     * Password
     */
    @NotBlank(message = "Password required")
    private String password;

    /**
     * 이메일
     * Email address
     */
    @NotBlank(message = "Email required")
    @Email(message = "Invalid email format")
    private String email;

    @JsonProperty("name")
    private String name;


    @JsonProperty("pet_name")
    private String petName;

    /**
     * 강아지 체중 (kg)
     * Pet Weight (kg)
     */
    @NotNull(message = "Pet Weight(kg) required")
    @Positive(message = "Pet Weight(kg) must be a positive number")
    @JsonProperty("pet_weight")
    private Double petWeight;


    /**
     * 강아지 나이 (months)
     * Pet Age (months)
     * Default: 12~84 months(ADULT)
     */
    @NotNull
    @Positive(message = "Pet Age(months) must be a positive number")
    private Integer pet_age;

    /**
     * 강아지 성별(중성화 여부 포함) (Male: 수컷, Female: 암컷, Neutered male: 중성화 수컷, Spayed female: 중성화 암컷)
     * Pet Gender(Include neutered)
     * Default: Neutered male(Spayed female)
     */
    @NotBlank(message = "Pet Gender required." +
                        " Available options: Male: 수컷, Female: 암컷, Neutered male: 중성화 수컷, Spayed female: 중성화 암컷")
    private String gender;

    /**
     * 강아지 활동 수준 ()
     * Pet activity_level(INACTIVE: 비활동적, NORMAL: 보통, ACTIVE: 활동적, VERY_ACTIVE: 매우 활동적
     * Default: NORMAL
     */
//    @NotBlank(message = "Pet Gender required")
    private String activity_level;


    /*
      만약, 사용자가 강아지의 나이와 성별을 모두 입력하지 않았을 경우
      isNeutered 함수는 false로 처리되어 "미중성화"처리되고,
      생애단계는 ADULT_INTACT(미중성화 성견)으로 처리됨.
     */


}
