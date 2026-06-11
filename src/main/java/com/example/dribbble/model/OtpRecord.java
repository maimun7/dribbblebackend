package com.example.dribbble.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Stores OTP codes temporarily.
 * Deleted after successful verification or expiry.
 *
 * Flow:
 * 1. User clicks "Continue with Google" → Google returns idToken
 * 2. Backend verifies idToken → finds/creates User → generates 6-digit OTP
 * 3. OTP is saved here and emailed to user
 * 4. User enters OTP → backend checks this table → issues JWT
 */
@Entity
@Table(name = "otp_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The email this OTP was sent to
    @Column(nullable = false, length = 150)
    private String email;

    // 6-digit OTP code
    @Column(nullable = false, length = 10)
    private String code;

    // OTP purpose: "LOGIN" or "REGISTER"
    @Column(length = 20)
    @Builder.Default
    private String purpose = "LOGIN";

    // Attempt count — block after 5 wrong attempts
    @Column
    @Builder.Default
    private Integer attempts = 0;

    // When this OTP expires
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    // When it was created
    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }
}