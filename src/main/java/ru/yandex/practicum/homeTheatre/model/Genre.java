package ru.yandex.practicum.homeTheatre.model;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Genre {
    private int id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre)) return false;
        Genre genre = (Genre) o;
        return id == genre.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
