package ru.yandex.practicum.homeTheatre.dal;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.homeTheatre.exceptions.InternalServerException;
import ru.yandex.practicum.homeTheatre.model.Friendship;

import java.util.Set;
import java.util.stream.Collectors;


@Repository
public class FriendshipRepository extends ru.yandex.practicum.homeTheatre.dal.BaseRepository<Friendship> {
    private static final String CHECKING_CONTAINS_USER_IDS_IN_TABLE = "SELECT COUNT(*) FROM friendships WHERE userId_1 = ? AND userId_2 = ?";
    private static final String INSERT_QUERY_ADD_FRIENDSHIP = "INSERT INTO friendships (userId_1, userId_2) VALUES (?, ?)";
    private static final String GET_FRIENDS_BY_USERID = "SELECT userId_1 FROM friendships WHERE userId_2 = ?"; // ---
    private static final String DELETE_QUERY = "DELETE FROM friendships WHERE userId_1 = ? AND userId_2 = ?";
    private static final String FIND_COMMON_FRIENDS_QUERY =
            "SELECT t1.userId_1 AS common_friend " +  // ---2 na 1
                    "FROM friendships t1 " +
                    "JOIN friendships t2 ON t1.userId_1 = t2.userId_1 " + // ---1 na 2
                    "WHERE t1.userId_2 = ? " + // --- 1 na 2
                    "AND t2.userId_2 = ?"; // --- 1 na 2

    public FriendshipRepository(JdbcTemplate jdbc, RowMapper<Friendship> mapper) {
        super(jdbc, mapper);

    }

    public void save(Friendship friendship) {
        Integer count = jdbc.queryForObject(CHECKING_CONTAINS_USER_IDS_IN_TABLE, Integer.class, friendship.getUserId1(), friendship.getUserId2());
        if (count == 0) {
            jdbc.update(INSERT_QUERY_ADD_FRIENDSHIP, friendship.getUserId1(), friendship.getUserId2());
        } else {
            System.out.println("Строка с такими значениями уже есть в таблице"); // хз какую ошибку и код

        }

    }

    public Set<Integer> getFriendsById(int id) {
        return jdbc.query(GET_FRIENDS_BY_USERID, (rs, rowNum) -> rs.getInt("userId_1"), id) // ---
                .stream()
                .collect(Collectors.toSet());
    }

    public void deleteFriendById(Friendship friendship) {
        boolean deleted = delete(DELETE_QUERY, friendship.getUserId1(), friendship.getUserId2());
        if (!deleted) {
            throw new InternalServerException("Не удалось удалить пользователя");
        }
    }

    public Set<Integer> findMutualFriends(int id1, int id2) {
        return jdbc.query(FIND_COMMON_FRIENDS_QUERY, (rs, rowNum) -> rs.getInt("common_friend"), id1, id2)
                .stream()
                .collect(Collectors.toSet());
    }
}


