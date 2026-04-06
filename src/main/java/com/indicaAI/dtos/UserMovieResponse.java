package com.indicaAI.dtos;

public record UserMovieResponse(
        Long id,
        String movieTitle,
        String posterPath,
        com.indicaAI.model.enums.StatusEnum status
) {}