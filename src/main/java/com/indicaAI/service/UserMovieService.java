package com.indicaAI.service;

import com.indicaAI.dtos.TmdbMovieDto;
import com.indicaAI.dtos.UserMovieRequest;
import com.indicaAI.dtos.UserMovieResponse;
import com.indicaAI.model.Movie;
import com.indicaAI.model.User;
import com.indicaAI.model.UserMovie;
import com.indicaAI.repository.UserMovieRepository;
import com.indicaAI.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMovieService {

    private final UserMovieRepository userMovieRepository;
    private final MovieService movieService;
    private final UserRepository userRepository;
    private final TmbdService tmbdService;

    public UserMovieService(UserMovieRepository userMovieRepository, MovieService movieService,
                            UserRepository userRepository, TmbdService tmbdService) {
        this.userMovieRepository = userMovieRepository;
        this.movieService = movieService;
        this.userRepository = userRepository;
        this.tmbdService = tmbdService;
    }

    public UserMovieResponse addMovie(UserMovieRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        // Busca filme no TMDB e salva se não existir
        TmdbMovieDto tmdbMovie = tmbdService.searchFilmById(request.tmdbId());
        Movie movie = movieService.saveMovie(tmdbMovie);

        if (userMovieRepository.existsByUserIdAndMovieId(user.getId(), movie.getId())) {
            throw new RuntimeException("Filme já está na sua lista.");
        }

        UserMovie userMovie = UserMovie.builder()
                .user(user)
                .movie(movie)
                .status(request.status())
                .build();

        userMovieRepository.save(userMovie);
        return toResponse(userMovie);
    }

    public List<UserMovieResponse> getMyList(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        return userMovieRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UserMovieResponse updateStatus(Long id, com.indicaAI.model.enums.StatusEnum status, String userEmail) {
        UserMovie userMovie = userMovieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado."));

        if (!userMovie.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Sem permissão.");
        }

        userMovie.setStatus(status);
        userMovieRepository.save(userMovie);
        return toResponse(userMovie);
    }

    public void deleteMovie(Long id, String userEmail) {
        UserMovie userMovie = userMovieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado."));

        if (!userMovie.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Sem permissão.");
        }

        userMovieRepository.delete(userMovie);
    }

    private UserMovieResponse toResponse(UserMovie userMovie) {
        return new UserMovieResponse(
                userMovie.getId(),
                userMovie.getMovie().getTitle(),
                userMovie.getMovie().getPosterPath(),
                userMovie.getStatus()
        );
    }
}