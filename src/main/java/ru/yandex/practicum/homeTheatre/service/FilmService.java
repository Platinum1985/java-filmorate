package ru.yandex.practicum.homeTheatre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.homeTheatre.dal.*;
import ru.yandex.practicum.homeTheatre.exceptions.ValidationException;
import ru.yandex.practicum.homeTheatre.model.Film;
import ru.yandex.practicum.homeTheatre.model.Film_Genre;
import ru.yandex.practicum.homeTheatre.model.Film_MPA;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final LikeRepository likeRepository;
    private final Film_MPA_Repository filmMpaRepository;
    private final Film_Genre_Repository filmGenreRepository;
    private final FilmRepository filmRepository;

    public List<Film> getAllFilms() {
        List<Film> films = filmRepository.findAll();
        for (Film film : films) {
            int filmId = film.getId();
            Set<Integer> genres = filmGenreRepository.getGenresByFilmId(filmId);
            Set<Integer> likes = likeRepository.getUserIdsByFilmId(filmId);
            int mpaId = filmMpaRepository.findMPAByFilmId(filmId).getMpaId();
            film.setLikes(likes);
            film.setGenres(genres);
            film.setMpa(mpaId);
        }
        return films;
    }

    public Film getFilm(int filmId) { // +
        Set<Integer> genres = filmGenreRepository.getGenresByFilmId(filmId);
        Set<Integer> likes = likeRepository.getUserIdsByFilmId(filmId);
        int mpaId = filmMpaRepository.findMPAByFilmId(filmId).getMpaId();
        Film film = filmRepository.getFilmById(filmId);
        film.setLikes(likes);
        film.setGenres(genres);
        film.setMpa(mpaId);
        return film;
    }

    public void addFilm(Film film) { // ++
        if (validateFilm(film)) {
            log.info("Валидация фильма {} прошла успешно", film);
            // формируем дополнительные данные
            log.info("Фильму {} присвоен id {}", film, film.getId());
            // сохраняем новую публикацию в памяти приложения
            // надо бы проверить на дубликаты во всех таблицах перед добавлением
            filmRepository.save(film);
            Set<Integer> genres = film.getGenres();
            for (int genreId : genres) {
                Film_Genre filmGenre = new Film_Genre();
                filmGenre.setFilmId(film.getId());
                filmGenre.setGenreId(genreId);
                filmGenreRepository.save(filmGenre);
            }
            int mpaId = film.getMpa();
            Film_MPA filmMpa = new Film_MPA();
            filmMpa.setFilmId(film.getId());
            filmMpa.setMpaId(mpaId);
            filmMpaRepository.save(filmMpa);

        } else {
            log.error("некорректно заполнены поля");
            throw new ValidationException("некорректно заполнены поля");

        }
    }

    public void removeFilm(int id) { // +
        filmRepository.removeFilmById(id);
    }

    public void updateFilm(Film film) {
        if (!validateFilm(film)) {
            log.error("Некорректно заполнены поля фильма {}", film);
            throw new ValidationException("некорректно заполнены поля");
        } else {
            Set<Integer> genres = film.getGenres();
            for (int genreId : genres) {
                Film_Genre filmGenre = new Film_Genre();
                filmGenre.setFilmId(film.getId());
                filmGenre.setGenreId(genreId);
                filmGenreRepository.update(filmGenre);
            }
            int mpaId = film.getMpa();
            Film_MPA filmMpa = new Film_MPA();
            filmMpa.setFilmId(film.getId());
            filmMpa.setMpaId(mpaId);
            filmMpaRepository.update(filmMpa);

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
        if (f.getDuration() == null || f.getDuration() <= 0) {
            log.error("Продолжительность фильма не заполнена или заполнена некорректно");
            return false;
        }

        // Проверка поля mpa
        if (f.getMpa() < 1 || f.getMpa() > 5) {
            log.error("Значение поля mpa должно быть целое число от 1 до 5");
            return false;
        }

        // Проверка поля genres
        if (f.getGenres() == null || f.getGenres().isEmpty()) {
            log.error("Список жанров пуст или не указан");
            return false;
        }
        if (f.getGenres().size() > 6) {
            log.error("Количество жанров превышает допустимое значение (не более 6)");
            return false;
        }
        for (Integer genre : f.getGenres()) {
            if (genre == null || genre < 1 || genre > 6) {
                log.error("Жанр должен быть положительным числом в интервале от 1 до 6");
                return false;
            }
        }

        return true; // Все проверки пройдены успешно
    }
}


