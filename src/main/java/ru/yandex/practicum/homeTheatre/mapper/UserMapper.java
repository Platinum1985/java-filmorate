package ru.yandex.practicum.homeTheatre.mapper;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.homeTheatre.dto.UserDto;
import ru.yandex.practicum.homeTheatre.model.User;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
// это аннотация из библиотеки Lombok, которая генерирует конструктор без параметров с указанным уровнем доступа. В данном случае уровень доступа установлен как PRIVATE, что означает, что конструктор будет доступен только внутри класса
public final class UserMapper {
    public static User mapToUser(UserDto request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setBirthday(request.getBirthday());

        return user;
    }

    public static UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setLogin(user.getLogin());
        dto.setBirthday(user.getBirthday());
        return dto;
    }
/*
    public static User updateUserFields(User user, UpdateUserRequest request) {
        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }
        if (request.hasPassword()) {
            user.setPassword(request.getPassword());
        }
        if (request.hasUsername()) {
            user.setUsername(request.getUsername());
        }
        return user;
    }*/
}