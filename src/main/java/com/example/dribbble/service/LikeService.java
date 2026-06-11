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
// * Handles like toggle logic:
// * - If user hasn't liked the shot → create Like record, increment shot.likeCount
// * - If user already liked the shot → delete Like record, decrement shot.likeCount
// */
//@Service
//@RequiredArgsConstructor
//public class LikeService {
//
//    private final com.example.dribbble.repository.LikeRepository likeRepository;
//    private final com.example.dribbble.repository.ShotRepository shotRepository;
//    private final com.example.dribbble.repository.UserRepository userRepository;
//
//    @Transactional
//    public Map<String, Object> toggleLike(Long shotId, String userEmail) {
//        com.example.dribbble.model.User user = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        com.example.dribbble.model.Shot shot = shotRepository.findById(shotId)
//                .orElseThrow(() -> new RuntimeException("Shot not found"));
//
//        boolean alreadyLiked = likeRepository.existsByUserAndShot(user, shot);
//
//        if (alreadyLiked) {
//            // Unlike
//            com.example.dribbble.model.Like like = likeRepository.findByUserAndShot(user, shot)
//                    .orElseThrow(() -> new RuntimeException("Like record not found"));
//            likeRepository.delete(like);
//            shot.setLikeCount(Math.max(0, shot.getLikeCount() - 1));
//            shotRepository.save(shot);
//            return Map.of("liked", false, "likeCount", shot.getLikeCount());
//        } else {
//            // Like
//            com.example.dribbble.model.Like like = com.example.dribbble.model.Like.builder().user(user).shot(shot).build();
//            likeRepository.save(like);
//            shot.setLikeCount(shot.getLikeCount() + 1);
//            shotRepository.save(shot);
//            return Map.of("liked", true, "likeCount", shot.getLikeCount());
//        }
//    }
//
//    public List<Long> getLikedShotIds(String userEmail) {
//        com.example.dribbble.model.User user = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        return likeRepository.findLikedShotIdsByUserId(user.getId());
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
//public class LikeService {
//
//    private final LikeRepository likeRepository;
//    private final ShotRepository shotRepository;
//    private final UserRepository userRepository;
//
//    /**
//     * Toggle like: if already liked → unlike, else → like
//     * Returns new like count and liked status
//     */
//    @Transactional
//    public Map<String, Object> toggleLike(Long shotId, String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        Shot shot = shotRepository.findById(shotId)
//                .orElseThrow(() -> new RuntimeException("Shot not found"));
//
//        boolean alreadyLiked = likeRepository.existsByUserAndShot(user, shot);
//
//        if (alreadyLiked) {
//            // Remove like
//            likeRepository.findByUserAndShot(user, shot)
//                    .ifPresent(likeRepository::delete);
//            shot.setLikeCount(Math.max(0, shot.getLikeCount() - 1));
//        } else {
//            // Add like
//            Like like = Like.builder().user(user).shot(shot).build();
//            likeRepository.save(like);
//            shot.setLikeCount(shot.getLikeCount() + 1);
//        }
//
//        shotRepository.save(shot);
//
//        return Map.of(
//                "liked", !alreadyLiked,
//                "likeCount", shot.getLikeCount()
//        );
//    }
//
//    /**
//     * Get all shot IDs that the logged-in user has liked
//     */
//    public List<Long> getLikedShotIds(String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        return likeRepository.findShotIdsByUserId(user.getId());
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
public class LikeService {

    private final LikeRepository   likeRepository;
    private final ShotRepository   shotRepository;
    private final UserRepository   userRepository;
    private final ShotMapper       shotMapper;

    /* Toggle like / unlike */
    @Transactional
    public Map<String, Object> toggleLike(Long shotId, String userEmail) {
        User user = findUser(userEmail);
        Shot shot = findShot(shotId);

        boolean alreadyLiked = likeRepository.existsByUserAndShot(user, shot);

        if (alreadyLiked) {
            Like like = likeRepository.findByUserAndShot(user, shot)
                    .orElseThrow(() -> new RuntimeException("Like record not found"));
            likeRepository.delete(like);
            shot.setLikeCount(Math.max(0, shot.getLikeCount() - 1));
            shotRepository.save(shot);
            return Map.of("liked", false, "likeCount", shot.getLikeCount());
        } else {
            likeRepository.save(Like.builder().user(user).shot(shot).build());
            shot.setLikeCount(shot.getLikeCount() + 1);
            shotRepository.save(shot);
            return Map.of("liked", true, "likeCount", shot.getLikeCount());
        }
    }

    /* Returns only shot IDs (lightweight) */
    public List<Long> getLikedShotIds(String userEmail) {
        User user = findUser(userEmail);
        return likeRepository.findLikedShotIdsByUserId(user.getId());
    }

    /* Returns full shot details for liked shots — used by dashboard */
    public List<ShotResponse> getLikedShots(String userEmail) {
        User user = findUser(userEmail);
        return likeRepository.findByUser(user)
                .stream()
                .map(like -> shotMapper.toResponse(like.getShot()))
                .collect(Collectors.toList());
    }

    /* Check if a specific shot is liked by user */
    public boolean isLikedByUser(Long shotId, String userEmail) {
        User user = findUser(userEmail);
        Shot shot = findShot(shotId);
        return likeRepository.existsByUserAndShot(user, shot);
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