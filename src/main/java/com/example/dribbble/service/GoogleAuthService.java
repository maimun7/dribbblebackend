package com.example.dribbble.service;

import com.example.dribbble.dto.AuthResponse;
import com.example.dribbble.dto.GoogleOtpRequest;
import com.example.dribbble.model.User;
import com.example.dribbble.repository.UserRepository;
import com.example.dribbble.util.JwtUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Google Auth Flow:
 *
 * Step 1 — Frontend:
 *   User clicks "Continue with Google"
 *   Google Sign-In returns an idToken (JWT string)
 *   Frontend sends idToken to POST /api/auth/google/init
 *
 * Step 2 — Backend (this service):
 *   Verifies idToken with Google's servers
 *   Extracts email, name, picture from token payload
 *   Creates user in DB if first time
 *   Generates OTP and emails it
 *   Returns masked email to frontend
 *
 * Step 3 — Frontend:
 *   Shows OTP input screen
 *   User enters OTP from email
 *   Frontend sends email + OTP to POST /api/auth/google/verify
 *
 * Step 4 — Backend:
 *   Verifies OTP
 *   Returns JWT token + user data
 */
@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    private final UserRepository userRepository;
    private final OtpService     otpService;
    private final JwtUtil        jwtUtil;

    @Value("${google.client.id}")
    private String googleClientId;

    /* ── Step 2: Verify Google Token → Send OTP ── */
    public GoogleOtpRequest.InitResponse initGoogleLogin(String idToken) throws Exception {

        GoogleIdToken.Payload payload = verifyGoogleToken(idToken);

        String email     = payload.getEmail();
        String name      = (String) payload.get("name");
        String picture   = (String) payload.get("picture");
        boolean verified = payload.getEmailVerified();

        if (!verified) {
            throw new RuntimeException("Google email is not verified.");
        }

        // Find or create user
        boolean isNewUser = !userRepository.existsByEmail(email);

        if (isNewUser) {
            User newUser = User.builder()
                    .name(name != null ? name : email.split("@")[0])
                    .email(email)
                    .password("") // no password for Google users
                    .avatarUrl(picture)
                    .role(User.Role.USER)
                    .build();
            userRepository.save(newUser);
        } else {
            // Update avatar if changed
            userRepository.findByEmail(email).ifPresent(u -> {
                if (picture != null && !picture.equals(u.getAvatarUrl())) {
                    u.setAvatarUrl(picture);
                    userRepository.save(u);
                }
            });
        }

        // Generate OTP and send email
        otpService.generateAndSend(email, "LOGIN");

        return GoogleOtpRequest.InitResponse.of(email, isNewUser);
    }

    /* ── Step 4: Verify OTP → Return JWT ── */
    public AuthResponse verifyOtpAndLogin(String email, String otp) {
        // Throws if OTP wrong/expired
        otpService.verifyOtp(email, otp);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .user(AuthResponse.UserDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .avatarUrl(user.getAvatarUrl())
                        .build())
                .build();
    }

    /* ── Verify Google ID Token with Google's servers ── */
    private GoogleIdToken.Payload verifyGoogleToken(String idTokenString) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance()
        )
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken == null) {
            throw new RuntimeException("Invalid Google token. Please try again.");
        }

        return idToken.getPayload();
    }
}