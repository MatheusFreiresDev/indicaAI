package com.indicaAI.dtos;

public record RecommendationResponse(
        Long tmdbId,
        String title,
        String posterPath,
        String reason
) {}