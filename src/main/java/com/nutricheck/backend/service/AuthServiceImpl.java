package com.nutricheck.backend.service;

import com.nutricheck.backend.dto.AuthResponse;
import com.nutricheck.backend.dto.LoginRequest;
import com.nutricheck.backend.dto.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public AuthResponse register(RegisterRequest request) {
        // todo: implement
        return AuthResponse.builder().jwt("TOKEN").build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // todo: implement
        return AuthResponse.builder().jwt("TOKEN").build();
    }
}
