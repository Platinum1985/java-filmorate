package ru.yandex.practicum.homeTheatre.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Like {
    private int id;
    private int filmId;
    private int userId;
}
