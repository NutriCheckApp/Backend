package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.*;
import com.nutricheck.backend.dto.PetInfoResponse;
import com.nutricheck.backend.dto.PetRegisterRequest;
import com.nutricheck.backend.dto.PetUpdateRequest;
import com.nutricheck.backend.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 반려견 서비스 구현체
 * Pet service implementation
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    private final CalculateCalorieService calculateCalorieService;

    private final ModelMapper modelMapper = new ModelMapper();




    /**
     * 반려견 등록 (칼로리 자동 계산)
     * Register a new pet with automatic calorie calculation
     */
    @Override
    public PetInfoResponse registerPet(User user, PetRegisterRequest request) {
        Pet petEntity = modelMapper.map(request, Pet.class);
        petEntity.setUser(user);

        // 생애 단계가 없으면 자동 추정 / Estimate life stage if not provided
        if (petEntity.getLifeStage() == null) {
            petEntity.setLifeStage(calculateCalorieService.estimateLifeStage(petEntity));
        }

        // 활동 수준 기본값 설정 / Set default activity level
        if (petEntity.getActivityLevel() == null) {
            petEntity.setActivityLevel(PetActivityLevel.NORMAL);
        }

        // 하루 권장 칼로리 계산 / Calculate daily recommended calories
        double dailyCalories = calculateCalorieService.calculateDailyCalories(petEntity);
        petEntity.setDailyCalories(dailyCalories);

        Pet created = petRepository.save(petEntity);
        return modelMapper.map(created, PetInfoResponse.class);
    }

    /**
     * ID로 반려견 조회
     * Get pet by ID
     */
    @Override
    public PetInfoResponse getPetById(User user, Long petId) {
        Pet pet = getPet(user, petId);
        return modelMapper.map(pet, PetInfoResponse.class);
    }

    /**
     * 사용자의 모든 반려견 조회
     * Get all pets by user
     */
    @Override
    public List<PetInfoResponse> getPetsByUser(User user) {
        List<Pet> pets = petRepository.findByUser(user);
        return pets.stream()
                .map(pet -> modelMapper.map(pet, PetInfoResponse.class))
                .toList();
    }

    /**
     * 반려견 정보 수정 (칼로리 자동 재계산)
     * Update pet information with automatic calorie recalculation
     */
    @Override
    public PetInfoResponse updatePet(User user, Long petId, PetUpdateRequest updateRequest) {
        Pet pet = getPet(user, petId);
        modelMapper.map(updateRequest, pet);

        // 생애 단계가 없으면 자동 추정 / Estimate life stage if not provided
        if (pet.getLifeStage() == null) {
            pet.setLifeStage(calculateCalorieService.estimateLifeStage(pet));
        }

        // 활동 수준 기본값 설정 / Set default activity level
        if (pet.getActivityLevel() == null) {
            pet.setActivityLevel(PetActivityLevel.NORMAL);
        }

        // 하루 권장 칼로리 재계산 / Recalculate daily recommended calories
        double dailyCalories = calculateCalorieService.calculateDailyCalories(pet);
        pet.setDailyCalories(dailyCalories);

        petRepository.save(pet);
        return modelMapper.map(pet, PetInfoResponse.class);
    }

    /**
     * 반려견 삭제
     * Delete pet
     */
    @Override
    public void deletePet(User user, Long petId) {
        // 존재 여부 및 소유권 확인 / Check existence and ownership
        Pet pet = getPet(user, petId);
        petRepository.deleteById(petId);
    }

    /**
     * 사용자 소유 반려견 조회 (내부용)
     * Get pet owned by user (internal use)
     */
    private Pet getPet(User user, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet with id " + petId + " not found!"));

        if (!pet.getUser().equals(user)) {
            throw new EntityNotFoundException("Pet with id " + petId + " does not belong to the user!");
        }
        return pet;
    }
}