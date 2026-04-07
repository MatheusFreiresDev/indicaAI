package com.indicaAI.service;

import com.indicaAI.dtos.TmdbMovieDto;
import com.indicaAI.dtos.UserMovieRequest;
import com.indicaAI.dtos.UserMovieResponse;
import com.indicaAI.model.Movie;
import com.indicaAI.model.User;
import com.indicaAI.model.UserMovie;
import com.indicaAI.model.enums.StatusEnum;
import com.indicaAI.repository.UserMovieRepository;
import com.indicaAI.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserMovieServiceTest {

    @Mock
    private UserMovieRepository userMovieRepository;

    @Mock
    private MovieService movieService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TmbdService tmbdService;

    @InjectMocks
    private UserMovieService userMovieService;

    @Test
    void shouldAddMovieSuccessfully() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        UserMovieRequest request = new UserMovieRequest(123L, StatusEnum.QUERO_VER);

        TmdbMovieDto tmdbDto = new TmdbMovieDto();

        Movie movie = new Movie();
        movie.setId(10L);
        movie.setTitle("Batman");

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(tmbdService.searchFilmById(123L))
                .thenReturn(tmdbDto);

        when(movieService.saveMovie(tmdbDto))
                .thenReturn(movie);

        when(userMovieRepository.existsByUserIdAndMovieId(1L, 10L))
                .thenReturn(false);

        UserMovieResponse response = userMovieService.addMovie(request, "test@gmail.com");

        assertNotNull(response);
        assertEquals("Batman", response.movieTitle());
        verify(userMovieRepository).save(any(UserMovie.class));
    }

    @Test
    void shouldThrowWhenMovieAlreadyExists() {
        User user = new User();
        user.setId(1L);

        Movie movie = new Movie();
        movie.setId(10L);

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));

        when(tmbdService.searchFilmById(any()))
                .thenReturn(new TmdbMovieDto());

        when(movieService.saveMovie(any()))
                .thenReturn(movie);

        when(userMovieRepository.existsByUserIdAndMovieId(1L, 10L))
                .thenReturn(true);

        assertThrows(RuntimeException.class, () ->
                userMovieService.addMovie(new UserMovieRequest(1L, StatusEnum.QUERO_VER), "test@gmail.com")
        );
    }
}