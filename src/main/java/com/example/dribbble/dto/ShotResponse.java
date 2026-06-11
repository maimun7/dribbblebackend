package com.example.dribbble.dto;

import lombok.*;
import java.time.LocalDateTime;

/**
 * Full shot details returned to frontend
 * Used in /api/likes/me/shots and /api/saves/me/shots
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShotResponse {

    private Long   id;
    private String title;
    private String description;
    private String imageUrl;
    private String category;
    private String bgStyle;
    private String badge;
    private Integer likeCount;
    private Integer viewCount;

    // Author info (flattened — no nested object)
    private Long   authorId;
    private String authorName;
    private String authorAvatarUrl;

    private LocalDateTime createdAt;
}