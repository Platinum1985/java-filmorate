package ru.yandex.practicum.homeTheatre.model;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.homeTheatre.dto.GenreDto;
import ru.yandex.practicum.homeTheatre.dto.MpaDto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Component
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private MpaDto mpa; // Предполагаем, что Mpa - это отдельный класс для объекта mpa в JSON
    private Set<GenreDto> genres = new HashSet<>(); // Предполагаем, что Genre - это отдельный класс для объектов в массиве genres
    private Set<Integer> likes = new HashSet<>();
}
