package com.nutricheck.backend.service;

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
            .petAge(2)
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
                .name("PetOwnerUser1")
                .password("password")
                .email("pet_test@example.com")
                .age(30)
                .gender("M")
                .height(180.0)
                .weight(80.0)
                .goalType("MAINTAIN")
                .build();

        authService.register(userRegisterRequest);
        testUser = userRepository.findByUsername(userRegisterRequest.getUsername()).get();
    }

    @Test
    void registerPet() {
        Assertions.assertEquals(0, petRepository.findAll().size());


        PetInfoResponse createdPet = petService.registerPet(testUser, petRegisterRequest);

        Assertions.assertNotNull(createdPet);
        Assertions.assertEquals(1, petRepository.findAll().size());
        Assertions.assertEquals(petRegisterRequest.getPetName(), createdPet.getPetName());
        Assertions.assertEquals(petRegisterRequest.getPetName(), createdPet.getPetName());
//        Assertions.assertEquals(testUser.getUsername(), createdPet.getGetOwnerUsername());
    }

    @Test
    void registerManyPet() {
        Assertions.assertEquals(0, petRepository.findAll().size());


        for (int i = 0; i < 10; i++) {
            PetRegisterRequest newPetRegisterRequest = PetRegisterRequest.builder()
                    .petName("DogName" + i)
                    .petBreed("Beagle")
                    .petAge(2)
                    .build();
            petService.registerPet(testUser, newPetRegisterRequest);
        }

        Assertions.assertEquals(10, petRepository.findAll().size());
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
                    .petAge(2)
                    .build();
            petService.registerPet(testUser, newPetRegisterRequest);
        }
        List<PetInfoResponse> pets = petService.getPetsByUser(testUser);
        Assertions.assertEquals(10, pets.size());
    }

    @Test
    void getPetsByUserWrongUser() {
        RegisterRequest userRegisterRequest2 = RegisterRequest.builder()
                .username("petOwnerTestUser2")
                .name("PetOwnerUser2")
                .password("password2")
                .email("pet_test2@example.com")
                .age(30)
                .gender("M")
                .height(180.0)
                .weight(80.0)
                .goalType("MAINTAIN")
                .build();
        authService.register(userRegisterRequest2);
        User testUser2 = userRepository.findByUsername(userRegisterRequest2.getUsername()).get();

        for (int i = 0; i < 10; i++) {
            PetRegisterRequest newPetRegisterRequest = PetRegisterRequest.builder()
                    .petName("DogName" + i)
                    .petBreed("Beagle")
                    .petAge(2)
                    .build();
            // register for user 1
            petService.registerPet(testUser, newPetRegisterRequest);
        }
        // request for user 2
        List<PetInfoResponse> pets = petService.getPetsByUser(testUser2);
        Assertions.assertEquals(0, pets.size());
    }

    @Test
    void updatePet() {
        PetUpdateRequest petUpdateRequest = PetUpdateRequest.builder()
                .petName("NEW" + petRegisterRequest.getPetName())
                .petBreed("NEW" + petRegisterRequest.getPetName())
                .petAge(petRegisterRequest.getPetAge() + 5)
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