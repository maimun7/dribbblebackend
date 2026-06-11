//package com.example.dribbble.controller;
//
//import com.example.dribbble.service.SaveService;
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
// * POST /api/saves/{shotId}  → toggle save (save/unsave / bookmark)
// * GET  /api/saves/me        → get all saved shot IDs for current user
// *
// * All endpoints require Bearer JWT token in Authorization header.
// */
//@RestController
//@RequestMapping("/api/saves")
//@RequiredArgsConstructor
//public class SaveController {
//
//    private final SaveService saveService;
//
//    // Toggle save — if already saved → unsave, else → save
//    @PostMapping("/{shotId}")
//    public ResponseEntity<?> toggleSave(
//            @PathVariable Long shotId,
//            @AuthenticationPrincipal UserDetails userDetails) {
//        try {
//            Map<String, Object> result = saveService.toggleSave(shotId, userDetails.getUsername());
//            return ResponseEntity.ok(result);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
//        }
//    }
//
//    // Get all saved shot IDs for the logged-in user
//    @GetMapping("/me")
//    public ResponseEntity<List<Long>> getMySaves(
//            @AuthenticationPrincipal UserDetails userDetails) {
//        List<Long> ids = saveService.getSavedShotIds(userDetails.getUsername());
//        return ResponseEntity.ok(ids);
//    }
//}





package com.example.dribbble.controller;

import com.example.dribbble.dto.ShotResponse;
import com.example.dribbble.service.SaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * POST /api/saves/{shotId}       → toggle save/unsave
 * GET  /api/saves/me             → get saved shot IDs only
 * GET  /api/saves/me/shots       → get FULL shot details for saved shots ← dashboard uses this
 * GET  /api/saves/me/{shotId}    → check if a specific shot is saved
 */
@RestController
@RequestMapping("/api/saves")
@RequiredArgsConstructor
public class SaveController {

    private final SaveService saveService;

    /* Toggle save */
    @PostMapping("/{shotId}")
    public ResponseEntity<?> toggleSave(
            @PathVariable Long shotId,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Map<String, Object> result = saveService.toggleSave(shotId, userDetails.getUsername());
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    /* Get only IDs */
    @GetMapping("/me")
    public ResponseEntity<List<Long>> getMySavedIds(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(saveService.getSavedShotIds(userDetails.getUsername()));
    }

    /* ★ Get FULL shot details for saved shots — UserDashboard uses this */
    @GetMapping("/me/shots")
    public ResponseEntity<List<ShotResponse>> getMySavedShots(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(saveService.getSavedShots(userDetails.getUsername()));
    }

    /* Check if one specific shot is saved */
    @GetMapping("/me/{shotId}")
    public ResponseEntity<Map<String, Boolean>> isSaved(
            @PathVariable Long shotId,
            @AuthenticationPrincipal UserDetails userDetails) {
        boolean saved = saveService.isSavedByUser(shotId, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("saved", saved));
    }
}