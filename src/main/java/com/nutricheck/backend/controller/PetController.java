package com.nutricheck.backend.controller;

import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.*;
import com.nutricheck.backend.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    public ResponseEntity<PetInfoResponse> registerPet(Authentication authentication,
                                                       @RequestBody @Valid PetRegisterRequest request) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(petService.registerPet(user, request));
    }

    @GetMapping
    public ResponseEntity<List<PetInfoResponse>> getAllPetsByUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(petService.getPetsByUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetInfoResponse> getPetById(Authentication authentication,
                                                      @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();
        PetInfoResponse pet = petService.getPetById(user, id);
        return ResponseEntity.ok(pet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetInfoResponse> updatePet(Authentication authentication,
                                                     @PathVariable Long id,
                                                     @RequestBody PetUpdateRequest updateRequest) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(petService.updatePet(user, id, updateRequest));
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(Authentication authentication,
                                          @PathVariable Long petId) {
        User user = (User) authentication.getPrincipal();
        petService.deletePet(user, petId);
        return ResponseEntity.ok().build();
    }


}
