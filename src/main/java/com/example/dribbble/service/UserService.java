package com.example.dribbble.service;

import com.example.dribbble.model.User;
import com.example.dribbble.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;
import java.nio.file.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentUser(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public User updateProfile(String email, User updated) {
        User user = userRepository.findByEmail(email).orElseThrow();

        user.setName(updated.getName());
//        user.setUsername(updated.getUsername());
        user.setLocation(updated.getLocation());

        user.setBio(updated.getBio());
        user.setAvatarUrl(updated.getAvatarUrl());

        return userRepository.save(user);
    }

//    public User uploadAvatar(String email, MultipartFile file) throws Exception {
//        User user = userRepository.findByEmail(email).orElseThrow();
//
//        String uploadDir = "uploads/";
//        File dir = new File(uploadDir);
//        if (!dir.exists()) dir.mkdirs();
//
//        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
//        File dest = new File(uploadDir + filename);
//        file.transferTo(dest);
//
//        user.setAvatarUrl("/uploads/" + filename);
//
//        return userRepository.save(user);
//    }




    public User uploadAvatar(String email, MultipartFile file) throws Exception {

        User user = userRepository.findByEmail(email).orElseThrow();

        // ✅ Absolute path (safe)
        String uploadDir = System.getProperty("user.dir") + "/uploads/";

        Path uploadPath = Paths.get(uploadDir);

        // ✅ Ensure folder exists
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // ✅ Unique filename
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path filePath = uploadPath.resolve(filename);

        // ✅ Save file safely
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // ✅ Save URL in DB
        user.setAvatarUrl("/uploads/" + filename);

        return userRepository.save(user);
    }
}