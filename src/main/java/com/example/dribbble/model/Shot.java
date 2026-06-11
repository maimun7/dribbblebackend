//package com.example.dribbble.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "shots")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Shot {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, length = 200)
//    private String title;
//
//    @Column(length = 1000)
//    private String description;
//
//    @Column(name = "image_url")
//    private String imageUrl;
//
//    @Column(length = 50)
//    private String category;
//
//    @Column(name = "like_count")
//    @Builder.Default
//    private Integer likeCount = 0;
//
//    @Column(name = "view_count")
//    @Builder.Default
//    private Integer viewCount = 0;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private com.example.dribbble.model.User author;
//
//    @Column(name = "created_at", nullable = false, updatable = false)
//    @Builder.Default
//    private LocalDateTime createdAt = LocalDateTime.now();
//}



//package com.example.dribbble.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "shots")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Shot {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, length = 200)
//    private String title;
//
//    @Column(length = 2000)
//    private String description;
//
//    @Column(name = "image_url", length = 500)
//    private String imageUrl;
//
//    // e.g. "Discover", "Animation", "Branding" etc.
//    @Column(length = 50)
//    private String category;
//
//    // badge: "", "pro", "proplus"
//    @Column(length = 20)
//    @Builder.Default
//    private String badge = "";
//
//    @Column(name = "like_count")
//    @Builder.Default
//    private Integer likeCount = 0;
//
//    @Column(name = "view_count")
//    @Builder.Default
//    private Integer viewCount = 0;
//
//    @Column(name = "bg_style", length = 300)
//    private String bgStyle; // CSS gradient or hex color
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "author_id", nullable = false)
//    private User author;
//
//    @Column(name = "created_at", nullable = false, updatable = false)
//    @Builder.Default
//    private LocalDateTime createdAt = LocalDateTime.now();
//}




package com.example.dribbble.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shots")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Shot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(length = 50)
    private String category;

    @Column(length = 20)
    @Builder.Default
    private String badge = "";

    @Column(name = "like_count")
    @Builder.Default
    private Integer likeCount = 0;

    @Column(name = "view_count")
    @Builder.Default
    private Integer viewCount = 0;

    @Column(name = "bg_style", length = 300)
    private String bgStyle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User author;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}