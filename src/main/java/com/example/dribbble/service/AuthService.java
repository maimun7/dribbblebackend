//package com.example.dribbble.service;
//
//import com.example.dribbble.dto.*;
//import com.example.dribbble.model.User;
//import com.example.dribbble.repository.UserRepository;
//import com.example.dribbble.util.JwtUtil;
//import com.example.dribbble.dto.RegisterRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.*;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
///**
// * Handles user registration and login.
// * - register(): saves new user with hashed password, returns JWT
// * - login(): verifies credentials via AuthenticationManager, returns JWT
// */
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtUtil jwtUtil;
//    private final AuthenticationManager authenticationManager;
//
//    public com.example.dribbble.dto.AuthResponse register(RegisterRequest req) {
//        if (userRepository.existsByEmail(req.getEmail())) {
//            throw new RuntimeException("Email is already registered.");
//        }
//
//        User user = User.builder()
//                .name(req.getName())
//                .email(req.getEmail())
//                .password(passwordEncoder.encode(req.getPassword()))
//                .role(User.Role.USER)
//                .build();
//
//        userRepository.save(user);
//
//        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
//        return buildResponse(token, user);
//    }
//
//    public com.example.dribbble.dto.AuthResponse login(com.example.dribbble.dto.LoginRequest req) {
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
//            );
//        } catch (AuthenticationException e) {
//            throw new RuntimeException("Invalid email or password.");
//        }
//
//        User user = userRepository.findByEmail(req.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found."));
//
//        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
//        return buildResponse(token, user);
//    }
//
//    private com.example.dribbble.dto.AuthResponse buildResponse(String token, User user) {
//        return com.example.dribbble.dto.AuthResponse.builder()
//                .token(token)
//                .user(com.example.dribbble.dto.AuthResponse.UserDto.builder()
//                        .id(user.getId())
//                        .name(user.getName())
//                        .email(user.getEmail())
//                        .role(user.getRole().name())
//                        .avatarUrl(user.getAvatarUrl())
//                        .build())
//                .build();
//    }
//}



package com.example.dribbble.service;

import com.example.dribbble.dto.*;
import com.example.dribbble.model.User;
import com.example.dribbble.repository.UserRepository;
import com.example.dribbble.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    /**
     * REGISTER: validate → encode password → save user → return JWT + user info
     */
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email is already registered.");
        }

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(User.Role.USER)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return toResponse(token, user);
    }

    /**
     * LOGIN: Spring Security authenticates email+password → generate JWT
     */
    public AuthResponse login(LoginRequest req) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );
        } catch (AuthenticationException ex) {
            throw new RuntimeException("Invalid email or password.");
        }

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return toResponse(token, user);
    }

    private AuthResponse toResponse(String token, User user) {
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
}