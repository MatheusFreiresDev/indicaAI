package com.indicaAI.dtos;

public record TmdbMovieDto( Long id,
                            String title,
                            String poster_path,
                            String release_date,
                            String overview) {
}
