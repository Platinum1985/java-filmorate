package ru.yandex.practicum.homeTheatre.dal.mappers;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.homeTheatre.model.MPA;


import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class MPARowMapper implements RowMapper<MPA> {
    @Override
    public MPA mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        MPA mpa = new MPA();
        mpa.setId(resultSet.getInt("id"));
        mpa.setRating(resultSet.getString("rating"));
        return mpa;
    }
}