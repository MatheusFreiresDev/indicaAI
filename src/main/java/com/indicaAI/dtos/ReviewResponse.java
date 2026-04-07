package com.indicaAI.dtos;

import com.indicaAI.model.Review;

public record ReviewResponse(
        Long id,
        Long movieId,
        String username,
        Integer nota,
        String descricao
) {
    public ReviewResponse(Review review) {
        this(
                review.getId(),
                review.getUserMovie().getMovie().getId(),
                review.getUserMovie().getUser().getNickName(),
                review.getNota(),
                review.getDescricao()
        );
    }
}