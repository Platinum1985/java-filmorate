package ru.yandex.practicum.homeTheatre.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.homeTheatre.model.MPA;

import java.util.List;
import java.util.Optional;

@Repository
public class MPARepository extends ru.yandex.practicum.homeTheatre.dal.BaseRepository<MPA> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa WHERE id = ?";

    public MPARepository(JdbcTemplate jdbc, RowMapper<MPA> mapper) {
        super(jdbc, mapper);

    }

    public List<MPA> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<MPA> findMPAById(int id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }
}