package ru.yandex.practicum.homeTheatre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.homeTheatre.dal.*;
import ru.yandex.practicum.homeTheatre.dto.GenreDto;
import ru.yandex.practicum.homeTheatre.dto.MpaDto;
import ru.yandex.practicum.homeTheatre.exceptions.NoFoundIdException;
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
    private final MPARepository mpaRepository;
    private final GenreRepository genreRepository;

    public List<Film> getAllFilms() {
        List<Film> films = filmRepository.findAll();
        for (Film film : films) {
            int filmId = film.getId();
            List<FilmGenre> genres = filmGenreRepository.getGenresByFilmId(filmId);
            Set<Integer> likes = likeRepository.getUserIdsByFilmId(filmId);
            FilmMPA filmMPA = filmMpaRepository.findMPAByFilmId(filmId);
            film.setMpa(MpaMapper.mapToMpaDto(filmMPA));
            List<GenreDto> genreDtos = genres.stream()
                    .map(GenreMapper::mapToFilmGenre)
                    .collect(Collectors.toList());
            film.setGenres(genreDtos);
        }
        return films;
    }

    public Film getFilm(int filmId) {
        Film film = filmRepository.getFilmById(filmId);
        List<FilmGenre> genres = filmGenreRepository.getGenresByFilmId(filmId).stream()
                .collect(Collectors.toList());
        FilmMPA filmMPA = filmMpaRepository.findMPAByFilmId(filmId);
        film.setMpa(MpaMapper.mapToMpaDto(filmMPA));
        List<GenreDto> genreDtos = genres.stream()
                .map(GenreMapper::mapToFilmGenre)
                .collect(Collectors.toList());
        film.setGenres(genreDtos);
        film.setLikes(likeRepository.getUserIdsByFilmId(filmId));
        return film;
    }

    public void addFilm(Film film) { // ++
        validateMpa(film.getMpa());
        validateGenres(film.getGenres());
        if (!validateFilm(film)) {
            log.error("некорректно заполнены поля");
            throw new ValidationException("некорректно заполнены поля");

        } else {
            log.info("Валидация фильма {} прошла успешно", film);
            // формируем дополнительные данные
            log.info("Фильму {} присвоен id {}", film, film.getId());
            // сохраняем новую публикацию в памяти приложения
            // надо бы проверить на дубликаты во всех таблицах перед добавлением
            filmRepository.save(film);
            List<GenreDto> genres = film.getGenres();
            for (GenreDto genre : genres) {
                FilmGenre filmGenre = GenreMapper.mapToFilmGenre(genre, film.getId());
                filmGenreRepository.save(filmGenre);
            }
            MpaDto mpaDto = film.getMpa();
            filmMpaRepository.save(MpaMapper.mapToFilmMPA(mpaDto, film.getId()));


        }
    }

    public void removeFilm(int id) { // +
        filmRepository.removeFilmById(id);
    }

    public void updateFilm(Film film) {
        // checkFilmId(film.getId());
        validateMpa(film.getMpa());
        if (!validateFilm(film)) {
            log.error("Некорректно заполнены поля фильма {}", film);
            throw new ValidationException("некорректно заполнены поля");
        } else {
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
        if (mpa == null) {
            log.error("Значение поля mpa должно быть целое число от 1 до 5");
            return false;
        }

        // Проверка поля genres
        /* List<GenreDto> genres = f.getGenres();
        if (genres == null || genres.isEmpty() || genres.size() > 6) {
            log.error("Список жанров пуст или количество жанров превышает допустимое значение (не более 6)");
            return false;
        } */
        return true; // Все проверки пройдены успешно
    }

    private void validateMpa(MpaDto mpa) {
        if (mpaRepository.findMPAById(mpa.getId()).isEmpty()) {
            throw new NoFoundIdException("такого MPA нет");
        }
    }

    private void validateGenres(List<GenreDto> genres) {
        for (GenreDto genre : genres) {
            if (genreRepository.findGenreById(genre.getId()).isEmpty()) {
                throw new NoFoundIdException("такого жанра нет");
            }
        }
    }

    private void checkFilmId(int filmId) { // будет ошибка 404 если id нет в таблице
        filmRepository.getFilmById(filmId);
    }
}



