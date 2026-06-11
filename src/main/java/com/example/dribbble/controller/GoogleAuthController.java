package com.example.dribbble.controller;

import com.example.dribbble.dto.AuthResponse;
import com.example.dribbble.dto.GoogleOtpRequest;
import com.example.dribbble.service.GoogleAuthService;
import com.example.dribbble.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * ════════════════════════════════════════════════
 * Google OAuth + OTP Auth Endpoints
 * ════════════════════════════════════════════════
 *
 * POST /api/auth/google/init
 *   Body: { "idToken": "eyJ..." }  ← from Google Sign-In
 *   Returns: { "email": "j***@gmail.com", "maskedEmail": "...", "isNewUser": true }
 *   Action: verifies token, creates user if new, sends OTP email
 *
 * POST /api/auth/google/verify
 *   Body: { "email": "john@gmail.com", "otp": "123456" }
 *   Returns: { "token": "eyJ...", "user": {...} }
 *   Action: verifies OTP, returns JWT
 *
 * POST /api/auth/google/resend
 *   Body: { "email": "john@gmail.com" }
 *   Returns: { "message": "OTP resent" }
 *   Action: generates new OTP and re-sends email
 * ════════════════════════════════════════════════
 */
@RestController
@RequestMapping("/api/auth/google")
@RequiredArgsConstructor
public class GoogleAuthController {

    private final GoogleAuthService googleAuthService;
    private final OtpService        otpService;

    /* ── Step 1: Receive Google ID token → send OTP ── */
    @PostMapping("/init")
    public ResponseEntity<?> initGoogleLogin(
            @Valid @RequestBody GoogleOtpRequest.InitRequest req) {
        try {
            GoogleOtpRequest.InitResponse response =
                    googleAuthService.initGoogleLogin(req.getIdToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    /* ── Step 2: Verify OTP → return JWT ── */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(
            @Valid @RequestBody GoogleOtpRequest.VerifyRequest req) {
        try {
            AuthResponse response =
                    googleAuthService.verifyOtpAndLogin(req.getEmail(), req.getOtp());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    /* ── Resend OTP ── */
    @PostMapping("/resend")
    public ResponseEntity<?> resendOtp(
            @Valid @RequestBody GoogleOtpRequest.EmailOtpRequest req) {
        try {
            otpService.generateAndSend(req.getEmail(), "LOGIN");
            return ResponseEntity.ok(Map.of("message", "OTP resent to " + req.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}