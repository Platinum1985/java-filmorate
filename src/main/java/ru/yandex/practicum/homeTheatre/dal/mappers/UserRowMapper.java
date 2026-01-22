package ru.yandex.practicum.homeTheatre.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.homeTheatre.model.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        Date sqlDate = resultSet.getDate("birthday");
        if (sqlDate != null) {
            LocalDate localDate = sqlDate.toLocalDate();
            user.setBirthday(localDate);
        }

        return user;
    }
}