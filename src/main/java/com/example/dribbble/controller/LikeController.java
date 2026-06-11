//package com.example.dribbble.controller;
//
//import com.example.dribbble.model.*;
//import com.example.dribbble.repository.*;
//import com.example.dribbble.util.JwtUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor
//public class LikeController {
//
//    private final UserRepository userRepository;
//    private final ShotRepository shotRepository;
//    private final JwtUtil jwtUtil;
//
//    // ── LIKES ──
//
//    @PostMapping("/likes/{shotId}")
//    public ResponseEntity<?> toggleLike(
//            @PathVariable Long shotId,
//            @RequestHeader("Authorization") String authHeader) {
//
//        com.example.dribbble.model.User user = getUserFromHeader(authHeader);
//        com.example.dribbble.model.Shot shot = shotRepository.findById(shotId)
//                .orElseThrow(() -> new RuntimeException("Shot not found"));
//
//        boolean alreadyLiked = user.getLikedShots().contains(shot);
//
//        if (alreadyLiked) {
//            user.getLikedShots().remove(shot);
//            shot.setLikeCount(Math.max(0, shot.getLikeCount() - 1));
//        } else {
//            user.getLikedShots().add(shot);
//            shot.setLikeCount(shot.getLikeCount() + 1);
//        }
//
//        userRepository.save(user);
//        shotRepository.save(shot);
//
//        return ResponseEntity.ok(Map.of(
//                "liked", !alreadyLiked,
//                "likeCount", shot.getLikeCount()
//        ));
//    }
//
//    @GetMapping("/likes/me")
//    public ResponseEntity<?> getMyLikes(@RequestHeader("Authorization") String authHeader) {
//        com.example.dribbble.model.User user = getUserFromHeader(authHeader);
//        Set<Long> likedIds = user.getLikedShots().stream()
//                .map(com.example.dribbble.model.Shot::getId)
//                .collect(Collectors.toSet());
//        return ResponseEntity.ok(Map.of("likedIds", likedIds));
//    }
//
//    // ── SAVES ──
//
//    @PostMapping("/saves/{shotId}")
//    public ResponseEntity<?> toggleSave(
//            @PathVariable Long shotId,
//            @RequestHeader("Authorization") String authHeader) {
//
//        com.example.dribbble.model.User user = getUserFromHeader(authHeader);
//        com.example.dribbble.model.Shot shot = shotRepository.findById(shotId)
//                .orElseThrow(() -> new RuntimeException("Shot not found"));
//
//        boolean alreadySaved = user.getSavedShots().contains(shot);
//
//        if (alreadySaved) {
//            user.getSavedShots().remove(shot);
//        } else {
//            user.getSavedShots().add(shot);
//        }
//
//        userRepository.save(user);
//
//        return ResponseEntity.ok(Map.of("saved", !alreadySaved));
//    }
//
//    @GetMapping("/saves/me")
//    public ResponseEntity<?> getMySaves(@RequestHeader("Authorization") String authHeader) {
//        com.example.dribbble.model.User user = getUserFromHeader(authHeader);
//        Set<Long> savedIds = user.getSavedShots().stream()
//                .map(com.example.dribbble.model.Shot::getId)
//                .collect(Collectors.toSet());
//        return ResponseEntity.ok(Map.of("savedIds", savedIds));
//    }
//
//    // ── HELPER ──
//    private com.example.dribbble.model.User getUserFromHeader(String authHeader) {
//        String token = authHeader.replace("Bearer ", "");
//        String email = jwtUtil.extractEmail(token);
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//    }
//}





//package com.example.dribbble.controller;
//
//import com.example.dribbble.service.LikeService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * POST /api/likes/{shotId}  → toggle like (like/unlike)
// * GET  /api/likes/me        → get all liked shot IDs for current user
// *
// * All endpoints require Bearer JWT token in Authorization header.
// */
//@RestController
//@RequestMapping("/api/likes")
//@RequiredArgsConstructor
//public class LikeController {
//
//    private final LikeService likeService;
//
//    // Toggle like — if already liked → unlike, else → like
//    @PostMapping("/{shotId}")
//    public ResponseEntity<?> toggleLike(
//            @PathVariable Long shotId,
//            @AuthenticationPrincipal UserDetails userDetails) {
//        try {
//            Map<String, Object> result = likeService.toggleLike(shotId, userDetails.getUsername());
//            return ResponseEntity.ok(result);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
//        }
//    }
//
//    // Get all liked shot IDs for the logged-in user
//    @GetMapping("/me")
//    public ResponseEntity<List<Long>> getMyLikes(
//            @AuthenticationPrincipal UserDetails userDetails) {
//        List<Long> ids = likeService.getLikedShotIds(userDetails.getUsername());
//        return ResponseEntity.ok(ids);
//    }
//}





package com.example.dribbble.controller;

import com.example.dribbble.dto.ShotResponse;
import com.example.dribbble.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * POST /api/likes/{shotId}       → toggle like/unlike
 * GET  /api/likes/me             → get liked shot IDs only
 * GET  /api/likes/me/shots       → get FULL shot details for liked shots ← dashboard uses this
 * GET  /api/likes/me/{shotId}    → check if a specific shot is liked
 */
@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    /* Toggle like */
    @PostMapping("/{shotId}")
    public ResponseEntity<?> toggleLike(
            @PathVariable Long shotId,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Map<String, Object> result = likeService.toggleLike(shotId, userDetails.getUsername());
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    /* Get only IDs — for quick checking which shots are liked */
    @GetMapping("/me")
    public ResponseEntity<List<Long>> getMyLikedIds(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(likeService.getLikedShotIds(userDetails.getUsername()));
    }

    /* ★ Get FULL shot details for liked shots — UserDashboard uses this */
    @GetMapping("/me/shots")
    public ResponseEntity<List<ShotResponse>> getMyLikedShots(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(likeService.getLikedShots(userDetails.getUsername()));
    }

    /* Check if one specific shot is liked */
    @GetMapping("/me/{shotId}")
    public ResponseEntity<Map<String, Boolean>> isLiked(
            @PathVariable Long shotId,
            @AuthenticationPrincipal UserDetails userDetails) {
        boolean liked = likeService.isLikedByUser(shotId, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("liked", liked));
    }
}