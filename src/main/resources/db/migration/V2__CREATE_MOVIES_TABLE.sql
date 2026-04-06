CREATE TABLE IF NOT EXISTS movies (
    id BIGSERIAL PRIMARY KEY,
    tmdb_id BIGINT NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    poster_path VARCHAR(255),
    overview TEXT

);