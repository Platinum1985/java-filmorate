package ru.yandex.practicum.homeTheatre.mapperDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.homeTheatre.dto.GenreDto;
import ru.yandex.practicum.homeTheatre.model.FilmGenre;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenreMapper {
    public static FilmGenre mapToFilmGenre(GenreDto genreDto, int filmId) {
        FilmGenre filmGenre = new FilmGenre();
        filmGenre.setGenreId(genreDto.getId());
        filmGenre.setFilmId(filmId);

        return filmGenre;
    }

    public static GenreDto mapToFilmGenre(FilmGenre filmGenre) {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(filmGenre.getGenreId());
        return genreDto;
    }
}