package ru.yandex.practicum.homeTheatre.dal.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.homeTheatre.model.Like;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LikeRowMapper implements RowMapper<Like> {
    @Override
    public Like mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Like like = new Like();
        like.setId(resultSet.getInt("id"));
        like.setFilmId(resultSet.getInt("filmId"));
        like.setUserId(resultSet.getInt("userId"));
        return like;
    }
}
