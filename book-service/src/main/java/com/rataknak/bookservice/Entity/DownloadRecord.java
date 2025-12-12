package com.rataknak.bookservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "download_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DownloadRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "transaction_hash")
    private String transactionHash;

    @Column(name = "blockchain_status")
    private String blockchainStatus = "PENDING";

    @Column(name = "downloaded_at")
    private LocalDateTime downloadedAt = LocalDateTime.now();
}

