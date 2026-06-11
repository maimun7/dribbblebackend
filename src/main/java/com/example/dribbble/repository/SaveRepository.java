//package com.example.dribbble.repository;
//
//import com.example.dribbble.model.Save;
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
//public interface SaveRepository extends JpaRepository<Save, Long> {
//
//    // Check if user already saved a shot
//    boolean existsByUserAndShot(User user, Shot shot);
//
//    // Find a specific save record (to delete it)
//    Optional<Save> findByUserAndShot(User user, Shot shot);
//
//    // Get all saves by a user
//    List<Save> findByUser(User user);
//
//    // Count total saves by a user
//    long countByUser(User user);
//
//    // Get saved shot IDs for a user (for frontend)
//    @Query("SELECT s.shot.id FROM Save s WHERE s.user.id = :userId")
//    List<Long> findSavedShotIdsByUserId(@Param("userId") Long userId);
//}




package com.example.dribbble.repository;

import com.example.dribbble.model.Save;
import com.example.dribbble.model.Shot;
import com.example.dribbble.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaveRepository extends JpaRepository<Save, Long> {

    // Check if a user already saved a shot
    boolean existsByUserAndShot(User user, Shot shot);

    // Find the save record to delete it
    Optional<Save> findByUserAndShot(User user, Shot shot);

    // Get all saves by a user
    List<Save> findByUser(User user);

    // Count how many times a shot was saved
    long countByShot(Shot shot);

    // Get all shot IDs saved by a user (for frontend state)
    @Query("SELECT s.shot.id FROM Save s WHERE s.user.id = :userId")
//    List<Long> findShotIdsByUserId(@Param("userId") Long userId);
    List<Long> findSavedShotIdsByUserId(@Param("userId") Long userId);
}