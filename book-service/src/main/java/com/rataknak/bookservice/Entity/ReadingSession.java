package com.rataknak.bookservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "reading_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "pages_read")
    private Integer pagesRead = 0;

    @Column(name = "current_page")
    private Integer currentPage = 0;

    @Column(name = "time_spent_minutes")
    private Integer timeSpentMinutes = 0;

    @Column(name = "session_start")
    private LocalDateTime sessionStart;

    @Column(name = "session_end")
    private LocalDateTime sessionEnd;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}

