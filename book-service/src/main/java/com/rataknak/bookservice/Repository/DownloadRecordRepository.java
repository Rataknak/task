package com.rataknak.bookservice.Repository;

import com.rataknak.bookservice.Entity.DownloadRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DownloadRecordRepository extends JpaRepository<DownloadRecord, Long> {

    List<DownloadRecord> findByUserId(Long userId);

    List<DownloadRecord> findByBookId(Long bookId);

    Optional<DownloadRecord> findByTransactionHash(String transactionHash);

    List<DownloadRecord> findByBlockchainStatus(String status);
}

