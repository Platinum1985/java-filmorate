package ru.yandex.practicum.homeTheatre.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@Data
@Component
@EqualsAndHashCode(of = {"id"})
public class Like {
    private int id;
    private int filmId;
    private int userId;
}
