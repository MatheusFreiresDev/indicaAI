package com.indicaAI.service;

import com.indicaAI.dtos.ReviewRequest;
import com.indicaAI.dtos.ReviewResponse;
import com.indicaAI.model.Review;
import com.indicaAI.model.UserMovie;
import org.springframework.stereotype.Component;

@Component
public class ReviewWrapper {

    public static ReviewResponse reviewToReviewResponse(Review review) {
        if (review == null) return null;
        return new ReviewResponse(review);
    }

    public Review reviewRequestToReview(ReviewRequest request, UserMovie userMovie) {
        if (request == null || userMovie == null) return null;

        return Review.builder()
                .userMovie(userMovie)
                .nota(request.nota())
                .descricao(request.descricao())
                .build();
    }
}