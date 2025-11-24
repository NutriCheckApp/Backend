package com.nutricheck.backend.controller;


import com.nutricheck.backend.dto.*;
import com.nutricheck.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/check-username")
    public ResponseEntity<CheckUsernameResponse> checkUsername(@RequestParam String username) {
        return ResponseEntity.ok(authService.checkUsername(username));
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<SendVerificationCodeResponse> sendVerificationCode(@RequestBody @Valid SendVerificationCodeRequest request) {
        return ResponseEntity.ok(authService.sendVerificationCode(request));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<VerifyCodeResponse> verifyCode(@RequestBody @Valid VerifyCodeRequest request) {
        return ResponseEntity.ok(authService.verifyCode(request));
    }
}
