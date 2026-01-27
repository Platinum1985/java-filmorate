package ru.yandex.practicum.homeTheatre.dal.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.homeTheatre.model.Friendship;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class FriendshipRowMapper implements RowMapper<Friendship> {
    @Override
    public Friendship mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Friendship friendship = new Friendship();
        friendship.setId(resultSet.getInt("id"));
        friendship.setUserRequestId(resultSet.getInt("userId_1"));
        friendship.setUserFriendId(resultSet.getInt("userId_2"));
        return friendship;
    }
}