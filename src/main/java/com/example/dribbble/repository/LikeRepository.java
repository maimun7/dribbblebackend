//package com.example.dribbble.repository;
//
//import com.example.dribbble.model.Like;
//import com.example.dribbble.model.Shot;
//import com.example.dribbble.model.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface LikeRepository extends JpaRepository<Like, Long> {
//
//    // Check if user already liked a shot
//    boolean existsByUserAndShot(User user, Shot shot);
//
//    // Find a specific like record (to delete it)
//    Optional<Like> findByUserAndShot(User user, Shot shot);
//
//    // Get all likes by a user
//    List<Like> findByUser(User user);
//
//    // Get all likes for a shot
//    List<Like> findByShot(Shot shot);
//
//    // Count total likes a user has given
//    long countByUser(User user);
//
//    // Get liked shot IDs for a user (for frontend)
//    @Query("SELECT l.shot.id FROM Like l WHERE l.user.id = :userId")
//    List<Long> findLikedShotIdsByUserId(@Param("userId") Long userId);
//}



package com.example.dribbble.repository;

import com.example.dribbble.model.Like;
import com.example.dribbble.model.Shot;
import com.example.dribbble.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    // Check if a user already liked a shot
    boolean existsByUserAndShot(User user, Shot shot);

    // Find the like record to delete it
    Optional<Like> findByUserAndShot(User user, Shot shot);

    // Get all likes by a user
    List<Like> findByUser(User user);

    // Get all likes for a shot
    List<Like> findByShot(Shot shot);

    // Count how many users liked a shot
    long countByShot(Shot shot);

    // Get all shot IDs liked by a user (for frontend state)
    @Query("SELECT l.shot.id FROM Like l WHERE l.user.id = :userId")
//    List<Long> findShotIdsByUserId(@Param("userId") Long userId);
    List<Long> findLikedShotIdsByUserId(@Param("userId") Long userId);
}