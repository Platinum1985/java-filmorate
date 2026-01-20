package ru.yandex.practicum.homeTheatre.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FriendshipDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;
    private int userId_1;
    private int userId_2;
    // private FriendStatus friendStatus;

}
