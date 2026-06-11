//package com.example.dribbble.controller;
//
//import com.example.dribbble.model.*;
//import com.example.dribbble.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/admin")
//@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
//public class AdminController {
//
//    private final com.example.dribbble.repository.UserRepository userRepository;
//    private final com.example.dribbble.repository.ShotRepository shotRepository;
//
//    // GET all users with their liked and saved shots
//    @GetMapping("/users")
//    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
//        List<com.example.dribbble.model.User> users = userRepository.findAll();
//
//        List<Map<String, Object>> result = users.stream().map(user -> {
//            Map<String, Object> map = new LinkedHashMap<>();
//            map.put("id", user.getId());
//            map.put("name", user.getName());
//            map.put("email", user.getEmail());
//            map.put("role", user.getRole());
//            map.put("location", user.getLocation());
//            map.put("createdAt", user.getCreatedAt());
//
//            // Liked shots summary
//            List<Map<String, Object>> liked = user.getLikedShots().stream()
//                    .map(s -> Map.<String, Object>of(
//                            "id", s.getId(),
//                            "title", s.getTitle(),
//                            "imageUrl", s.getImageUrl() != null ? s.getImageUrl() : ""
//                    )).collect(Collectors.toList());
//            map.put("likedShots", liked);
//            map.put("likedCount", liked.size());
//
//            // Saved shots summary
//            List<Map<String, Object>> saved = user.getSavedShots().stream()
//                    .map(s -> Map.<String, Object>of(
//                            "id", s.getId(),
//                            "title", s.getTitle(),
//                            "imageUrl", s.getImageUrl() != null ? s.getImageUrl() : ""
//                    )).collect(Collectors.toList());
//            map.put("savedShots", saved);
//            map.put("savedCount", saved.size());
//
//            return map;
//        }).collect(Collectors.toList());
//
//        return ResponseEntity.ok(result);
//    }
//
//    // GET single user detail
//    @GetMapping("/users/{id}")
//    public ResponseEntity<?> getUserDetail(@PathVariable Long id) {
//        return userRepository.findById(id)
//                .map(user -> {
//                    Map<String, Object> map = new LinkedHashMap<>();
//                    map.put("id", user.getId());
//                    map.put("name", user.getName());
//                    map.put("email", user.getEmail());
//                    map.put("role", user.getRole());
//                    map.put("location", user.getLocation());
//                    map.put("bio", user.getBio());
//                    map.put("createdAt", user.getCreatedAt());
//                    map.put("likedShots", user.getLikedShots());
//                    map.put("savedShots", user.getSavedShots());
//                    return ResponseEntity.ok(map);
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    // GET all shots sorted by popularity
//    @GetMapping("/shots")
//    public ResponseEntity<List<com.example.dribbble.model.Shot>> getAllShots() {
//        return ResponseEntity.ok(shotRepository.findAllByOrderByLikeCountDesc());
//    }
//
//    // GET dashboard stats
//    @GetMapping("/stats")
//    public ResponseEntity<Map<String, Object>> getStats() {
//        long totalUsers = userRepository.count();
//        long totalShots = shotRepository.count();
//
//        long totalLikes = userRepository.findAll().stream()
//                .mapToLong(u -> u.getLikedShots().size())
//                .sum();
//
//        long totalSaves = userRepository.findAll().stream()
//                .mapToLong(u -> u.getSavedShots().size())
//                .sum();
//
//        return ResponseEntity.ok(Map.of(
//                "totalUsers", totalUsers,
//                "totalShots", totalShots,
//                "totalLikes", totalLikes,
//                "totalSaves", totalSaves
//        ));
//    }
//
//    // DELETE user
//    @DeleteMapping("/users/{id}")
//    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
//        if (!userRepository.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        userRepository.deleteById(id);
//        return ResponseEntity.ok(Map.of("message", "User deleted"));
//    }
//
//    // PUT promote user to ADMIN
//    @PutMapping("/users/{id}/promote")
//    public ResponseEntity<?> promoteToAdmin(@PathVariable Long id) {
//        return userRepository.findById(id)
//                .map(user -> {
//                    user.setRole(com.example.dribbble.model.User.Role.ADMIN);
//                    userRepository.save(user);
//                    return ResponseEntity.ok(Map.of("message", "User promoted to ADMIN"));
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//}



