package com.indicaAI.repository;

import com.indicaAI.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    Optional<Review> findByUserMovieIdOrderByCreatedAtDesc(Long userMovieId);
    List<Review> findAllByUserMovieUserId(Long userId);
    List<Review> findAllByOrderByCreatedAtDesc();
}
