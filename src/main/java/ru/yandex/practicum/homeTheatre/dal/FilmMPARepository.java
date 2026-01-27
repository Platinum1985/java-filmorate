package ru.yandex.practicum.homeTheatre.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import ru.yandex.practicum.homeTheatre.exceptions.NoFoundIdException;

import ru.yandex.practicum.homeTheatre.model.FilmMPA;


import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Repository
public class FilmMPARepository extends ru.yandex.practicum.homeTheatre.dal.BaseRepository<FilmMPA> {
    private static final String CHECKING_CONTAINS_IDS_IN_TABLE = "SELECT COUNT(*) FROM film_mpa WHERE filmId = ? AND mpaId = ?";
    private static final String INSERT_QUERY_ADD_MPA_BY_FILM = "INSERT INTO film_mpa (filmId, mpaId) VALUES (?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM film_mpa WHERE filmId = ?";
    private static final String UPDATE_QUERY = "UPDATE film_mpa SET filmId = ?, mpaId = ? WHERE id = ?"; //
    private static final String FIND_MPAS_BY_FILM_IDS = "SELECT * FROM film_mpa WHERE filmId IN (:filmIds)";

    public FilmMPARepository(JdbcTemplate jdbc, RowMapper<FilmMPA> mapper) {
        super(jdbc, mapper);

    }

    public void save(FilmMPA filmMpa) {
        Integer count = jdbc.queryForObject(CHECKING_CONTAINS_IDS_IN_TABLE, Integer.class, filmMpa.getFilmId(), filmMpa.getMpaId());
        if (count == 0) {
            jdbc.update(INSERT_QUERY_ADD_MPA_BY_FILM, filmMpa.getFilmId(), filmMpa.getMpaId());
        } else {
            System.out.println("Строка с такими значениями уже есть в таблице"); // хз какую ошибку и код
            throw new IllegalArgumentException("Строка с такими значениями уже есть в таблице");
        }

    }

    public FilmMPA findMPAByFilmId(int filmId) {
        Optional<FilmMPA> filmMpa = findOne(FIND_BY_ID_QUERY, filmId);
        if (filmMpa.isEmpty()) {
            throw new NoFoundIdException("Нет строки с таким filmId");
        } else {
            return filmMpa.get();
        }
    }

    public void update(FilmMPA filmMPA) {
        update(
                UPDATE_QUERY,
                filmMPA.getFilmId(),
                filmMPA.getMpaId(),
                filmMPA.getId()
        );
    }

    public List<FilmMPA> findMPAsByFilmIds(List<Integer> filmIds) {
        return jdbc.query(FIND_MPAS_BY_FILM_IDS, (rs, rowNum) -> {
            FilmMPA filmMPA = new FilmMPA();
            filmMPA.setFilmId(rs.getInt("filmId"));
            filmMPA.setMpaId(rs.getInt("mpaId"));
            return filmMPA;
        }, Collections.singletonMap("filmIds", filmIds));
    }
}

