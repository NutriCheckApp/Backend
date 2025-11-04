package com.nutricheck.backend.service;

import com.nutricheck.backend.dto.AuthResponseDto;
import com.nutricheck.backend.dto.LoginRequestDto;
import com.nutricheck.backend.dto.RegisterRequestDto;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public AuthResponseDto register(RegisterRequestDto request) {
        // todo: implement
        return AuthResponseDto.builder().jwt("TOKEN").build();
    }

    @Override
    public AuthResponseDto login(LoginRequestDto request) {
        // todo: implement
        return AuthResponseDto.builder().jwt("TOKEN").build();
    }
}
