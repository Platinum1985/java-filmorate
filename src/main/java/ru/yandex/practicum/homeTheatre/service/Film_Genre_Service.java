package ru.yandex.practicum.homeTheatre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.homeTheatre.dal.Film_Genre_Repository;
import ru.yandex.practicum.homeTheatre.model.Film_Genre;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class Film_Genre_Service {
    private final Film_Genre_Repository filmGenreRepository;

    public Set<Integer> getGenresByFilmId(int filmId) {
        return filmGenreRepository.getGenresByFilmId(filmId);
    }

    public void addGenreByFilmId(Film_Genre filmGenre) {
        filmGenreRepository.save(filmGenre);
    }

}
