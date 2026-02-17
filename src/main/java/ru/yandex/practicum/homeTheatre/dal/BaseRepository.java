package ru.yandex.practicum.homeTheatre.dal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.homeTheatre.exceptions.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class BaseRepository<T> {
    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;

    protected Optional<T> findOne(String query, Object... params) {
        try {
            T result = jdbc.queryForObject(query, mapper, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    protected List<T> findMany(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    protected boolean delete(String query, int id) {
        try {
            int rowsDeleted = jdbc.update(query, id);
            return rowsDeleted > 0;
        } catch (Exception e) {
            // Логирование исключения, если необходимо
            throw new DatabaseException("Произошла ошибка при удалении данных");
        }
    }

    protected boolean delete(String query, Object... params) { // можно было сделать аргументы переменной длины
        try {
            int rowsDeleted = jdbc.update(query, params);
            return rowsDeleted > 0;
        } catch (Exception e) {
            // Логирование исключения, если необходимо
            throw new DatabaseException("Произошла ошибка при удалении данных");
        }
    }

    protected void update(String query, Object... params) {
        int rowsUpdated = jdbc.update(query, params);
        if (rowsUpdated > 0) {
            log.info("Данные базы успешно обновлены");
        } else {
            log.debug("Обновления данных не произошло или возникла ошибка");
        }
    }

    protected long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);

        // Возвращаем id нового пользователя
        if (id != null) {
            return id;
        } else {
            throw new DatabaseException("Не удалось сохранить данные");
        }
    }
}
