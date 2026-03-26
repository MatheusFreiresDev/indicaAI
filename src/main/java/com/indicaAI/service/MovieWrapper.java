package com.indicaAI.service;

import com.indicaAI.dtos.MovieDto;
import com.indicaAI.dtos.TmdbMovieDto;
import com.indicaAI.model.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieWrapper {

    public MovieDto tmdbToMovieDto(TmdbMovieDto dto) {
        return new MovieDto(dto.title(), dto.poster_path(), dto.overview());
    }

    public MovieDto movieToMovieDto(Movie movie) {
        return new MovieDto(movie.getTitle(), movie.getPosterPath(), movie.getOverview());
    }
    public Movie tmbdMovieDtoToMovie(TmdbMovieDto dto) {
        if (dto == null) return null;

        return Movie.builder()
                .tmdbId(dto.id())
                .title(dto.title())
                .posterPath(dto.poster_path())
                .overview(dto.overview())
                .build();
    }
}
