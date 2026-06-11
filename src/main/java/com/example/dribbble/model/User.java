
//package com.example.dribbble.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.time.LocalDateTime;
//import java.util.HashSet;
//import java.util.Set;
//
//@Entity
//@Table(name = "users")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, length = 100)
//    private String name;
//
//    @Column(nullable = false, unique = true, length = 150)
//    private String email;
//
//    @Column(nullable = false)
//    private String password;
//
//    @Column(length = 50)
//    private String location;
//
//    @Column(length = 500)
//    private String bio;
//
//    @Column(name = "avatar_url")
//    private String avatarUrl;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    @Builder.Default
//    private Role role = Role.USER;
//
//    @Column(name = "created_at", nullable = false, updatable = false)
//    @Builder.Default
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "user_likes",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "shot_id"))
//    @Builder.Default
//    private Set<com.example.dribbble.model.Shot> likedShots = new HashSet<>();
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "user_saves",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "shot_id"))
//    @Builder.Default
//    private Set<com.example.dribbble.model.Shot> savedShots = new HashSet<>();
//
//    public enum Role { USER, ADMIN }
//}




//package com.example.dribbble.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "users")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, length = 100)
//    private String name;
//
//    @Column(nullable = false, unique = true, length = 150)
//    private String email;
//
//    @Column(nullable = false)
//    private String password;
//
//    @Column(length = 100)
//    private String location;
//
//    @Column(length = 500)
//    private String bio;
//
//    @Column(name = "avatar_url", length = 500)
//    private String avatarUrl;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false, length = 20)
//    @Builder.Default
//    private Role role = Role.USER;
//
//    @Column(name = "created_at", nullable = false, updatable = false)
//    @Builder.Default
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    public enum Role {
//        USER, ADMIN
//    }
//}


package com.example.dribbble.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


//import jakarta.persistence.Entity;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    // ✅ ADD THIS
//    @Column(unique = true, length = 100)
//    private String username;

    @Column(length = 100)
    private String location;

    @Column(length = 500)
    private String bio;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Role role = Role.USER;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Role {
        USER, ADMIN
    }
}