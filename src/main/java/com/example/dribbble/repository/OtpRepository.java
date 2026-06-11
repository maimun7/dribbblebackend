package com.example.dribbble.repository;

import com.example.dribbble.model.OtpRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpRecord, Long> {

    // Find latest OTP for an email
    Optional<OtpRecord> findTopByEmailOrderByCreatedAtDesc(String email);

    // Find by email AND code
    Optional<OtpRecord> findByEmailAndCode(String email, String code);

    // Delete all OTPs for an email (after successful verify)
    @Modifying
    @Transactional
    void deleteByEmail(String email);

    // Cleanup expired OTPs (called by scheduled job)
    @Modifying
    @Transactional
    @Query("DELETE FROM OtpRecord o WHERE o.expiresAt < :now")
    void deleteAllExpired(@Param("now") LocalDateTime now);
}