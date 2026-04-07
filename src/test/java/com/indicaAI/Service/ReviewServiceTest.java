package com.indicaAI.Service;
import com.indicaAI.dtos.ReviewRequest;
import com.indicaAI.exception.InvalidDataException;
import com.indicaAI.model.Review;
import com.indicaAI.model.User;
import com.indicaAI.model.UserMovie;
import com.indicaAI.model.enums.StatusEnum;
import com.indicaAI.repository.ReviewRepository;
import com.indicaAI.repository.UserMovieRepository;
import com.indicaAI.repository.UserRepository;
import com.indicaAI.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserMovieRepository userMovieRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void shouldCreateReview() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        UserMovie userMovie = new UserMovie();
        userMovie.setUser(user);
        userMovie.setStatus(StatusEnum.ASSISTIDO);

        ReviewRequest request = new ReviewRequest(8, "Muito bom");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(userMovieRepository.findById(1L))
                .thenReturn(Optional.of(userMovie));

        when(reviewRepository.findByUserMovieIdOrderByCreatedAtDesc(1L))
                .thenReturn(Optional.empty());

        when(reviewRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Review review = reviewService.create(1L, request, "test@gmail.com");

        assertEquals(8, review.getNota());
        verify(reviewRepository).save(any());
    }

    @Test
    void shouldThrowWhenNotWatched() {
        User user = new User();
        user.setId(1L);

        UserMovie userMovie = new UserMovie();
        userMovie.setUser(user);
        userMovie.setStatus(StatusEnum.QUERO_VER);

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));

        when(userMovieRepository.findById(any()))
                .thenReturn(Optional.of(userMovie));

        assertThrows(InvalidDataException.class, () ->
                reviewService.create(1L, new ReviewRequest(8, "ok"), "test@gmail.com")
        );
    }
}