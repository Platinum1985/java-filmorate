package ru.yandex.practicum.homeTheatre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.homeTheatre.dal.*;
import ru.yandex.practicum.homeTheatre.exceptions.NoFoundIdException;
import ru.yandex.practicum.homeTheatre.exceptions.ValidationException;
import ru.yandex.practicum.homeTheatre.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final LikeRepository likeRepository;
    private final FilmMPARepository filmMpaRepository;
    private final FilmGenreRepository filmGenreRepository;
    private final FilmRepository filmRepository;
    private final MPARepository mpaRepository;
    private final GenreRepository genreRepository;

    public List<Film> getAllFilms() {
        List<Film> films = filmRepository.findAll();
        for (Film film : films) {
            int filmId = film.getId();
            List<FilmGenre> filmGenres = filmGenreRepository.getGenresByFilmId(filmId).stream()
                    .collect(Collectors.toList());
            FilmMPA filmMPA = filmMpaRepository.findMPAByFilmId(filmId);
            MPA mpa = mpaRepository.findMPAById(filmMPA.getMpaId()).get(); // -------
            film.setMpa(mpa);
            System.out.println(film.getMpa() + "++++++++++++");
            List<Genre> genres = new ArrayList<>();
            for (FilmGenre filmGenre : filmGenres) {
                Genre genre = genreRepository.findGenreById(filmGenre.getGenreId()).get();
                genres.add(genre);
            }
            film.setGenres(genres);
            film.setLikes(likeRepository.getUserIdsByFilmId(filmId));
        }
        return films;
    }

    public Film getFilm(int filmId) {
        Film film = filmRepository.getFilmById(filmId);
        List<FilmGenre> filmGenres = filmGenreRepository.getGenresByFilmId(filmId).stream()
                .collect(Collectors.toList());
        FilmMPA filmMPA = filmMpaRepository.findMPAByFilmId(filmId);
        MPA mpa = mpaRepository.findMPAById(filmMPA.getMpaId()).get(); // -------
        film.setMpa(mpa);
        System.out.println(film.getMpa() + "++++++++++++");
        List<Genre> genres = new ArrayList<>();
        for (FilmGenre filmGenre : filmGenres) {
            Genre genre = genreRepository.findGenreById(filmGenre.getGenreId()).get();
            genres.add(genre);
        }
        film.setGenres(genres);
        film.setLikes(likeRepository.getUserIdsByFilmId(filmId));
        return film;
    }

    public void addFilm(Film film) { // ++
        MPA mpa = film.getMpa();
        validateMpa(mpa);
        validateGenres(film.getGenres());
        if (!validateFilm(film)) {
            log.error("некорректно заполнены поля");
            throw new ValidationException("некорректно заполнены поля");

        } else {
            log.info("Валидация фильма {} прошла успешно", film);
            log.info("Фильму {} присвоен id {}", film, film.getId());
            filmRepository.save(film);
            List<Genre> g = film.getGenres();
            LinkedHashSet<Genre> uniqueGenres = new LinkedHashSet<>(g);
            List<Genre> genres = new ArrayList<>(uniqueGenres);
            for (Genre genre : genres) {
                FilmGenre filmGenre = new FilmGenre();
                filmGenre.setGenreId(genre.getId());
                filmGenre.setFilmId(film.getId());
                filmGenreRepository.save(filmGenre);
            }
            FilmMPA filmMPA = new FilmMPA();
            filmMPA.setMpaId(mpa.getId());
            filmMPA.setFilmId(film.getId());
            filmMpaRepository.save(filmMPA);

        }
    }

    public void removeFilm(int id) { // +
        filmRepository.removeFilmById(id);
    }

    public void updateFilm(Film film) { // ++
        checkFilmId(film.getId());
        validateMpa(film.getMpa());
        if (!validateFilm(film)) {
            log.error("Некорректно заполнены поля фильма {}", film);
            throw new ValidationException("некорректно заполнены поля");
        } else {
            MPA mpa = film.getMpa();

            FilmMPA filmMpa = new FilmMPA();
            filmMpa.setMpaId(mpa.getId());
            filmMpa.setFilmId(film.getId());
            filmMpa.setId(filmMpaRepository.findMPAByFilmId(film.getId()).getId());
            filmMpaRepository.update(filmMpa);
            filmRepository.update(film);
        }
    }

    public void deleteLike(int filmId, int userId) {
        likeRepository.deleteLike(filmId, userId);
    }


    public List<Film> getPopularFilms(int count) {
        List<Integer> idFilms = likeRepository.getPopularFilmIds(count);
        List<Film> films = new ArrayList<>();
        for (int id : idFilms) {
            films.add(getFilm(id));
        }
        return films;
    }

    public void addLike(int filmId, int userId) {
        likeRepository.addLike(filmId, userId);
    }


    public static boolean validateFilm(Film f) {
        // Проверка, что название не пустое
        if (!StringUtils.hasText(f.getName())) {
            log.error("Не заполнено или пустое поле name");
            return false;
        }

        // Максимальная длина описания — 200 символов
        if (f.getDescription() != null && f.getDescription().length() > 200) {
            log.error("Описание пустое либо содержит больше 200 символов");
            return false;
        }

        // Дата релиза — не раньше 28 декабря 1895 года
        LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);
        if (f.getReleaseDate() == null || f.getReleaseDate().isBefore(minReleaseDate)) {
            log.error("Дата релиза фильма не заполнена или заполнена некорректно");
            return false;
        }

        // Продолжительность фильма должна быть положительным числом
        if (f.getDuration() <= 0) {
            log.error("Продолжительность фильма не заполнена или заполнена некорректно");
            return false;
        }
        return true; // Все проверки пройдены успешно
    }

    private void validateMpa(MPA mpa) {
        if (mpaRepository.findMPAById(mpa.getId()).isEmpty()) {
            throw new NoFoundIdException("такого MPA нет");
        }
    }

    private void validateGenres(List<Genre> genres) {
        for (Genre genre : genres) {
            if (genreRepository.findGenreById(genre.getId()).isEmpty()) {
                throw new NoFoundIdException("такого жанра нет");
            }
        }
    }

    private void checkFilmId(int filmId) { // будет ошибка 404 если id нет в таблице
        filmRepository.getFilmById(filmId);
    }
}



