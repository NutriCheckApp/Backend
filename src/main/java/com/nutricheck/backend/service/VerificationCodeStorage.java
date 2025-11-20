package com.nutricheck.backend.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 인증 코드 저장소
 * Verification code storage (in-memory)
 */
@Service
public class VerificationCodeStorage {

    private final Map<String, CodeData> storage = new ConcurrentHashMap<>();

    /**
     * 인증 코드 저장
     * @param email 이메일
     * @param code 인증 코드
     * @param expiryMinutes 만료 시간(분)
     */
    public void saveCode(String email, String code, int expiryMinutes) {
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(expiryMinutes);
        storage.put(email, new CodeData(code, expiryTime));
    }

    /**
     * 인증 코드 검증
     * @param email 이메일
     * @param code 입력된 코드
     * @return 검증 성공 여부
     */
    public boolean verifyCode(String email, String code) {
        CodeData data = storage.get(email);
        if (data == null) {
            return false;
        }

        // 만료 확인
        if (LocalDateTime.now().isAfter(data.expiryTime)) {
            storage.remove(email);
            return false;
        }

        // 코드 일치 확인
        boolean isValid = data.code.equals(code);
        if (isValid) {
            storage.remove(email); // 사용된 코드는 삭제
        }
        return isValid;
    }

    /**
     * 인증 코드 데이터
     */
    private record CodeData(String code, LocalDateTime expiryTime) {
    }
}
