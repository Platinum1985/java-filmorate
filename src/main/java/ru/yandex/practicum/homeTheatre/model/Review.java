package ru.yandex.practicum.homeTheatre.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@Data
@EqualsAndHashCode(of = {"reviewId"})
public class Review {
    private int reviewId;
    private String content;
    private Boolean isPositive;
    private int userId;
    private int filmId;
    private int useful = 0;
}
