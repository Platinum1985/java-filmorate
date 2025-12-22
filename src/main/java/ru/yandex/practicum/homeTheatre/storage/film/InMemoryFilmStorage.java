package ru.yandex.practicum.homeTheatre.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.homeTheatre.exceptions.NoFoundIdException;
import ru.yandex.practicum.homeTheatre.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> allFilms = new HashMap<>();

    @Override
    public void addFilm(Film film) {
        film.setId(getNextId());
        log.info("Фильму {} присвоен id {}", film, film.getId());
        allFilms.put(film.getId(), film);
    }

    @Override
    public void removeFilm(int id) {
        allFilms.remove(id);
    }

    @Override
    public Film getFilm(int id) {
        if (!existFilmById(id)) {
            log.error("Фильм с таким ID {} не найден", id);
            throw new NoFoundIdException("Пост с id = " + id + " не найден");
        }
        return allFilms.get(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return allFilms.values();
    }

    @Override
    public void updateFilm(Film film) {
        if (!existsFilm(film)) {
            log.error("Фильм с ID {} не найден", film.getId());
            throw new NoFoundIdException("Пост с id = " + film.getId() + " не найден");
        }
        allFilms.put(film.getId(), film);
    }

    @Override
    public void addLike(int filmId, int userId) {
        if (!existFilmById(filmId)) {
            log.error("Фильм с таким Id {} не найден", filmId);
            throw new NoFoundIdException("Получены некорректные id фильма или пользователя");
        }
        allFilms.get(filmId).getLikes().add(userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        if (!existFilmById(filmId)) {
            log.error("Film с таким Id {} для удаления Like не найден", filmId);
            throw new NoFoundIdException("Фильм с id = " + filmId + " не найден");
        }
        if (!getFilm(filmId).getLikes().contains(userId)) {
            throw new NoFoundIdException("В лайках нет польз id = " + userId);
        }
        allFilms.get(filmId).getLikes().remove(userId);
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        return allFilms.values().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    private int getNextId() {
        int currentMaxId = Math.toIntExact(allFilms.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0));
        int nextId = ++currentMaxId;
        return nextId;
    }

    public boolean existsFilm(Film film) {
        return allFilms.containsKey(film.getId());
    }

    public boolean existFilmById(int id) {
        return allFilms.containsKey(id);
    }
}