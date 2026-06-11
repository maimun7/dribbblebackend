package com.example.dribbble.controller;

import com.example.dribbble.model.User;
import com.example.dribbble.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public User getMe(Authentication auth) {
        return userService.getCurrentUser(auth.getName());
    }

    @PutMapping("/me")
    public User updateProfile(
            Authentication auth,
            @RequestBody User updated
    ) {
        return userService.updateProfile(auth.getName(), updated);
    }

    @PostMapping("/avatar")
    public User uploadAvatar(
            Authentication auth,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        return userService.uploadAvatar(auth.getName(), file);
    }
}