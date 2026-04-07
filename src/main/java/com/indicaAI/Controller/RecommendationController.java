package com.indicaAI.Controller;

import com.indicaAI.dtos.RecommendationResponse;
import com.indicaAI.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/me")
    public ResponseEntity<List<RecommendationResponse>> recommend(
            @AuthenticationPrincipal UserDetails user
    ) {
        List<RecommendationResponse> recommendations = recommendationService.recommend(user.getUsername());
        return ResponseEntity.ok(recommendations);
    }
}