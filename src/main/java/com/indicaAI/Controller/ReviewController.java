package com.indicaAI.Controller;

import com.indicaAI.dtos.ReviewRequest;
import com.indicaAI.dtos.ReviewResponse;
import com.indicaAI.model.Review;
import com.indicaAI.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{userMovieId}")
    public ResponseEntity<ReviewResponse> create(
            @PathVariable Long userMovieId,
            @RequestBody @Valid ReviewRequest request,
            @AuthenticationPrincipal UserDetails user
    ) {
        Review review = reviewService.create(userMovieId, request, user.getUsername());
        return ResponseEntity.ok(new ReviewResponse(review));
    }

    @GetMapping("/me")
    public ResponseEntity<List<ReviewResponse>> getMyReviews(
            @AuthenticationPrincipal UserDetails user
    ) {
        List<ReviewResponse> response = reviewService.getMyReviews(user.getUsername())
                .stream()
                .map(ReviewResponse::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReviewResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ReviewRequest request,
            @AuthenticationPrincipal UserDetails user
    ) {
        Review review = reviewService.update(id, request, user.getUsername());
        return ResponseEntity.ok(new ReviewResponse(review));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user
    ) {
        reviewService.delete(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReviewResponse>> reviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }
    @GetMapping("/{nickname}")
    public List<ReviewResponse> getUserReviews(@PathVariable String nickname) {

        return reviewService.getReviewsByNickname(nickname);
    }
}