////package com.example.dribbble.controller;
////
////import com.example.dribbble.model.*;
////import com.example.dribbble.repository.*;
////import com.example.dribbble.service.LikeService;
////import com.example.dribbble.service.SaveService;
////import lombok.RequiredArgsConstructor;
////import org.springframework.http.ResponseEntity;
////import org.springframework.security.access.prepost.PreAuthorize;
////import org.springframework.web.bind.annotation.*;
////
////import java.util.*;
////import java.util.stream.Collectors;
////
/////**
//// * Admin-only endpoints (requires ROLE_ADMIN JWT token)
//// *
//// * GET  /api/admin/stats          → total users, shots, likes, saves
//// * GET  /api/admin/users          → all users with their liked + saved shots
//// * GET  /api/admin/users/{id}     → single user detail
//// * PUT  /api/admin/users/{id}/promote  → promote user to ADMIN
//// * DELETE /api/admin/users/{id}   → delete user
//// * GET  /api/admin/shots          → all shots sorted by likes
//// * GET  /api/admin/likes          → all like records
//// * GET  /api/admin/saves          → all save records
//// */
////@RestController
////@RequestMapping("/api/admin")
////@RequiredArgsConstructor
////@PreAuthorize("hasRole('ADMIN')")
////public class AdminController {
////
////    private final UserRepository userRepository;
////    private final ShotRepository shotRepository;
////    private final LikeRepository likeRepository;
////    private final SaveRepository saveRepository;
////
////    // ── DASHBOARD STATS ─────────────────────────────
////    @GetMapping("/stats")
////    public ResponseEntity<Map<String, Object>> getStats() {
////        Map<String, Object> stats = new LinkedHashMap<>();
////        stats.put("totalUsers",  userRepository.count());
////        stats.put("totalShots",  shotRepository.count());
////        stats.put("totalLikes",  likeRepository.count());
////        stats.put("totalSaves",  saveRepository.count());
////        return ResponseEntity.ok(stats);
////    }
////
////    // ── ALL USERS with liked + saved shots ──────────
////    @GetMapping("/users")
////    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
////        List<User> users = userRepository.findAll();
////
////        List<Map<String, Object>> result = users.stream().map(user -> {
////            Map<String, Object> map = new LinkedHashMap<>();
////            map.put("id",        user.getId());
////            map.put("name",      user.getName());
////            map.put("email",     user.getEmail());
////            map.put("role",      user.getRole());
////            map.put("location",  user.getLocation());
////            map.put("avatarUrl", user.getAvatarUrl());
////            map.put("createdAt", user.getCreatedAt());
////
////            // Liked shots
////            List<Like> likes = likeRepository.findByUser(user);
////            List<Map<String, Object>> likedShots = likes.stream().map(l -> {
////                Map<String, Object> s = new LinkedHashMap<>();
////                s.put("shotId",    l.getShot().getId());
////                s.put("title",     l.getShot().getTitle());
////                s.put("imageUrl",  l.getShot().getImageUrl());
////                s.put("likedAt",   l.getCreatedAt());
////                return s;
////            }).collect(Collectors.toList());
////
////            map.put("likedShots", likedShots);
////            map.put("likedCount", likedShots.size());
////
////            // Saved shots
////            List<Save> saves = saveRepository.findByUser(user);
////            List<Map<String, Object>> savedShots = saves.stream().map(sv -> {
////                Map<String, Object> s = new LinkedHashMap<>();
////                s.put("shotId",   sv.getShot().getId());
////                s.put("title",    sv.getShot().getTitle());
////                s.put("imageUrl", sv.getShot().getImageUrl());
////                s.put("savedAt",  sv.getCreatedAt());
////                return s;
////            }).collect(Collectors.toList());
////
////            map.put("savedShots", savedShots);
////            map.put("savedCount", savedShots.size());
////
////            return map;
////        }).collect(Collectors.toList());
////
////        return ResponseEntity.ok(result);
////    }
////
////    // ── SINGLE USER DETAIL ───────────────────────────
////    @GetMapping("/users/{id}")
////    public ResponseEntity<?> getUserDetail(@PathVariable Long id) {
////        return userRepository.findById(id).map(user -> {
////            Map<String, Object> map = new LinkedHashMap<>();
////            map.put("id",        user.getId());
////            map.put("name",      user.getName());
////            map.put("email",     user.getEmail());
////            map.put("role",      user.getRole());
////            map.put("location",  user.getLocation());
////            map.put("bio",       user.getBio());
////            map.put("avatarUrl", user.getAvatarUrl());
////            map.put("createdAt", user.getCreatedAt());
////
////            List<Like> likes = likeRepository.findByUser(user);
////            map.put("likedShots", likes.stream().map(l -> Map.of(
////                    "shotId", l.getShot().getId(),
////                    "title",  l.getShot().getTitle(),
////                    "likedAt",l.getCreatedAt()
////            )).collect(Collectors.toList()));
////
////            List<Save> saves = saveRepository.findByUser(user);
////            map.put("savedShots", saves.stream().map(sv -> Map.of(
////                    "shotId",  sv.getShot().getId(),
////                    "title",   sv.getShot().getTitle(),
////                    "savedAt", sv.getCreatedAt()
////            )).collect(Collectors.toList()));
////
////            return ResponseEntity.ok(map);
////        }).orElse(ResponseEntity.notFound().build());
////    }
////
////    // ── PROMOTE USER TO ADMIN ────────────────────────
////    @PutMapping("/users/{id}/promote")
////    public ResponseEntity<?> promoteToAdmin(@PathVariable Long id) {
////        return userRepository.findById(id).map(user -> {
////            user.setRole(User.Role.ADMIN);
////            userRepository.save(user);
////            return ResponseEntity.ok(Map.of(
////                    "message", "User promoted to ADMIN",
////                    "userId",  id,
////                    "name",    user.getName()
////            ));
////        }).orElse(ResponseEntity.notFound().build());
////    }
////
////    // ── DELETE USER ──────────────────────────────────
////    @DeleteMapping("/users/{id}")
////    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
////        if (!userRepository.existsById(id)) {
////            return ResponseEntity.notFound().build();
////        }
////        // Remove all likes and saves first
////        User user = userRepository.findById(id).get();
////        likeRepository.findByUser(user).forEach(likeRepository::delete);
////        saveRepository.findByUser(user).forEach(saveRepository::delete);
////        userRepository.deleteById(id);
////        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
////    }
////
////    // ── ALL SHOTS ────────────────────────────────────
////    @GetMapping("/shots")
////    public ResponseEntity<?> getAllShots() {
////        List<Shot> shots = shotRepository.findAllByOrderByLikeCountDesc();
////        List<Map<String, Object>> result = shots.stream().map(shot -> {
////            Map<String, Object> map = new LinkedHashMap<>();
////            map.put("id",         shot.getId());
////            map.put("title",      shot.getTitle());
////            map.put("category",   shot.getCategory());
////            map.put("likeCount",  shot.getLikeCount());
////            map.put("viewCount",  shot.getViewCount());
////            map.put("author",     shot.getAuthor().getName());
////            map.put("createdAt",  shot.getCreatedAt());
////            return map;
////        }).collect(Collectors.toList());
////        return ResponseEntity.ok(result);
////    }
////
////    // ── ALL LIKES (raw records) ──────────────────────
////    @GetMapping("/likes")
////    public ResponseEntity<?> getAllLikes() {
////        List<Like> likes = likeRepository.findAll();
////        List<Map<String, Object>> result = likes.stream().map(l -> {
////            Map<String, Object> map = new LinkedHashMap<>();
////            map.put("likeId",    l.getId());
////            map.put("userName",  l.getUser().getName());
////            map.put("userEmail", l.getUser().getEmail());
////            map.put("shotId",    l.getShot().getId());
////            map.put("shotTitle", l.getShot().getTitle());
////            map.put("likedAt",   l.getCreatedAt());
////            return map;
////        }).collect(Collectors.toList());
////        return ResponseEntity.ok(result);
////    }
////
////    // ── ALL SAVES (raw records) ──────────────────────
////    @GetMapping("/saves")
////    public ResponseEntity<?> getAllSaves() {
////        List<Save> saves = saveRepository.findAll();
////        List<Map<String, Object>> result = saves.stream().map(sv -> {
////            Map<String, Object> map = new LinkedHashMap<>();
////            map.put("saveId",    sv.getId());
////            map.put("userName",  sv.getUser().getName());
////            map.put("userEmail", sv.getUser().getEmail());
////            map.put("shotId",    sv.getShot().getId());
////            map.put("shotTitle", sv.getShot().getTitle());
////            map.put("savedAt",   sv.getCreatedAt());
////            return map;
////        }).collect(Collectors.toList());
////        return ResponseEntity.ok(result);
////    }
//}



