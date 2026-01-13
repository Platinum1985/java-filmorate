package ru.yandex.practicum.homeTheatre.model;

import lombok.Data;
import org.springframework.stereotype.Component;

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
    private Long duration;
    private Set<Integer> likes = new HashSet<>();
    private MPA mpa; //добавить обработку и валидацию
    private Set<Genre> genres = new HashSet<>(); // добавить обработку и валидацию
}