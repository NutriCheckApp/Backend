package com.nutricheck.backend.controller;


import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.ProfileResponse;
import com.nutricheck.backend.dto.UpdateUserRequest;
import com.nutricheck.backend.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping()
    public ResponseEntity<ProfileResponse> getCurrentUserProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(profileService.getProfile(user));
    }

    @PutMapping()
    public ResponseEntity<ProfileResponse> updateCurrentUserProfile(Authentication authentication,
                                                                    @RequestBody @Valid UpdateUserRequest request) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(profileService.updateProfile(user, request));
    }
}
