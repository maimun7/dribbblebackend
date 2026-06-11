//package com.example.dribbble.service;
//
//import com.example.dribbble.model.*;
//import com.example.dribbble.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * Handles save/bookmark toggle logic:
// * - If user hasn't saved the shot → create Save record
// * - If user already saved the shot → delete Save record
// */
//@Service
//@RequiredArgsConstructor
//public class SaveService {
//
//    private final com.example.dribbble.repository.SaveRepository saveRepository;
//    private final com.example.dribbble.repository.ShotRepository shotRepository;
//    private final com.example.dribbble.repository.UserRepository userRepository;
//
//    @Transactional
//    public Map<String, Object> toggleSave(Long shotId, String userEmail) {
//        com.example.dribbble.model.User user = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        com.example.dribbble.model.Shot shot = shotRepository.findById(shotId)
//                .orElseThrow(() -> new RuntimeException("Shot not found"));
//
//        boolean alreadySaved = saveRepository.existsByUserAndShot(user, shot);
//
//        if (alreadySaved) {
//            // Unsave
//            com.example.dribbble.model.Save save = saveRepository.findByUserAndShot(user, shot)
//                    .orElseThrow(() -> new RuntimeException("Save record not found"));
//            saveRepository.delete(save);
//            return Map.of("saved", false, "message", "Shot removed from saved");
//        } else {
//            // Save
//            com.example.dribbble.model.Save save = com.example.dribbble.model.Save.builder().user(user).shot(shot).build();
//            saveRepository.save(save);
//            return Map.of("saved", true, "message", "Shot saved successfully");
//        }
//    }
//
//    public List<Long> getSavedShotIds(String userEmail) {
//        com.example.dribbble.model.User user = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        return saveRepository.findSavedShotIdsByUserId(user.getId());
//    }
//}




//package com.example.dribbble.service;
//
//import com.example.dribbble.model.*;
//import com.example.dribbble.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class SaveService {
//
//    private final SaveRepository saveRepository;
//    private final ShotRepository shotRepository;
//    private final UserRepository userRepository;
//
//    /**
//     * Toggle save: if already saved → unsave, else → save
//     */
//    @Transactional
//    public Map<String, Object> toggleSave(Long shotId, String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        Shot shot = shotRepository.findById(shotId)
//                .orElseThrow(() -> new RuntimeException("Shot not found"));
//
//        boolean alreadySaved = saveRepository.existsByUserAndShot(user, shot);
//
//        if (alreadySaved) {
//            saveRepository.findByUserAndShot(user, shot)
//                    .ifPresent(saveRepository::delete);
//        } else {
//            Save save = Save.builder().user(user).shot(shot).build();
//            saveRepository.save(save);
//        }
//
//        return Map.of(
//                "saved", !alreadySaved,
//                "shotId", shotId
//        );
//    }
//
//    /**
//     * Get all shot IDs that the logged-in user has saved
//     */
//    public List<Long> getSavedShotIds(String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        return saveRepository.findShotIdsByUserId(user.getId());
//    }
//}





package com.example.dribbble.service;

import com.example.dribbble.dto.ShotResponse;
import com.example.dribbble.model.*;
import com.example.dribbble.repository.*;
import com.example.dribbble.util.ShotMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaveService {

    private final SaveRepository   saveRepository;
    private final ShotRepository   shotRepository;
    private final UserRepository   userRepository;
    private final ShotMapper       shotMapper;

    /* Toggle save / unsave */
    @Transactional
    public Map<String, Object> toggleSave(Long shotId, String userEmail) {
        User user = findUser(userEmail);
        Shot shot = findShot(shotId);

        boolean alreadySaved = saveRepository.existsByUserAndShot(user, shot);

        if (alreadySaved) {
            Save save = saveRepository.findByUserAndShot(user, shot)
                    .orElseThrow(() -> new RuntimeException("Save record not found"));
            saveRepository.delete(save);
            return Map.of("saved", false, "message", "Shot removed from saved");
        } else {
            saveRepository.save(Save.builder().user(user).shot(shot).build());
            return Map.of("saved", true, "message", "Shot saved successfully");
        }
    }

    /* Returns only shot IDs (lightweight) */
    public List<Long> getSavedShotIds(String userEmail) {
        User user = findUser(userEmail);
        return saveRepository.findSavedShotIdsByUserId(user.getId());
    }

    /* Returns full shot details for saved shots — used by dashboard */
    public List<ShotResponse> getSavedShots(String userEmail) {
        User user = findUser(userEmail);
        return saveRepository.findByUser(user)
                .stream()
                .map(save -> shotMapper.toResponse(save.getShot()))
                .collect(Collectors.toList());
    }

    /* Check if a specific shot is saved by user */
    public boolean isSavedByUser(Long shotId, String userEmail) {
        User user = findUser(userEmail);
        Shot shot = findShot(shotId);
        return saveRepository.existsByUserAndShot(user, shot);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    private Shot findShot(Long id) {
        return shotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shot not found: " + id));
    }
}