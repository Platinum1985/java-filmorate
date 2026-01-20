package ru.yandex.practicum.homeTheatre.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.homeTheatre.model.FilmMPA;


import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmMPARowMapper implements RowMapper<FilmMPA> {
    @Override
    public FilmMPA mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        FilmMPA filmMpa = new FilmMPA();
        filmMpa.setId(resultSet.getInt("id"));
        filmMpa.setFilmId(resultSet.getInt("filmId"));
        filmMpa.setMpaId(resultSet.getInt("mpaId"));
        return filmMpa;
    }
}