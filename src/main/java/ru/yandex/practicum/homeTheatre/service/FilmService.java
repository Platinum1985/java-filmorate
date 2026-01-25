package ru.yandex.practicum.homeTheatre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.homeTheatre.dal.*;
import ru.yandex.practicum.homeTheatre.dto.GenreDto;
import ru.yandex.practicum.homeTheatre.dto.MpaDto;
import ru.yandex.practicum.homeTheatre.exceptions.ValidationException;
import ru.yandex.practicum.homeTheatre.mapperDto.GenreMapper;
import ru.yandex.practicum.homeTheatre.mapperDto.MpaMapper;
import ru.yandex.practicum.homeTheatre.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final LikeRepository likeRepository;
    private final FilmMPARepository filmMpaRepository;
    private final FilmGenreRepository filmGenreRepository;
    private final FilmRepository filmRepository;

    public List<Film> getAllFilms() {
        List<Film> films = filmRepository.findAll();
        for (Film film : films) {
            int filmId = film.getId();
            Set<FilmGenre> genres = filmGenreRepository.getGenresByFilmId(filmId);
            Set<Integer> likes = likeRepository.getUserIdsByFilmId(filmId);
            FilmMPA filmMPA = filmMpaRepository.findMPAByFilmId(filmId);
            film.setMpa(MpaMapper.mapToMpaDto(filmMPA));
            Set<GenreDto> genreDtos = genres.stream()
                    .map(GenreMapper::mapToFilmGenre)
                    .collect(Collectors.toSet());
            film.setGenres(genreDtos);
        }
        return films;
    }

    public Film getFilm(int filmId) { // +
        Film film = filmRepository.getFilmById(filmId);
        Set<FilmGenre> genres = filmGenreRepository.getGenresByFilmId(filmId);
        FilmMPA filmMPA = filmMpaRepository.findMPAByFilmId(filmId);
        film.setMpa(MpaMapper.mapToMpaDto(filmMPA));
        Set<GenreDto> genreDtos = genres.stream()
                .map(GenreMapper::mapToFilmGenre)
                .collect(Collectors.toSet());
        film.setGenres(genreDtos);
        film.setLikes(likeRepository.getUserIdsByFilmId(filmId));
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
            Set<GenreDto> genres = film.getGenres();
            for (GenreDto genre : genres) {
                FilmGenre filmGenre = GenreMapper.mapToFilmGenre(genre, film.getId());
                filmGenreRepository.save(filmGenre);
            }
            MpaDto mpaDto = film.getMpa();
            filmMpaRepository.save(MpaMapper.mapToFilmMPA(mpaDto, film.getId()));

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
            Set<GenreDto> genres = film.getGenres();
            for (GenreDto genre : genres) {
                FilmGenre filmGenre = GenreMapper.mapToFilmGenre(genre, film.getId());
                filmGenreRepository.update(filmGenre);
            }

            MpaDto mpaDto = film.getMpa();
            FilmMPA filmMpa = MpaMapper.mapToFilmMPA(mpaDto, film.getId());
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
        if (f.getDuration() <= 0) {
            log.error("Продолжительность фильма не заполнена или заполнена некорректно");
            return false;
        }

        // Проверка поля mpa
        MpaDto mpa = f.getMpa();
        if (mpa == null || mpa.getId() < 1 || mpa.getId() > 5) {
            log.error("Значение поля mpa должно быть целое число от 1 до 5");
            return false;
        }

        // Проверка поля genres
        Set<GenreDto> genres = f.getGenres();
        if (genres == null || genres.isEmpty() || genres.size() > 6) {
            log.error("Список жанров пуст или количество жанров превышает допустимое значение (не более 5)");
            return false;
        }
        return true; // Все проверки пройдены успешно
    }
}


