package ru.yandex.practicum.homeTheatre.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.homeTheatre.model.Film_Genre;


import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class Film_Genre_Repository extends ru.yandex.practicum.homeTheatre.dal.BaseRepository<Film_Genre> {
    private static final String CHECKING_CONTAINS_IDS_IN_TABLE = "SELECT COUNT(*) FROM film_genre WHERE filmId = ? AND genreId = ?";
    private static final String INSERT_QUERY_ADD_GENRE = "INSERT INTO film_genre (filmId, genreId) VALUES (?, ?)";
    private static final String GET_GENRES_BY_FILM_ID = "SELECT genreId FROM film_genre WHERE filmId = ?";
    private static final String UPDATE_QUERY = "UPDATE film_genre SET filmId = ?, genreId = ? WHERE id = ?";

    public Film_Genre_Repository(JdbcTemplate jdbc, RowMapper<Film_Genre> mapper) {
        super(jdbc, mapper);

    }

    public void save(Film_Genre film_genre) {
        Integer count = jdbc.queryForObject(CHECKING_CONTAINS_IDS_IN_TABLE, Integer.class, film_genre.getFilmId(), film_genre.getGenreId());
        if (count == 0) {
            jdbc.update(INSERT_QUERY_ADD_GENRE, film_genre.getFilmId(), film_genre.getGenreId());
        } else {
            System.out.println("Строка с такими значениями уже есть в таблице"); // хз какую ошибку и код
            throw new IllegalArgumentException("Строка с такими значениями уже есть в таблице");
        }

    }

    public Set<Integer> getGenresByFilmId(int filmId) {
        return jdbc.query(GET_GENRES_BY_FILM_ID, (rs, rowNum) -> rs.getInt("genreId"), filmId)
                .stream()
                .collect(Collectors.toSet());
    }

    public void update(Film_Genre filmGenre) {
        update(
                UPDATE_QUERY,
                filmGenre.getFilmId(),
                filmGenre.getGenreId(),
                filmGenre.getId()
        );
    }
}
