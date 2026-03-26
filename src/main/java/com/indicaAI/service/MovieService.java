package com.indicaAI.service;

import com.indicaAI.dtos.MovieDto;
import com.indicaAI.dtos.TmbdResponse;
import com.indicaAI.dtos.TmdbMovieDto;
import com.indicaAI.model.Movie;
import com.indicaAI.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final TmbdService tmbdService;
    private final MovieWrapper movieWrapper;
    private Supplier<? extends Throwable> RuntimeException;

    public MovieService(MovieRepository movieRepository, TmbdService tmbdService, MovieWrapper movieWrapper) {
        this.movieRepository = movieRepository;
        this.tmbdService = tmbdService;
        this.movieWrapper = movieWrapper;
    }

    public List<MovieDto> searchMovies(String title) {
        if (title == null || title.isBlank()) {
            throw new RuntimeException("Title inválido.");
        }

        List<Movie> localMovies = movieRepository.findAllByTitleContainingIgnoreCase(title);
        if (!localMovies.isEmpty()) {
            return localMovies.stream().map(movieWrapper::movieToMovieDto).toList();
        }

        return tmbdService.searchFilm(title)
                .stream()
                .map(movieWrapper::tmdbToMovieDto)
                .toList();
    }

    public MovieDto searchMovieByid (Long id) {
        if( id < 0 ){
            throw new RuntimeException("Id inválido.");
        };
        try {
            MovieDto movieResponse = movieRepository.findById(id).map(movieWrapper::movieToMovieDto).orElseThrow(RuntimeException);
            return movieResponse;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
}
    public Movie saveMovie (TmdbMovieDto dto) {
        return movieRepository.findByTmdbId(dto.id())
                .orElseGet(() -> {
                    Movie movie = movieWrapper.tmbdMovieDtoToMovie(dto);
                    return movieRepository.save(movie);
                });

    }





}









