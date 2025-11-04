package com.nutricheck.backend.service;

import com.nutricheck.backend.dto.AuthResponseDto;
import com.nutricheck.backend.dto.LoginRequestDto;
import com.nutricheck.backend.dto.RegisterRequestDto;

public interface AuthService {

    AuthResponseDto register(RegisterRequestDto request);

    AuthResponseDto login(LoginRequestDto request);
}
