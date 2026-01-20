package ru.yandex.practicum.homeTheatre.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.homeTheatre.exceptions.InternalServerException;
import ru.yandex.practicum.homeTheatre.model.Like;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Repository
public class LikeRepository extends ru.yandex.practicum.homeTheatre.dal.BaseRepository<Like> {
    private static final String FIND_LIKES_BY_ID_FILM_QUERY = "SELECT userId FROM likes WHERE filmId = ?";
    private static final String CHECKING_CONTAINS_IDS_IN_TABLE = "SELECT COUNT(*) FROM likes WHERE" +
            " filmId = ? AND userId = ?";
    private static final String INSERT_QUERY_ADD_LIKE = "INSERT INTO likes (filmId, userId) VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM likes WHERE filmId = ? AND userId = ?";
    private static final String FIND_MOST_LIKED_FILMS_QUERY = "SELECT filmId, COUNT(filmId) AS count " +
            "FROM likes " +
            "GROUP BY filmId " +
            "ORDER BY count DESC " +
            "LIMIT ?";

    public LikeRepository(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);

    }

    public Set<Integer> getUserIdsByFilmId(int filmId) {
        return jdbc.query(FIND_LIKES_BY_ID_FILM_QUERY, (rs, rowNum) -> rs.getInt("userId"), filmId)
                .stream()
                .collect(Collectors.toSet());
    }

    public void addLike(int filmId, int userId) {
        Integer count = jdbc.queryForObject(CHECKING_CONTAINS_IDS_IN_TABLE, Integer.class, filmId, userId);
        if (count == 0) {
            jdbc.update(INSERT_QUERY_ADD_LIKE, filmId, userId);
        } else {
            System.out.println("Строка с такими значениями уже есть в таблице");
        }
    }

    public void deleteLike(int filmId, int userId) {
        boolean deleted = delete(DELETE_LIKE_QUERY, filmId, userId);
        if (!deleted) {
            throw new InternalServerException("Не удалось удалить пользователя");
        }
    }

    public List<Integer> getPopularFilmIds(int count) {
        return jdbc.query(FIND_MOST_LIKED_FILMS_QUERY, (rs, rowNum) -> rs.getInt("filmId"), count);
    }
}
