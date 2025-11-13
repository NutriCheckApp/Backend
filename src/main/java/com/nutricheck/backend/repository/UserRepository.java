package com.nutricheck.backend.repository;

import com.nutricheck.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 사용자 리포지토리
 * User repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 사용자 이름으로 사용자 조회
     * Find user by username
     */
    Optional<User> findByUsername(String username);

    /**
     * 사용자 이름 존재 여부 확인
     * Check if username exists
     */
    boolean existsByUsername(String username);
}