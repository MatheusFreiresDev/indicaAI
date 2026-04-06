package com.indicaAI.dtos;

public record UserMovieRequest(Long tmdbId, com.indicaAI.model.enums.StatusEnum status) {}