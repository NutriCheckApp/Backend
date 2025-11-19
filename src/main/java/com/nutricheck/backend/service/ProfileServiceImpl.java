package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.ProfileResponse;
import com.nutricheck.backend.dto.UpdateUserRequest;
import com.nutricheck.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public ProfileResponse getProfile(User user) {
        ProfileResponse response = modelMapper.map(user, ProfileResponse.class);
        return response;
    }

    @Override
    public ProfileResponse updateProfile(User user, UpdateUserRequest updateRequest) {
        if (!updateRequest.getUsername().equals(user.getUsername())
            && userRepository.findByUsername(updateRequest.getUsername()).isPresent()) {
            throw new RuntimeException(updateRequest.getUsername() + " already exists");
        }
        modelMapper.map(updateRequest, user);
        User save = userRepository.save(user);
        ProfileResponse response = modelMapper.map(save, ProfileResponse.class);
        return response;
    }
}
