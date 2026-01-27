package ru.yandex.practicum.homeTheatre.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.homeTheatre.exceptions.NoFoundIdException;
import ru.yandex.practicum.homeTheatre.exceptions.ValidationException;
import ru.yandex.practicum.homeTheatre.model.Film;
import ru.yandex.practicum.homeTheatre.service.FilmService;

import java.util.Collection;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;


    @GetMapping("/films")
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable("id") int id) {
        return filmService.getFilm(id);
    }

    @GetMapping("/films/popular")
    public Collection<Film> getPopularFilms(@RequestParam(value = "count", required = false,
            defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }


    @PostMapping("/films")
    public Film create(@RequestBody Film film) {
        filmService.addFilm(film);
        return film;

    }

    @PutMapping("/films")
    public Film update(@RequestBody Film film) { // почему горишь синим???
        filmService.updateFilm(film);
        return film;
    }

    @DeleteMapping("/films")
    public void deleteFilm(@RequestParam int id) {
        filmService.removeFilm(id);
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public void addLike(@PathVariable("filmId") int filmId, @PathVariable("userId") int userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable("filmId") int filmId, @PathVariable("userId") int userId) {
        filmService.deleteLike(filmId, userId);
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
        return Map.of("error", "Произошла внутренняя ошибка сервера.");
    }
}
