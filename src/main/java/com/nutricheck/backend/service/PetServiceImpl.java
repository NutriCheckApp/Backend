package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.Pet;
import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.PetInfoResponse;
import com.nutricheck.backend.dto.PetRegisterRequest;
import com.nutricheck.backend.dto.PetUpdateRequest;
import com.nutricheck.backend.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    private final ModelMapper modelMapper = new ModelMapper();


    @Override
    public PetInfoResponse registerPet(User user, PetRegisterRequest request) {
        Pet petEntity = modelMapper.map(request, Pet.class);
        petEntity.setUser(user);
        Pet created = petRepository.save(petEntity);
        return modelMapper.map(created, PetInfoResponse.class);
    }

    @Override
    public PetInfoResponse getPetById(User user, Long petId) {
        Pet pet = getPet(user, petId);

        return modelMapper.map(pet, PetInfoResponse.class);
    }


    @Override
    public List<PetInfoResponse> getPetsByUser(User user) {

        List<Pet> pets = petRepository.findByUser(user);

        return pets.stream()
                .map(pet -> modelMapper.map(pet, PetInfoResponse.class))
                .toList();
    }

    @Override
    public PetInfoResponse updatePet(User user, Long petId, PetUpdateRequest updateRequest) {
        Pet pet = getPet(user, petId);
        modelMapper.map(updateRequest, pet);
        petRepository.save(pet);
        return modelMapper.map(pet, PetInfoResponse.class);

    }

    @Override
    public void deletePet(User user, Long petId) {
        // check existence and ownership
        Pet pet = getPet(user, petId);

        petRepository.deleteById(petId);
    }

    private Pet getPet(User user, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet with id " + petId + " not found!"));

        if (!pet.getUser().equals(user)) {
           throw new EntityNotFoundException("Pet with id " + petId + " does not belong to the user!");
        }
        return pet;
    }
}
