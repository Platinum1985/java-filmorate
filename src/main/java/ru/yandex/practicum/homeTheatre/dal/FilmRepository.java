package ru.yandex.practicum.homeTheatre.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.homeTheatre.exceptions.InternalServerException;
import ru.yandex.practicum.homeTheatre.exceptions.NoFoundIdException;
import ru.yandex.practicum.homeTheatre.model.Film;


import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends ru.yandex.practicum.homeTheatre.dal.BaseRepository<Film> {
    private static final String FIND_FILM_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String DELETE_FILM_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String UPDATE_FILM_BY_ID_QUERY = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ? WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, releaseDate, duration)" +
            "VALUES (?, ?, ?, ?)"; // returning id; так h2 не принимает

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);

    }

    public Film getFilmById(int id) {
        Optional<Film> film = findOne(FIND_FILM_BY_ID_QUERY, id);
        if (film.isEmpty()) {
            throw new NoFoundIdException("Фильм с таким id не найден");
        } else {
            return film.get();
        }
    }

    public List<Film> findAll() {
        List<Film> films = findMany(FIND_ALL_QUERY);
        if (films.isEmpty()) {
            throw new NoFoundIdException("Список фильмов пуст");
        } else {
            return films;
        }
    }

    public void removeFilmById(int id) {
        boolean deleted = delete(DELETE_FILM_QUERY, id);
        if (!deleted) {
            throw new InternalServerException("Не удалось удалить пользователя");
        }
    }

    public void update(Film film) {
        update(
                UPDATE_FILM_BY_ID_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getId()
        );
    }

    public void save(Film film) { // + может вместо void надо Film? нет!
        int id = Math.toIntExact(insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration()
        ));
        film.setId(id); // после сохранения в таблице присваивает PK id для film
    }
}