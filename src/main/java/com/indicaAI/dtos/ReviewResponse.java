package com.indicaAI.dtos;

import com.indicaAI.model.Review;

public record ReviewResponse(
        Long id,
        Long movieId,
        Integer nota,
        String descricao
) {
    public ReviewResponse(Review review) {
        this(
                review.getId(),
                review.getUserMovie().getMovie().getId(),
                review.getNota(),
                review.getDescricao()
        );
    }
}