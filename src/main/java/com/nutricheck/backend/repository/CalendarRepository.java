package com.nutricheck.backend.repository;

import com.nutricheck.backend.domain.CalendarEntry;
import com.nutricheck.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<CalendarEntry, Long> {

    // 특정 유저 + 특정 날짜 기록 하나
    Optional<CalendarEntry> findByUserAndDate(User user, LocalDate date);

    // 특정 유저 + 날짜 범위 기록들 (예: 한 달치)
    List<CalendarEntry> findAllByUserAndDateBetween(User user, LocalDate start, LocalDate end);

    Optional<CalendarEntry> findByUserAndImageName(User user, String imageName);

}
