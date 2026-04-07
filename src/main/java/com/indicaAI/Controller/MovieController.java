package com.indicaAI.Controller;

import com.indicaAI.dtos.MovieDto;
import com.indicaAI.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieDto>> searchMovies(@RequestParam String query) {
        return ResponseEntity.ok(movieService.searchMovies(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> searchById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.searchMovieByid(id));
    }
}
