package ru.yandex.practicum.homeTheatre.storage.film;

import ru.yandex.practicum.homeTheatre.model.Film;

import java.util.Collection;

public interface FilmStorage {
    public Collection<Film> getAllFilms();

    public void addFilm(Film film);

    public void updateFilm(Film film);

    public void addLike(int filmId, int userId);

    public void deleteLike(int filmId, int userId);

    public Collection<Film> getPopularFilms(int count);

    public Film getFilm(int id);

    public void removeFilm(int id);

    public boolean existsFilm(Film film);

    public boolean existFilmById(int id);
}
