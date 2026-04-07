package com.indicaAI.repository;

import com.indicaAI.model.UserMovie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMovieRepository extends JpaRepository<UserMovie, Long> {
    List<UserMovie> findByUserId(Long userId);
    boolean existsByUserIdAndMovieId(Long userId, Long movieId);
    int countByUserIdAndStatus(Long userId, com.indicaAI.model.enums.StatusEnum status);
}