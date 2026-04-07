package com.indicaAI.service;
import com.indicaAI.dtos.ReviewRequest;
import com.indicaAI.dtos.ReviewResponse;
import com.indicaAI.exception.AlreadyExistException;
import com.indicaAI.exception.ForbiddenException;
import com.indicaAI.exception.InvalidDataException;
import com.indicaAI.exception.NotFoundException;
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
                .orElseThrow(() -> new NotFoundException("UserMovie não encontrado"));
        if (!userMovie.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Não é teu");
        }

        if (!(userMovie.getStatus() == com.indicaAI.model.enums.StatusEnum.ASSISTIDO)) {
            throw new InvalidDataException("Só pode avaliar filmes assistidos");
        }

        if (reviewRepository.findByUserMovieIdOrderByCreatedAtDesc(userMovieId).isPresent()) {
            throw new AlreadyExistException("Review já existe");
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
                .orElseThrow(() -> new NotFoundException("Review não encontrada"));

        if (!review.getUserMovie().getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Não é tua");
        }

        validarNota(request.nota());

        review.setNota(request.nota());
        review.setDescricao(request.descricao());

        return reviewRepository.save(review);
    }

    public void delete(Long id, String email) {

        User user = getUserByEmail(email);

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review não encontrada"));

        if (!review.getUserMovie().getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Não é tua");
        }

        reviewRepository.delete(review);
    }
    public List<Review> getMyReviews(String email) {
        User user = getUserByEmail(email);
        return reviewRepository.findAllByUserMovieUserId(user.getId());
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    private void validarNota(Integer nota) {
        if (nota == null || nota < 0 || nota > 10) {
            throw new InvalidDataException("Nota deve ser entre 0 e 10");
        }
    }

    public List<ReviewResponse> getAllReviews() {
        List<ReviewResponse> reviews = reviewRepository.findAllByOrderByCreatedAtDesc().stream().map(ReviewWrapper::reviewToReviewResponse).toList();
        return reviews;
    }
}