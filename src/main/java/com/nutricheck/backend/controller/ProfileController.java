package com.nutricheck.backend.controller;


import com.nutricheck.backend.dto.ProfileResponseDto;
import com.nutricheck.backend.dto.UpdateUserRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    @GetMapping()
    public ResponseEntity<ProfileResponseDto> getCurrentUserProfile( ) {
        // todo: get user from Security Context
        // todo: call service (current_user)
        ProfileResponseDto userResponseDto = new ProfileResponseDto();
        return ResponseEntity.ok(userResponseDto);
    }

    @PutMapping()
    public ResponseEntity<ProfileResponseDto> updateCurrentUserProfile(@RequestBody @Valid UpdateUserRequestDto request) {
        // todo: update profile info
        // todo: send updated profile
        ProfileResponseDto updatedUser = new ProfileResponseDto() ;
        return ResponseEntity.ok(updatedUser);
    }
}
