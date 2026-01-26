package ru.yandex.practicum.homeTheatre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.homeTheatre.dal.FilmGenreRepository;
import ru.yandex.practicum.homeTheatre.model.FilmGenre;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmGenreService {
    private final FilmGenreRepository filmGenreRepository;

    public List<FilmGenre> getGenresByFilmId(int filmId) {
        return filmGenreRepository.getGenresByFilmId(filmId);
    }

    public void addGenreByFilmId(FilmGenre filmGenre) {
        filmGenreRepository.save(filmGenre);
    }

}
