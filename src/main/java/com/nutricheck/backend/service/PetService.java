package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.Pet;
import com.nutricheck.backend.domain.PetActivityLevel;
import com.nutricheck.backend.domain.PetLifeStage;
import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.*;

import java.util.List;

/**
 * 반려견 서비스 인터페이스
 * Pet service interface
 */
public interface PetService {

    /**
     * 반려견 등록
     * Register a new pet
     */
    PetInfoResponse registerPet(User user, PetRegisterRequest request);

    /**
     * ID로 반려견 조회
     * Get pet by ID
     */
    PetInfoResponse getPetById(User owner, Long petId);

    /**
     * 사용자의 모든 반려견 조회
     * Get all pets by user
     */
    List<PetInfoResponse> getPetsByUser(User user);

    /**
     * 반려견 정보 수정
     * Update pet information
     */
    PetInfoResponse updatePet(User user, Long petId, PetUpdateRequest updateRequest);

    /**
     * 반려견 삭제
     * Delete pet
     */
    void deletePet(User user, Long petId);

}

