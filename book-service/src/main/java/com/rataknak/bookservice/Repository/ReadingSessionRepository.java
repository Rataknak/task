package com.rataknak.bookservice.Repository;

import com.rataknak.bookservice.Entity.ReadingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadingSessionRepository extends JpaRepository<ReadingSession, Long> {

    List<ReadingSession> findByUserId(Long userId);

    List<ReadingSession> findByUserIdAndBookId(Long userId, Long bookId);

    @Query("SELECT rs FROM ReadingSession rs WHERE rs.userId = :userId ORDER BY rs.createdAt DESC")
    List<ReadingSession> findRecentByUserId(Long userId);

    @Query("SELECT SUM(rs.pagesRead) FROM ReadingSession rs WHERE rs.userId = :userId")
    Integer getTotalPagesReadByUser(Long userId);

    @Query("SELECT SUM(rs.timeSpentMinutes) FROM ReadingSession rs WHERE rs.userId = :userId")
    Integer getTotalTimeSpentByUser(Long userId);
}

