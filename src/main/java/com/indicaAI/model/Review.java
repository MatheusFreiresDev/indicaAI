package com.indicaAI.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_movie_id", nullable = false, unique = true)
    private UserMovie userMovie;

    private Integer nota;

    private String descricao;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}