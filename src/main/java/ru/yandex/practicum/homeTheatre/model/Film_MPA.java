package ru.yandex.practicum.homeTheatre.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Film_MPA {
    int id;
    int mpaId;
    int filmId;
}
