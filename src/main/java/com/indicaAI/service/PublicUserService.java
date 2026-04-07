package com.indicaAI.service;

import com.indicaAI.dtos.MovieStatusResponse;
import com.indicaAI.dtos.ReviewResponse;
import com.indicaAI.dtos.UserProfileResponse;
import com.indicaAI.exception.NotFoundException;
import com.indicaAI.model.User;
import com.indicaAI.model.Review;
import com.indicaAI.repository.UserRepository;
import com.indicaAI.repository.UserMovieRepository;
import com.indicaAI.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicUserService {

    private final UserRepository userRepository;
    private final UserMovieRepository userMovieRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    @Transactional(readOnly = true)
    public List<MovieStatusResponse> getUserMovies(String userName) {
        Long userId = userRepository.findByNickname(userName).get().getId();

        return userMovieRepository.findByUserId(userId)
                .stream()
                .map(um -> new MovieStatusResponse(um.getMovie().getId(), um.getStatus().name()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getUserReviews(String userName) {
        return reviewService.getReviewsByNickname(userName);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(String userName) {
        Long userId = userRepository.findByNickname(userName).get().getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        int totalWatched = userMovieRepository.countByUserIdAndStatus(userId, com.indicaAI.model.enums.StatusEnum.ASSISTIDO);

        double averageRating = reviewRepository.findAllByUserMovieUserId(userId)
                .stream()
                .mapToInt(Review::getNota)
                .average()
                .orElse(0.0);

        return new UserProfileResponse(user.getNickName(), totalWatched, averageRating);
    }

    public UserProfileResponse getUserProfileByemail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("Email invalido."));
        int totalWatched = userMovieRepository.countByUserIdAndStatus(user.getId(), com.indicaAI.model.enums.StatusEnum.ASSISTIDO);

        double averageRating = reviewRepository.findAllByUserMovieUserId(user.getId())
                .stream()
                .mapToInt(Review::getNota)
                .average()
                .orElse(0.0);

        return new UserProfileResponse(user.getNickName(), totalWatched, averageRating);
     }
}