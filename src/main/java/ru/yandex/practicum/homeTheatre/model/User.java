package ru.yandex.practicum.homeTheatre.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = { "email" })//если поля email равны, то и их hashCode равны и объекты равны
public class User {
    int id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    Set<Integer> friends = new HashSet<>();
}
