package com.indicaAI.service;

import com.indicaAI.dtos.ReviewRequest;
import com.indicaAI.model.Review;
import com.indicaAI.model.User;
import com.indicaAI.model.UserMovie;
import com.indicaAI.repository.ReviewRepository;
import com.indicaAI.repository.UserMovieRepository;
import com.indicaAI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserMovieRepository userMovieRepository;
    private final UserRepository userRepository;

    public Review create(Long userMovieId, ReviewRequest request, String email) {
        User user = getUserByEmail(email);
        UserMovie userMovie = userMovieRepository.findById(userMovieId)
                .orElseThrow(() -> new RuntimeException("UserMovie não encontrado"));
        if (!userMovie.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Não é teu");
        }

        if (!userMovie.getStatus().name().equals("ASSISTIDO")) {
            throw new RuntimeException("Só pode avaliar filmes assistidos");
        }

        if (reviewRepository.findByUserMovieId(userMovieId).isPresent()) {
            throw new RuntimeException("Review já existe");
        }

        validarNota(request.nota());

        Review review = Review.builder()
                .userMovie(userMovie)
                .nota(request.nota())
                .descricao(request.descricao())
                .createdAt(LocalDateTime.now())
                .build();

        return reviewRepository.save(review);
    }

    public Review update(Long id, ReviewRequest request, String email) {

        User user = getUserByEmail(email);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review não encontrada"));

        if (!review.getUserMovie().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Não é tua");
        }

        validarNota(request.nota());

        review.setNota(request.nota());
        review.setDescricao(request.descricao());

        return reviewRepository.save(review);
    }

    public void delete(Long id, String email) {

        User user = getUserByEmail(email);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review não encontrada"));

        if (!review.getUserMovie().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Não é tua");
        }

        reviewRepository.delete(review);
    }
    public List<Review> getMyReviews(String email) {
        User user = getUserByEmail(email);
        return reviewRepository.findAllByUserMovieUserId(user.getId());
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private void validarNota(Integer nota) {
        if (nota == null || nota < 0 || nota > 10) {
            throw new RuntimeException("Nota deve ser entre 0 e 10");
        }
    }
}