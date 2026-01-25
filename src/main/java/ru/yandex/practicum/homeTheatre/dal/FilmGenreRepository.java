package ru.yandex.practicum.homeTheatre.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.homeTheatre.model.FilmGenre;


import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class FilmGenreRepository extends ru.yandex.practicum.homeTheatre.dal.BaseRepository<FilmGenre> {
    private static final String CHECKING_CONTAINS_IDS_IN_TABLE = "SELECT COUNT(*) FROM film_genre WHERE filmId = ? AND genreId = ?";
    private static final String INSERT_QUERY_ADD_GENRE = "INSERT INTO film_genre (filmId, genreId) VALUES (?, ?)";
    private static final String GET_GENRES_BY_FILM_ID = "SELECT * FROM film_genre WHERE filmId = ?";
    private static final String UPDATE_QUERY = "UPDATE film_genre SET filmId = ?, genreId = ? WHERE id = ?";

    public FilmGenreRepository(JdbcTemplate jdbc, RowMapper<FilmGenre> mapper) {
        super(jdbc, mapper);

    }

    public void save(FilmGenre filmGenre) {
        Integer count = jdbc.queryForObject(CHECKING_CONTAINS_IDS_IN_TABLE, Integer.class, filmGenre.getFilmId(), filmGenre.getGenreId());
        if (count == 0) {
            jdbc.update(INSERT_QUERY_ADD_GENRE, filmGenre.getFilmId(), filmGenre.getGenreId());
        } else {
            System.out.println("Строка с такими значениями уже есть в таблице"); // хз какую ошибку и код
            throw new IllegalArgumentException("Строка с такими значениями уже есть в таблице");
        }

    }

    public Set<FilmGenre> getGenresByFilmId(int filmId) {
        return jdbc.query(GET_GENRES_BY_FILM_ID, (rs, rowNum) -> {
            FilmGenre filmGenre = new FilmGenre();
            filmGenre.setFilmId(rs.getInt("filmId"));
            filmGenre.setGenreId(rs.getInt("genreId"));
            return filmGenre;
        }, filmId).stream().collect(Collectors.toSet());
    }

    public void update(FilmGenre filmGenre) {
        update(
                UPDATE_QUERY,
                filmGenre.getFilmId(),
                filmGenre.getGenreId(),
                filmGenre.getId()
        );
    }
}
