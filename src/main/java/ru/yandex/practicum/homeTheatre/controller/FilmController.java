package ru.yandex.practicum.homeTheatre.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.homeTheatre.exceptions.NoFoundIdException;
import ru.yandex.practicum.homeTheatre.exceptions.ValidationException;
import ru.yandex.practicum.homeTheatre.model.Film;
import ru.yandex.practicum.homeTheatre.service.FilmService;
import ru.yandex.practicum.homeTheatre.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;

    @GetMapping("/films")
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable("id") int id) {
        if (!filmService.existFilmById(id)) {
            log.error("Фильм с таким ID {} не найден", id);
            throw new NoFoundIdException("Пост с id = " + id + " не найден");
        }
        return filmService.getFilm(id);
    }

    @GetMapping("/films/popular")
    public Collection<Film> getPopularFilms(@RequestParam(value = "count", required = false,
            defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }


    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        log.info("Начинается создание нового фильма: {}", film);
        // проверяем выполнение необходимых условий
        if (validateFilm(film)) {
            log.info("Валидация фильма {} прошла успешно", film);
            // формируем дополнительные данные
            log.info("Фильму {} присвоен id {}", film, film.getId());
            // сохраняем новую публикацию в памяти приложения
            filmService.addFilm(film);
        } else {
            log.error("некорректно заполнены поля");
            throw new ValidationException("некорректно заполнены поля");

        }
        log.info("Фильм {} успешно создан и добавлен в HashMap", film);
        return film;

    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) {
        log.info("Начинается обновление фильма: {}", film);
        // проверяем необходимые условия
        if (!filmService.existFilm(film)) {
            log.error("Фильм с ID {} не найден", film.getId());
            throw new NoFoundIdException("Пост с id = " + film.getId() + " не найден");
        }
        if (!validateFilm(film)) {
            log.error("Некорректно заполнены поля фильма {}", film);
            throw new ValidationException("некорректно заполнены поля");
        }
        filmService.updateFilm(film);
        log.info("Фильм успешно обновлен: {}", film);
        return film;
    }

    @DeleteMapping("/films")
    public void deleteFilm(@RequestParam int id) {
        filmService.removeFilm(id);
        log.trace("Фильм с Id = {} удален", id);
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public void addLike(@PathVariable("filmId") int filmId, @PathVariable("userId") int userId) {
        if (!filmService.existFilmById(filmId) || userService.getUserById(userId) == null) {
            log.error("Фильм с таким Id {} не найден", filmId);
            throw new NoFoundIdException("Получены некорректные id фильма или пользователя");
        }
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable("filmId") int filmId, @PathVariable("userId") int userId) {
        if (!filmService.existFilmById(filmId)) {
            log.error("Film с таким Id {} для удаления Like не найден", filmId);
            throw new NoFoundIdException("Фильм с id = " + filmId + " не найден");
        }
        if (!filmService.getFilm(filmId).getLikes().contains(userId)) {
            throw new NoFoundIdException("В лайках нет польз id = " + userId);
        }
        filmService.deleteLike(filmId, userId);
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

    @ExceptionHandler(NoFoundIdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoFoundIdException(NoFoundIdException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(ValidationException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGeneralException(Exception e) {
        log.error("Произошла ошибка на сервере", e);
        return Map.of("error", "Произошла внутренняя ошибка сервера.");
    }
}
