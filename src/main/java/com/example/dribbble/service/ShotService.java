package com.example.dribbble.service;

import com.example.dribbble.model.Shot;
import com.example.dribbble.repository.ShotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business logic for fetching shots.
 * - getShots(): returns shots filtered by category and sorted by popular/new
 * - getShotById(): increments view count and returns shot
 * - searchShots(): searches by title keyword
 */
@Service
@RequiredArgsConstructor
public class ShotService {

    private final ShotRepository shotRepository;

    public List<Shot> getShots(String category, String sort) {
        boolean isNew = "new".equalsIgnoreCase(sort);
        boolean hasCategory = category != null && !category.isBlank()
                && !"Discover".equalsIgnoreCase(category);

        if (hasCategory && isNew) {
            return shotRepository.findByCategoryIgnoreCaseOrderByCreatedAtDesc(category);
        } else if (hasCategory) {
            return shotRepository.findByCategoryIgnoreCaseOrderByLikeCountDesc(category);
        } else if (isNew) {
            return shotRepository.findAllByOrderByCreatedAtDesc();
        } else {
            return shotRepository.findAllByOrderByLikeCountDesc();
        }
    }

    public Shot getShotById(Long id) {
        Shot shot = shotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shot not found with id: " + id));
        // Increment view count
        shot.setViewCount(shot.getViewCount() + 1);
        return shotRepository.save(shot);
    }

    public List<Shot> searchShots(String query) {
        return shotRepository.searchByTitle(query);
    }

    public List<Shot> getShotsByAuthor(Long userId) {
        return shotRepository.findByAuthorId(userId);
    }

    public List<Shot> getAllShotsSortedByLikes() {
        return shotRepository.findAllByOrderByLikeCountDesc();
    }
}