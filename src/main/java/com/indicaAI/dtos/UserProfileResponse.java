package com.indicaAI.dtos;

public record UserProfileResponse(
        String username,
        int totalWatched,
        double averageRating
) {}