package com.indicaAI.repository;

import com.indicaAI.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {
    List<Movie> findAllByTitleContainingIgnoreCase(String title);

   Optional<Movie> findByTmdbId(Long id);
}
