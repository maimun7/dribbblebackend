package com.example.dribbble.util;

import com.example.dribbble.dto.ShotResponse;
import com.example.dribbble.model.Shot;
import org.springframework.stereotype.Component;

@Component
public class ShotMapper {

    public ShotResponse toResponse(Shot shot) {
        return ShotResponse.builder()
                .id(shot.getId())
                .title(shot.getTitle())
                .description(shot.getDescription())
                .imageUrl(shot.getImageUrl())
                .category(shot.getCategory())
                .bgStyle(shot.getBgStyle())
                .badge(shot.getBadge())
                .likeCount(shot.getLikeCount())
                .viewCount(shot.getViewCount())
                .authorId(shot.getAuthor().getId())
                .authorName(shot.getAuthor().getName())
                .authorAvatarUrl(shot.getAuthor().getAvatarUrl())
                .createdAt(shot.getCreatedAt())
                .build();
    }
}