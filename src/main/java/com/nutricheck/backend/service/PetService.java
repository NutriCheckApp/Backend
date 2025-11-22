package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.*;

import java.util.List;

public interface PetService {

    PetInfoResponse registerPet(User user, PetRegisterRequest request);

    PetInfoResponse getPetById(User owner, Long petId);

    List<PetInfoResponse> getPetsByUser(User user);

    PetInfoResponse updatePet(User user, Long petId, PetUpdateRequest updateRequest);

    void deletePet(User user, Long petId);
}

