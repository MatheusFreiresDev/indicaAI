package com.indicaAI.Controller;

import com.indicaAI.dtos.UserMovieRequest;
import com.indicaAI.dtos.UserMovieResponse;
import com.indicaAI.service.UserMovieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-movies")
public class UserMovieController {

    private final UserMovieService userMovieService;

    public UserMovieController(UserMovieService userMovieService) {
        this.userMovieService = userMovieService;
    }

    @PostMapping
    public ResponseEntity<UserMovieResponse> addMovie(
            @RequestBody @Valid UserMovieRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMovieService.addMovie(request, userDetails.getUsername()));
    }

    @GetMapping("/me")
    public ResponseEntity<List<UserMovieResponse>> getMyList(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userMovieService.getMyList(userDetails.getUsername()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserMovieResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody com.indicaAI.model.enums.StatusEnum status,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userMovieService.updateStatus(id, status, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        userMovieService.deleteMovie(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}