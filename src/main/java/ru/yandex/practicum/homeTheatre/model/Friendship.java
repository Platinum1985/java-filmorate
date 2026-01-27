package ru.yandex.practicum.homeTheatre.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@Data
@Component
@EqualsAndHashCode(of = {"id"})
public class Friendship {
    private int id;
    private int userRequestId; // user который отправляет запрос на дружбу
    private int userFriendId; // user который принимает или не принимает дружбу
}
