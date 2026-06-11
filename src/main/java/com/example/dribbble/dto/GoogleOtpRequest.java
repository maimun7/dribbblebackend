package com.example.dribbble.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

// ─── Request: Google ID Token from frontend ───────────────────────────────
// Frontend sends this after user clicks "Continue with Google"
public class GoogleOtpRequest {
    @Data
    public static class InitRequest {
        @NotBlank(message = "Google ID token is required")
        private String idToken; // From Google Sign-In
    }

    // ─── After Google verify: send OTP to email ───────────────────────────
    @Data
    public static class InitResponse {
        private String email;           // Masked email shown to user (e.g. j***@gmail.com)
        private String maskedEmail;
        private String message;
        private boolean isNewUser;      // true if registering for first time

        public static InitResponse of(String email, boolean isNewUser) {
            InitResponse r = new InitResponse();
            r.email       = email;
            r.maskedEmail = maskEmail(email);
            r.isNewUser   = isNewUser;
            r.message     = "OTP sent to " + maskEmail(email);
            return r;
        }

        private static String maskEmail(String email) {
            if (email == null || !email.contains("@")) return email;
            String[] parts = email.split("@");
            String local  = parts[0];
            String domain = parts[1];
            if (local.length() <= 2) return local.charAt(0) + "***@" + domain;
            return local.charAt(0) + "***" + local.charAt(local.length() - 1) + "@" + domain;
        }
    }

    // ─── Request: user submits OTP code ──────────────────────────────────
    @Data
    public static class VerifyRequest {
        @NotBlank(message = "Email is required")
        @Email
        private String email;

        @NotBlank(message = "OTP is required")
        @Size(min = 6, max = 6, message = "OTP must be 6 digits")
        private String otp;
    }

    // ─── Request: normal email/password login sends OTP ──────────────────
    @Data
    public static class EmailOtpRequest {
        @NotBlank @Email
        private String email;
    }
}