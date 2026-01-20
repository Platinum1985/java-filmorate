package ru.yandex.practicum.homeTheatre.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.homeTheatre.model.FilmGenre;


import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmGenreRowMapper implements RowMapper<FilmGenre> {
    @Override
    public FilmGenre mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        FilmGenre film_genre = new FilmGenre();
        film_genre.setId(resultSet.getInt("id"));
        film_genre.setFilmId(resultSet.getInt("filmId"));
        film_genre.setGenreId(resultSet.getInt("genreId"));
        return film_genre;
    }
}
