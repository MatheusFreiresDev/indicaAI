CREATE TABLE reviews (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    movie_id BIGINT NOT NULL,
    nota INTEGER CHECK (nota BETWEEN 1 AND 5),
    descricao TEXT,
    watched_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_review_movie FOREIGN KEY (movie_id) REFERENCES movies(id)
);