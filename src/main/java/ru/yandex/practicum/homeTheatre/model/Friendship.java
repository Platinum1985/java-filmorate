package ru.yandex.practicum.homeTheatre.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Friendship {
    private int id;
    private int userId1; // user который отправляет запрос на дружбу
    private int userId2; // user который принимает или не принимает дружбу
}
