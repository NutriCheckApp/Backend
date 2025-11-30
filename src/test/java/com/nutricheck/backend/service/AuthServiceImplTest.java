package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.Gender;
import com.nutricheck.backend.domain.Pet;
import com.nutricheck.backend.domain.PetActivityLevel;
import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.RegisterRequest;
import com.nutricheck.backend.repository.PetRepository;
import com.nutricheck.backend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@Transactional
class AuthServiceImplTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Test
    void register() {
        long init_count = userRepository.count();
        long init_count_pet = petRepository.count();

        RegisterRequest userRegisterRequest = RegisterRequest.builder()
                .username("petOwnerTestUser")
                .password("password")
                .email("pet_test@example.com")
                .petWeight(10.0)
                .pet_age(24)
                .activity_level(PetActivityLevel.NORMAL.name())
                .gender(Gender.MALE.name())
                .build();

        authService.register(userRegisterRequest);
        Optional<User> byUsername = userRepository.findByUsername(userRegisterRequest.getUsername());
        Assertions.assertTrue(byUsername.isPresent());
        Assertions.assertEquals(init_count + 1, userRepository.count());
        Assertions.assertEquals(init_count_pet + 1, petRepository.count());

        Assertions.assertEquals(userRegisterRequest.getUsername(), byUsername.get().getUsername());
        Assertions.assertEquals(userRegisterRequest.getEmail(), byUsername.get().getEmail());

        List<Pet> pets = byUsername.get().getPets();
        Assertions.assertEquals(1, pets.size());
        Assertions.assertEquals(userRegisterRequest.getPetWeight(), pets.get(0).getPetWeight());
        Assertions.assertEquals(Gender.MALE, pets.get(0).getPetGender());
        Assertions.assertEquals(PetActivityLevel.NORMAL, pets.get(0).getActivityLevel());
        Assertions.assertEquals(userRegisterRequest.getPet_age(), pets.get(0).getPetAge());


    }
}