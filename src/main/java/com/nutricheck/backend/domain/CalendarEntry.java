package com.nutricheck.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "calendar_entry",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_calendar_entry_user_date",
                        columnNames = {"user_id", "entry_date"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "user")
public class CalendarEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_entry_id")
    private Long id;

    /**
     * 어떤 유저의 기록인지
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 캘린더 날짜 (하루 단위)
     */
    @Column(name = "entry_date", nullable = false)
    private LocalDate date;

    /**
     * 메모 내용
     */
    @Column(columnDefinition = "TEXT")
    private String memo;

    /**
     * 이미지를 저장한 경로/URL (파일 업로드 구현 후 사용)
     */
    @Column(name = "image_url", length = 500)
    private String imageUrl;


    @Column(name = "image_name", length = 500)
    private String imageName;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
