package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.Gender;
import com.nutricheck.backend.domain.Pet;
import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.PetInfoResponse;
import com.nutricheck.backend.dto.PetRegisterRequest;
import com.nutricheck.backend.dto.PetUpdateRequest;
import com.nutricheck.backend.dto.RegisterRequest;
import com.nutricheck.backend.repository.PetRepository;
import com.nutricheck.backend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@SpringBootTest()
@Transactional
class PetServiceTest {

    private final PetRegisterRequest petRegisterRequest = PetRegisterRequest.builder()
            .petName("DogName")
            .petBreed("Beagle")
            .petAge(24)
            .petWeight(10.0)
            .petGender(Gender.NEUTERED_MALE)
            .build();

    @Autowired
    private PetService petService;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        RegisterRequest userRegisterRequest = RegisterRequest.builder()
                .username("petOwnerTestUser")
                .password("password")
                .email("pet_test@example.com")
                .petWeight(10.0)
                .pet_age(24)
                .gender("NEUTERED_MALE")
                .activity_level("NORMAL")
                .build();

        authService.register(userRegisterRequest);
        testUser = userRepository.findByUsername(userRegisterRequest.getUsername()).get();
    }

    @Test
    void registerPet() {
        // 회원가입 시 자동으로 Pet 1개가 생성됨 ("My Pet")
        Assertions.assertEquals(1, petRepository.findAll().size());

        PetInfoResponse createdPet = petService.registerPet(testUser, petRegisterRequest);

        Assertions.assertNotNull(createdPet);
        Assertions.assertEquals(2, petRepository.findAll().size());
        Assertions.assertEquals(petRegisterRequest.getPetName(), createdPet.getPetName());
        Assertions.assertEquals(petRegisterRequest.getPetName(), createdPet.getPetName());
//        Assertions.assertEquals(testUser.getUsername(), createdPet.getGetOwnerUsername());
    }

    @Test
    void registerManyPet() {
        // 회원가입 시 자동으로 Pet 1개가 생성됨 ("My Pet")
        Assertions.assertEquals(1, petRepository.findAll().size());

        for (int i = 0; i < 10; i++) {
            PetRegisterRequest newPetRegisterRequest = PetRegisterRequest.builder()
                    .petName("DogName" + i)
                    .petBreed("Beagle")
                    .petAge(24)
                    .petWeight(10.0)
                    .petGender(Gender.NEUTERED_MALE)
                    .build();
            petService.registerPet(testUser, newPetRegisterRequest);
        }

        Assertions.assertEquals(11, petRepository.findAll().size());
    }


    @Test
    void getPetById() {
        PetInfoResponse registeredPet = petService.registerPet(testUser, petRegisterRequest);
        PetInfoResponse petById = petService.getPetById(testUser, registeredPet.getPetId());

        Assertions.assertNotNull(petById);
        Assertions.assertEquals(registeredPet, petById);
    }

    @Test
    void getPetByWrongId() {
        Assertions.assertThrows(Exception.class, () -> petService.getPetById(testUser, 9999L));
    }

    @Test
    void getPetsByUser() {
        for (int i = 0; i < 10; i++) {
            PetRegisterRequest newPetRegisterRequest = PetRegisterRequest.builder()
                    .petName("DogName" + i)
                    .petBreed("Beagle")
                    .petAge(24)
                    .petWeight(10.0)
                    .petGender(Gender.NEUTERED_MALE)
                    .build();
            petService.registerPet(testUser, newPetRegisterRequest);
        }
        List<PetInfoResponse> pets = petService.getPetsByUser(testUser);
        // 회원가입 시 생성된 Pet 1개 + 추가로 등록한 Pet 10개 = 11개
        Assertions.assertEquals(11, pets.size());
    }

    @Test
    void getPetsByUserWrongUser() {
        RegisterRequest userRegisterRequest2 = RegisterRequest.builder()
                .username("petOwnerTestUser2")
                .password("password2")
                .email("pet_test2@example.com")
                .petWeight(8.0)
                .pet_age(36)
                .gender("FEMALE")
                .activity_level("ACTIVE")
                .build();
        authService.register(userRegisterRequest2);
        User testUser2 = userRepository.findByUsername(userRegisterRequest2.getUsername()).get();

        for (int i = 0; i < 10; i++) {
            PetRegisterRequest newPetRegisterRequest = PetRegisterRequest.builder()
                    .petName("DogName" + i)
                    .petBreed("Beagle")
                    .petAge(24)
                    .petWeight(10.0)
                    .petGender(Gender.NEUTERED_MALE)
                    .build();
            // register for user 1
            petService.registerPet(testUser, newPetRegisterRequest);
        }
        // request for user 2
        List<PetInfoResponse> pets = petService.getPetsByUser(testUser2);
        // testUser2도 회원가입 시 Pet 1개가 자동 생성됨 ("My Pet")
        Assertions.assertEquals(1, pets.size());
    }

    @Test
    void updatePet() {
        PetUpdateRequest petUpdateRequest = PetUpdateRequest.builder()
                .petName("NEW" + petRegisterRequest.getPetName())
                .petBreed("NEW" + petRegisterRequest.getPetBreed())
                .petAge(petRegisterRequest.getPetAge() + 5)
                .petWeight(12.0)
                .petGender(Gender.NEUTERED_MALE)
                .build();

        PetInfoResponse registeredPet = petService.registerPet(testUser, petRegisterRequest);
        PetInfoResponse petInfoResponse = petService.updatePet(testUser, registeredPet.getPetId(), petUpdateRequest);

        Assertions.assertNotNull(petInfoResponse);
        Assertions.assertNotEquals(registeredPet, petInfoResponse);
        Assertions.assertEquals(petUpdateRequest.getPetName(), petInfoResponse.getPetName());
        Assertions.assertEquals(petUpdateRequest.getPetAge(), petInfoResponse.getPetAge());

    }

    @Test
    void deletePet() {
        PetInfoResponse registeredPet = petService.registerPet(testUser, petRegisterRequest);

        petService.deletePet(testUser, registeredPet.getPetId());

        Optional<Pet> deletedPet = petRepository.findById(registeredPet.getPetId());
        Assertions.assertTrue(deletedPet.isEmpty());
    }
}