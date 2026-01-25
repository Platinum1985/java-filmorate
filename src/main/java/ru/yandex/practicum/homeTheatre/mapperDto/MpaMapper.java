package ru.yandex.practicum.homeTheatre.mapperDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.homeTheatre.dto.MpaDto;
import ru.yandex.practicum.homeTheatre.model.FilmMPA;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MpaMapper {
    public static FilmMPA mapToFilmMPA(MpaDto mpaDto, int filmId) {
        FilmMPA filmMPA = new FilmMPA();
        filmMPA.setMpaId(mpaDto.getId());
        filmMPA.setFilmId(filmId);

        return filmMPA;
    }

    public static MpaDto mapToMpaDto(FilmMPA filmMPA) {
        MpaDto mpaDto = new MpaDto();
        mpaDto.setId(filmMPA.getMpaId());
        return mpaDto;
    }
}