package com.indicaAI.repository;

import com.indicaAI.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    Optional<Review> findByUserMovieId(Long userMovieId);
    List<Review> findAllByUserMovieUserId(Long userId);
}
