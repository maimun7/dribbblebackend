//package com.example.dribbble.controller;
//
//import com.example.dribbble.model.Shot;
//import com.example.dribbble.repository.ShotRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/shots")
//@RequiredArgsConstructor
//public class ShotController {
//
//    private final ShotRepository shotRepository;
//
//    // GET all shots — sorted by likes (popular)
//    @GetMapping
//    public ResponseEntity<List<Shot>> getAllShots(
//            @RequestParam(defaultValue = "popular") String sort,
//            @RequestParam(required = false) String category) {
//
//        List<Shot> shots;
//
//        if (category != null && !category.isBlank()) {
//            shots = shotRepository.findByCategoryOrderByLikeCountDesc(category);
//        } else if ("new".equalsIgnoreCase(sort)) {
//            shots = shotRepository.findAllByOrderByCreatedAtDesc();
//        } else {
//            shots = shotRepository.findAllByOrderByLikeCountDesc();
//        }
//
//        return ResponseEntity.ok(shots);
//    }
//
//    // GET single shot
//    @GetMapping("/{id}")
//    public ResponseEntity<Shot> getShot(@PathVariable Long id) {
//        return shotRepository.findById(id)
//                .map(shot -> {
//                    shot.setViewCount(shot.getViewCount() + 1);
//                    shotRepository.save(shot);
//                    return ResponseEntity.ok(shot);
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//}



package com.example.dribbble.controller;

import com.example.dribbble.model.Shot;
import com.example.dribbble.service.ShotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * GET /api/shots                        → all shots (popular)
 * GET /api/shots?category=Branding      → filtered by category
 * GET /api/shots?sort=new               → sorted by newest
 * GET /api/shots?category=Mobile&sort=new
 * GET /api/shots/search?q=dashboard     → search by title
 * GET /api/shots/{id}                   → single shot + increments view
 */
@RestController
@RequestMapping("/api/shots")
@RequiredArgsConstructor
public class ShotController {

    private final ShotService shotService;

    @GetMapping
    public ResponseEntity<List<Shot>> getShots(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "popular") String sort) {
        return ResponseEntity.ok(shotService.getShots(category, sort));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Shot>> search(@RequestParam String q) {
        if (q == null || q.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(shotService.searchShots(q));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getShotById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(shotService.getShotById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Shot>> getShotsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(shotService.getShotsByAuthor(userId));
    }
}