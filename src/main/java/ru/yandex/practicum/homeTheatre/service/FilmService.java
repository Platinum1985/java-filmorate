package ru.yandex.practicum.homeTheatre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.homeTheatre.exceptions.NoFoundIdException;
import ru.yandex.practicum.homeTheatre.exceptions.ValidationException;
import ru.yandex.practicum.homeTheatre.model.Film;
import ru.yandex.practicum.homeTheatre.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    // private final Film user;

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilm(int id) {
        return filmStorage.getFilm(id);
    }

    public void addFilm(Film film) { //++
        if (validateFilm(film)) {
            log.info("Валидация фильма {} прошла успешно", film);
            // формируем дополнительные данные
            log.info("Фильму {} присвоен id {}", film, film.getId());
            // сохраняем новую публикацию в памяти приложения
            filmStorage.addFilm(film);
        } else {
            log.error("некорректно заполнены поля");
            throw new ValidationException("некорректно заполнены поля");

        }
    }

    public void removeFilm(int id) {
        filmStorage.removeFilm(id);
    }

    public void updateFilm(Film film) {
        if (!validateFilm(film)) {
            log.error("Некорректно заполнены поля фильма {}", film);
            throw new ValidationException("некорректно заполнены поля");
        }
        filmStorage.updateFilm(film);
    }

    public void addLike(int filmId, int userId) {
        if (userService.getUserById(userId) == null) {
            log.error("User с таким Id {} не найден", filmId);
            throw new NoFoundIdException("Получены некорректные id пользователя");
        }
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

        return true; // Все проверки пройдены успешно
    }
}


