package ru.yandex.practicum.homeTheatre.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.homeTheatre.dto.UserDto;
import ru.yandex.practicum.homeTheatre.exceptions.NoFoundIdException;
import ru.yandex.practicum.homeTheatre.exceptions.ValidationException;
import ru.yandex.practicum.homeTheatre.model.User;
import ru.yandex.practicum.homeTheatre.service.UserService;

import java.util.Collection;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public Collection<User> findAll() {

        return userService.getAllUsers();
    }
    @GetMapping("/users/{userId}") // +
    public UserDto findUserById(@PathVariable("userId") int userId) {

        return userService.getUserById(userId);
    }

    @DeleteMapping("/users")
    public void removeUserById(@RequestParam int id) {
        userService.removeUserById(id);
    }


    @PostMapping("/users") // +
    public UserDto create(@RequestBody UserDto userRequest) {
        log.info("Начинается создание нового user: {}", userRequest);
        userService.addUser(userRequest);
        log.info("Пользователь {} успешно создан и добавлен в HashMap", userRequest);
        return userRequest;

    }


    @PutMapping("/users") // +
    public UserDto update(@RequestBody UserDto userDto) {
        log.info("Начинается обновление пользователя: {}", userDto);
        userService.updateUser(userDto);
        log.info("Изменили данные пользователя");
        return userDto;
    }

    @ExceptionHandler(NoFoundIdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNoFoundIdException(NoFoundIdException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(ValidationException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGeneralException(Exception e) {
        log.error("Произошла ошибка на сервере", e);
        return Map.of("error", "Произошла внутренняя ошибка сервера.");
    }
}
