CREATE TABLE reviews (
    id BIGSERIAL PRIMARY KEY,

    user_movie_id BIGINT NOT NULL UNIQUE,

    nota INTEGER CHECK (nota BETWEEN 0 AND 10),
    descricao TEXT,

    created_at TIMESTAMP DEFAULT NOW(),

    CONSTRAINT fk_review_user_movie
        FOREIGN KEY (user_movie_id)
        REFERENCES user_movie(id)
        ON DELETE CASCADE
);