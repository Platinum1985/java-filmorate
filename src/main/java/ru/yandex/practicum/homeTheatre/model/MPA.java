package ru.yandex.practicum.homeTheatre.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

@Data
@Component
@EqualsAndHashCode(of = {"id"})
public class MPA {
    private int id;
    private String name;
}
