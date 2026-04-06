CREATE TABLE user_movie (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,
    movie_id BIGINT NOT NULL,

    status VARCHAR(20) NOT NULL,
    watched_at TIMESTAMP,

    CONSTRAINT fk_user_movie_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_movie_movie
        FOREIGN KEY (movie_id)
        REFERENCES movies(id)
        ON DELETE CASCADE,

    CONSTRAINT unique_user_movie
        UNIQUE (user_id, movie_id)
);

CREATE INDEX idx_user_movie_user ON user_movie(user_id);
CREATE INDEX idx_user_movie_movie ON user_movie(movie_id);