package com.nutricheck.backend.controller;


import com.nutricheck.backend.dto.ProfileResponse;
import com.nutricheck.backend.dto.UpdateUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    @GetMapping()
    public ResponseEntity<ProfileResponse> getCurrentUserProfile( ) {
        // todo: get user from Security Context
        // todo: call service (current_user)
        ProfileResponse userResponseDto = new ProfileResponse();
        return ResponseEntity.ok(userResponseDto);
    }

    @PutMapping()
    public ResponseEntity<ProfileResponse> updateCurrentUserProfile(@RequestBody @Valid UpdateUserRequest request) {
        // todo: update profile info
        // todo: send updated profile
        ProfileResponse updatedUser = new ProfileResponse() ;
        return ResponseEntity.ok(updatedUser);
    }
}
