package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.ProfileResponse;
import com.nutricheck.backend.dto.UpdateUserRequest;
import com.nutricheck.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 프로필 서비스 구현체
 * User profile service implementation
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;

    @Override
    public ProfileResponse getProfile(User user) {
        return ProfileResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    @Override
    @Transactional
    public ProfileResponse updateProfile(User user, UpdateUserRequest updateRequest) {
        // 이메일 중복 체크 (본인 이메일 제외)
        if (!updateRequest.getEmail().equals(user.getEmail())
                && userRepository.findByEmail(updateRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email " + updateRequest.getEmail() + " already exists");
        }

        // 사용자 정보 업데이트
        user.setName(updateRequest.getName());
        user.setEmail(updateRequest.getEmail());

        User savedUser = userRepository.save(user);

        return ProfileResponse.builder()
                .username(savedUser.getUsername())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .build();
    }
}
