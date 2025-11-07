package com.nutricheck.backend.service;

import com.nutricheck.backend.dto.AuthResponse;
import com.nutricheck.backend.dto.LoginRequest;
import com.nutricheck.backend.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
