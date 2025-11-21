package com.nutricheck.backend.service;

import com.nutricheck.backend.dto.*;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    CheckUsernameResponse checkUsername(String username);

    SendVerificationCodeResponse sendVerificationCode(SendVerificationCodeRequest request);

    VerifyCodeResponse verifyCode(VerifyCodeRequest request);

    UpdateUserMetricsResponse updateUserMetrics(UpdateUserMetricsRequest request);
}
