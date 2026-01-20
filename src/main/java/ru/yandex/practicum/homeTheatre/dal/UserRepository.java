package ru.yandex.practicum.homeTheatre.dal;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.homeTheatre.exceptions.InternalServerException;
import ru.yandex.practicum.homeTheatre.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends ru.yandex.practicum.homeTheatre.dal.BaseRepository<User> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(name, email, login, birthday)" +
            "VALUES (?, ?, ?, ?) returning id";
    private static final String UPDATE_QUERY = "UPDATE users SET name = ?, email = ?, login = ?, birthday = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<User> findById(int userId) { // +
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    public void save(User user) { // +
        int id = Math.toIntExact(insert(
                INSERT_QUERY,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday()
        ));
        user.setId(id);
    }

    public void update(User user) {
        update(
                UPDATE_QUERY,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId()
        );
    }

    public void removeUserById(int id) {
        boolean deleted = delete(DELETE_QUERY, id);
        if (!deleted) {
            throw new InternalServerException("Не удалось удалить пользователя");
        }
    }
}