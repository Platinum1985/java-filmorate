create TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL, --- убрал UNIQUE
    login VARCHAR(255) NOT NULL,
    birthday DATE
);

create TABLE IF NOT EXISTS films (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    releaseDate DATE NOT NULL,
    duration BIGINT NOT NULL,
    rate BIGINT  --- необязательное поле
);
create TABLE IF NOT EXISTS mpa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rating VARCHAR(255) NOT NULL
);
create TABLE IF NOT EXISTS genres (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

create TABLE IF NOT EXISTS likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    filmId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    FOREIGN KEY (filmId) REFERENCES films(id) ON delete CASCADE,
    FOREIGN KEY (userId) REFERENCES users(id) ON delete CASCADE
);
CREATE TABLE IF NOT EXISTS reviews (
    reviewId BIGINT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(255),
    isPositive BOOLEAN,
    userId BIGINT,
    filmId BIGINT,
    useful BIGINT DEFAULT 0,
    FOREIGN KEY (filmId) REFERENCES films(id) ON delete CASCADE,
    FOREIGN KEY (userId) REFERENCES users(id) ON delete CASCADE
);
CREATE TABLE IF NOT EXISTS reviewLikes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    isLike BOOLEAN,
    reviewId BIGINT,
    userId BIGINT,
    FOREIGN KEY (reviewId) REFERENCES reviews(reviewId) ON delete CASCADE,
    FOREIGN KEY (userId) REFERENCES users(id) ON delete CASCADE
);

create TABLE IF NOT EXISTS friendships (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userId_1 BIGINT NOT NULL,
    userId_2 BIGINT NOT NULL,
    FOREIGN KEY (userId_1) REFERENCES users(id) ON delete CASCADE,
    FOREIGN KEY (userId_2) REFERENCES users(id) ON delete CASCADE
);
create TABLE IF NOT EXISTS film_mpa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    filmId BIGINT NOT NULL,
    mpaId BIGINT NOT NULL,
    FOREIGN KEY (filmId) REFERENCES films(id) ON delete CASCADE,
    FOREIGN KEY (mpaId) REFERENCES mpa(id) ON delete CASCADE
);

create TABLE IF NOT EXISTS film_genre (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    filmId BIGINT NOT NULL,
    genreId BIGINT NOT NULL,
    FOREIGN KEY (filmId) REFERENCES films(id) ON delete CASCADE,
    FOREIGN KEY (genreId) REFERENCES genres(id) ON delete CASCADE
);
