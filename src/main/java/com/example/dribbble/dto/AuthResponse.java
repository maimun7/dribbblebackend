package com.example.dribbble.dto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private UserDto user;

    @Data @AllArgsConstructor @NoArgsConstructor @Builder
    public static class UserDto {
        private Long id;
        private String name;
        private String email;
        private String role;
        private String avatarUrl;
    }
}