package com.example.dribbble.controller;

import com.example.dribbble.model.*;
import com.example.dribbble.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;
    private final ShotRepository shotRepository;
    private final LikeRepository likeRepository;
    private final SaveRepository saveRepository;

    /* ── STATS ── */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(Map.of(
                "totalUsers",  userRepository.count(),
                "totalShots",  shotRepository.count(),
                "totalLikes",  likeRepository.count(),
                "totalSaves",  saveRepository.count()
        ));
    }

    /* ── ALL USERS with full liked + saved shot details ── */
    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<Map<String, Object>> result = users.stream().map(user -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id",        user.getId());
            map.put("name",      user.getName());
            map.put("email",     user.getEmail());
            map.put("role",      user.getRole());
            map.put("location",  user.getLocation());
            map.put("avatarUrl", user.getAvatarUrl());
            map.put("createdAt", user.getCreatedAt());

            // Full liked shots
            List<Map<String, Object>> likedShots = likeRepository.findByUser(user)
                    .stream()
                    .map(l -> buildShotMap(l.getShot(), l.getCreatedAt(), "likedAt"))
                    .collect(Collectors.toList());
            map.put("likedShots", likedShots);
            map.put("likedCount", likedShots.size());

            // Full saved shots
            List<Map<String, Object>> savedShots = saveRepository.findByUser(user)
                    .stream()
                    .map(sv -> buildShotMap(sv.getShot(), sv.getCreatedAt(), "savedAt"))
                    .collect(Collectors.toList());
            map.put("savedShots", savedShots);
            map.put("savedCount", savedShots.size());

            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /* ── SINGLE USER ── */
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserDetail(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id",        user.getId());
            map.put("name",      user.getName());
            map.put("email",     user.getEmail());
            map.put("role",      user.getRole());
            map.put("location",  user.getLocation());
            map.put("bio",       user.getBio());
            map.put("avatarUrl", user.getAvatarUrl());
            map.put("createdAt", user.getCreatedAt());

            map.put("likedShots", likeRepository.findByUser(user).stream()
                    .map(l -> buildShotMap(l.getShot(), l.getCreatedAt(), "likedAt"))
                    .collect(Collectors.toList()));

            map.put("savedShots", saveRepository.findByUser(user).stream()
                    .map(sv -> buildShotMap(sv.getShot(), sv.getCreatedAt(), "savedAt"))
                    .collect(Collectors.toList()));

            return ResponseEntity.ok(map);
        }).orElse(ResponseEntity.notFound().build());
    }

    /* ── PROMOTE USER ── */
    @PutMapping("/users/{id}/promote")
    public ResponseEntity<?> promoteToAdmin(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            user.setRole(User.Role.ADMIN);
            userRepository.save(user);
            return ResponseEntity.ok(Map.of(
                    "message", "User promoted to ADMIN",
                    "userId", id, "name", user.getName()
            ));
        }).orElse(ResponseEntity.notFound().build());
    }

    /* ── DELETE USER ── */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) return ResponseEntity.notFound().build();
        User user = userRepository.findById(id).get();
        likeRepository.findByUser(user).forEach(likeRepository::delete);
        saveRepository.findByUser(user).forEach(saveRepository::delete);
        userRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    /* ── ALL SHOTS ── */
    @GetMapping("/shots")
    public ResponseEntity<List<Map<String, Object>>> getAllShots() {
        return ResponseEntity.ok(
                shotRepository.findAllByOrderByLikeCountDesc().stream().map(shot -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id",        shot.getId());
                    m.put("title",     shot.getTitle());
                    m.put("category",  shot.getCategory());
                    m.put("bgStyle",   shot.getBgStyle());
                    m.put("badge",     shot.getBadge());
                    m.put("likeCount", shot.getLikeCount());
                    m.put("viewCount", shot.getViewCount());
                    m.put("authorId",  shot.getAuthor().getId());
                    m.put("author",    shot.getAuthor().getName());
                    m.put("createdAt", shot.getCreatedAt());
                    return m;
                }).collect(Collectors.toList())
        );
    }

    /* ── ALL LIKES (with full shot info) ── */
    @GetMapping("/likes")
    public ResponseEntity<List<Map<String, Object>>> getAllLikes() {
        return ResponseEntity.ok(
                likeRepository.findAll().stream().map(l -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("likeId",     l.getId());
                    m.put("userName",   l.getUser().getName());
                    m.put("userEmail",  l.getUser().getEmail());
                    m.put("shotId",     l.getShot().getId());
                    m.put("shotTitle",  l.getShot().getTitle());
                    m.put("shotBg",     l.getShot().getBgStyle());
                    m.put("category",   l.getShot().getCategory());
                    m.put("likedAt",    l.getCreatedAt());
                    return m;
                }).collect(Collectors.toList())
        );
    }

    /* ── ALL SAVES (with full shot info) ── */
    @GetMapping("/saves")
    public ResponseEntity<List<Map<String, Object>>> getAllSaves() {
        return ResponseEntity.ok(
                saveRepository.findAll().stream().map(sv -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("saveId",     sv.getId());
                    m.put("userName",   sv.getUser().getName());
                    m.put("userEmail",  sv.getUser().getEmail());
                    m.put("shotId",     sv.getShot().getId());
                    m.put("shotTitle",  sv.getShot().getTitle());
                    m.put("shotBg",     sv.getShot().getBgStyle());
                    m.put("category",   sv.getShot().getCategory());
                    m.put("savedAt",    sv.getCreatedAt());
                    return m;
                }).collect(Collectors.toList())
        );
    }

    /* ── HELPER — builds a shot detail map with timestamp ── */
    private Map<String, Object> buildShotMap(Shot shot, Object timestamp, String tsKey) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("shotId",    shot.getId());
        m.put("title",     shot.getTitle());
        m.put("category",  shot.getCategory());
        m.put("bgStyle",   shot.getBgStyle());
        m.put("badge",     shot.getBadge());
        m.put("likeCount", shot.getLikeCount());
        m.put("viewCount", shot.getViewCount());
        m.put("imageUrl",  shot.getImageUrl());
        m.put("authorId",  shot.getAuthor().getId());
        m.put("author",    shot.getAuthor().getName());
        m.put(tsKey,       timestamp);
        return m;
    }
}