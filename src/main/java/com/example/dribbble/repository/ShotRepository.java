//package com.example.dribbble.repository;
//
//import com.example.dribbble.model.Shot;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//import java.util.List;
//
//@Repository
//public interface ShotRepository extends JpaRepository<Shot, Long> {
//
//    // All shots sorted by likes (Popular)
//    List<Shot> findAllByOrderByLikeCountDesc();
//
//    // All shots sorted by date (New & Noteworthy)
//    List<Shot> findAllByOrderByCreatedAtDesc();
//
//    // By category sorted by likes
//    List<Shot> findByCategoryIgnoreCaseOrderByLikeCountDesc(String category);
//
//    // By category sorted by date
//    List<Shot> findByCategoryIgnoreCaseOrderByCreatedAtDesc(String category);
//
//    // Shots by a specific author
//    @Query("SELECT s FROM Shot s WHERE s.author.id = :userId ORDER BY s.createdAt DESC")
//    List<Shot> findByAuthorId(@Param("userId") Long userId);
//
//    // Search by title
//    @Query("SELECT s FROM Shot s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :query, '%')) ORDER BY s.likeCount DESC")
//    List<Shot> searchByTitle(@Param("query") String query);
//
//    List<com.example.dribbble.model.Shot> findByCategoryOrderByLikeCountDesc(String category);
//}




package com.example.dribbble.repository;

import com.example.dribbble.model.Shot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShotRepository extends JpaRepository<Shot, Long> {

    // All shots sorted by likes (Popular tab)
    List<Shot> findAllByOrderByLikeCountDesc();

    // All shots sorted by date (New & Noteworthy)
    List<Shot> findAllByOrderByCreatedAtDesc();

    // By category sorted by likes
    List<Shot> findByCategoryIgnoreCaseOrderByLikeCountDesc(String category);

    // By category sorted by date
    List<Shot> findByCategoryIgnoreCaseOrderByCreatedAtDesc(String category);

    // Shots by a specific author
    @Query("SELECT s FROM Shot s WHERE s.author.id = :authorId ORDER BY s.createdAt DESC")
    List<Shot> findByAuthorId(@Param("authorId") Long authorId);

    // Search by title
    @Query("SELECT s FROM Shot s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :q, '%')) ORDER BY s.likeCount DESC")
    List<Shot> searchByTitle(@Param("q") String query);
}