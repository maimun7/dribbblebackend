package com.example.dribbble.service;

import com.example.dribbble.model.OtpRecord;
import com.example.dribbble.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 * OTP Service:
 * - generateAndSend(): creates 6-digit OTP, saves to DB, emails it
 * - verifyOtp(): checks if code matches, not expired, not over attempts
 * - Scheduled cleanup of expired OTPs every 10 minutes
 */
@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final JavaMailSender mailSender;

    @Value("${otp.expiry.minutes:10}")
    private int otpExpiryMinutes;

    private static final int MAX_ATTEMPTS = 5;
    private static final SecureRandom RANDOM = new SecureRandom();

    /* ── Generate OTP, save to DB, send email ── */
    public void generateAndSend(String email, String purpose) {
        // Delete any previous OTPs for this email
        otpRepository.deleteByEmail(email);

        // Generate 6-digit OTP
        String code = String.format("%06d", RANDOM.nextInt(1_000_000));

        // Save to DB
        OtpRecord otp = OtpRecord.builder()
                .email(email)
                .code(code)
                .purpose(purpose)
                .attempts(0)
                .expiresAt(LocalDateTime.now().plusMinutes(otpExpiryMinutes))
                .build();
        otpRepository.save(otp);

        // Send email
        sendOtpEmail(email, code);
    }

    /* ── Verify OTP code ── */
    public boolean verifyOtp(String email, String code) {
        OtpRecord otp = otpRepository.findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new RuntimeException("No OTP found. Please request a new one."));

        if (otp.isExpired()) {
            otpRepository.delete(otp);
            throw new RuntimeException("OTP has expired. Please request a new one.");
        }

        if (otp.getAttempts() >= MAX_ATTEMPTS) {
            otpRepository.delete(otp);
            throw new RuntimeException("Too many wrong attempts. Please request a new OTP.");
        }

        if (!otp.getCode().equals(code)) {
            // Increment attempts
            otp.setAttempts(otp.getAttempts() + 1);
            otpRepository.save(otp);
            int remaining = MAX_ATTEMPTS - otp.getAttempts();
            throw new RuntimeException("Incorrect OTP. " + remaining + " attempt(s) remaining.");
        }

        // ✓ Correct — delete OTP (single-use)
        otpRepository.deleteByEmail(email);
        return true;
    }

    /* ── Cleanup expired OTPs every 10 minutes ── */
    @Scheduled(fixedDelay = 600_000)
    public void cleanupExpired() {
        otpRepository.deleteAllExpired(LocalDateTime.now());
    }

    /* ── Send email ── */
    private void sendOtpEmail(String email, String code) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setSubject("Your Dribbble login code: " + code);
            msg.setText(
                    "Hi,\n\n" +
                            "Your Dribbble verification code is:\n\n" +
                            "  " + code + "\n\n" +
                            "This code expires in " + otpExpiryMinutes + " minutes.\n\n" +
                            "If you didn't request this, please ignore this email.\n\n" +
                            "— Dribbble Team"
            );
            mailSender.send(msg);
        } catch (Exception e) {
            // Log but don't throw — OTP is still saved in DB
            System.err.println("⚠️  Email sending failed for " + email + ": " + e.getMessage());
            System.out.println("🔐 OTP for " + email + " (dev mode): " + code);
        }
    }
}