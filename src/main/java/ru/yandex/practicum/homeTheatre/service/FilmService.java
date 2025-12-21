package ru.yandex.practicum.homeTheatre.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.homeTheatre.model.Film;
import ru.yandex.practicum.homeTheatre.storage.film.FilmStorage;

import java.util.Collection;


@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    // private final Film user;

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilm(int id) {
        return filmStorage.getFilm(id);
    }

    public void addFilm(Film film) { //+
        filmStorage.addFilm(film);
    }

    public void removeFilm(int id) {
        filmStorage.removeFilm(id);
    }

    public void updateFilm(Film film) {
        filmStorage.updateFilm(film);
    }

    public void addLike(int filmId, int userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        filmStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    public boolean existFilm(Film film) {
        return filmStorage.existsFilm(film);
    }

    public boolean existFilmById(int id) {
        return filmStorage.existFilmById(id);
    }
}


