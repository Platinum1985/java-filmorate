package ru.yandex.practicum.homeTheatre.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id"})
public class FilmMPA {
    private int id;
    private int mpaId;
    private int filmId;
}
