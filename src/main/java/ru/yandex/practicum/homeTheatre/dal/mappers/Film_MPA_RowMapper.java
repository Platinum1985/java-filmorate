package ru.yandex.practicum.homeTheatre.dal.mappers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.homeTheatre.model.Film_MPA;


import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class Film_MPA_RowMapper implements RowMapper<Film_MPA> {@Override
public Film_MPA mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    Film_MPA filmMpa = new Film_MPA();
    filmMpa.setId(resultSet.getInt("id"));
    filmMpa.setFilmId(resultSet.getInt("filmId"));
    filmMpa.setMpaId(resultSet.getInt("mpaId"));
    return filmMpa;
}
}