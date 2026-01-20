package ru.yandex.practicum.homeTheatre.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    Set<Integer> friends = new HashSet<>();
}
