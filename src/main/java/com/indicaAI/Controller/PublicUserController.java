package com.indicaAI.Controller;

import com.indicaAI.dtos.MovieStatusResponse;
import com.indicaAI.dtos.ReviewResponse;
import com.indicaAI.dtos.UserProfileResponse;
import com.indicaAI.service.PublicUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class PublicUserController {


    @Autowired
    private final PublicUserService publicUserService;
    @GetMapping("/{username}/movies")
    public ResponseEntity<List<MovieStatusResponse>> getUserMovies(@PathVariable String username) {
        List<MovieStatusResponse> movies = publicUserService.getUserMovies(username);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{username}/reviews")
    public ResponseEntity<List<ReviewResponse>> getUserReviews(@PathVariable String username) {
        List<ReviewResponse> reviews = publicUserService.getUserReviews(username);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{username}/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable String username) {
        UserProfileResponse profile = publicUserService.getUserProfile(username);
        return ResponseEntity.ok(profile);
    }
    @GetMapping("/me/profile")
    public ResponseEntity<UserProfileResponse> getMyProfile(@AuthenticationPrincipal UserDetails user) {
        String email = user.getUsername(); // email do token
        System.out.println(email);
       return ResponseEntity.ok(publicUserService.getUserProfileByemail(email));
    }
}