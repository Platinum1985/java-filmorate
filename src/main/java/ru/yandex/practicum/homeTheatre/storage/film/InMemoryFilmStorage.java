package ru.yandex.practicum.homeTheatre.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
        return allFilms.get(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return allFilms.values();
    }

    @Override
    public void updateFilm(Film film) {
        allFilms.put(film.getId(), film);
    }

    @Override
    public void addLike(int filmId, int userId) {
        allFilms.get(filmId).getLikes().add(userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
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