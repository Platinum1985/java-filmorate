package ru.yandex.practicum.homeTheatre.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.homeTheatre.model.Film_Genre;


import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class Film_Genre_RowMapper implements RowMapper<Film_Genre> {
    @Override
    public Film_Genre mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film_Genre film_genre = new Film_Genre();
        film_genre.setId(resultSet.getInt("id"));
        film_genre.setFilmId(resultSet.getInt("filmId"));
        film_genre.setGenreId(resultSet.getInt("genreId"));
        return film_genre;
    }
}
