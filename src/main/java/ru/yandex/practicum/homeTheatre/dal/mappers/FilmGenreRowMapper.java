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
        FilmGenre filmgenre = new FilmGenre();
        filmgenre.setId(resultSet.getInt("id"));
        filmgenre.setFilmId(resultSet.getInt("filmId"));
        filmgenre.setGenreId(resultSet.getInt("genreId"));
        return filmgenre;
    }
}
