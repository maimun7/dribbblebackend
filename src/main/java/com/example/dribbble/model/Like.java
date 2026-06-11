//package com.example.dribbble.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(
//        name = "likes",
//        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "shot_id"})
//)
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Like {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private com.example.dribbble.model.User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "shot_id", nullable = false)
//    private com.example.dribbble.model.Shot shot;
//
//    @Column(name = "created_at", nullable = false, updatable = false)
//    @Builder.Default
//    private LocalDateTime createdAt = LocalDateTime.now();
//}



package com.example.dribbble.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "likes",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_shot_like",
                columnNames = {"user_id", "shot_id"}
        )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shot_id", nullable = false)
    private Shot shot;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}