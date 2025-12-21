package ru.yandex.practicum.homeTheatre.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.homeTheatre.exceptions.NoFoundIdException;
import ru.yandex.practicum.homeTheatre.exceptions.ValidationException;
import ru.yandex.practicum.homeTheatre.model.User;
import ru.yandex.practicum.homeTheatre.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public Collection<User> findAll() {

        return userService.getAllUsersStorage();
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriendById(@PathVariable("id") int yourId, @PathVariable("friendId") int friendId) {
        if (userService.getUserById(yourId) == null || userService.getUserById(friendId) == null) {
            throw new NoFoundIdException("Пользователи с такими id = " + yourId + " и " + friendId + " не найдены");
        }
        userService.deleteFriendById(yourId, friendId);
    }

    @DeleteMapping("/users")
    public void removeUserById(@RequestParam int id) {
        if (userService.getUserById(id) == null) {
            throw new NoFoundIdException("Пользователь с id = " + id + " не найден");
        }
        userService.removeUserById(id);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> getFriendsByUserId(@PathVariable("id") int id) {
        if (userService.getUserById(id) == null) {
            throw new NoFoundIdException("Пользователь с id = " + id + " не найден");
        }
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable("id") int id1, @PathVariable("otherId") int id2) {
        if (userService.getUserById(id1) == null || userService.getUserById(id2) == null) {
            throw new NoFoundIdException("Пользователи с такими id = " + id1 + " и " + id2 + " не найдены");
        }
        return userService.getMutualFriends(id1, id2);
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        log.info("Начинается создание нового user: {}", user);
        // проверяем выполнение необходимых условий
        if (validateUser(user)) {
            log.info("Валидация пользователя {} прошла успешно", user);
            userService.addUser(user);
        } else {
            log.error("Некорректно заполнены поля");
            throw new ValidationException("некорректно заполнены поля");

        }
        log.info("Пользователь {} успешно создан и добавлен в HashMap", user);
        return user;

    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriendById(@PathVariable("id") int yourId, @PathVariable("friendId") int friendId) {
        if (userService.getUserById(yourId) == null || userService.getUserById(friendId) == null) {
            throw new NoFoundIdException("Пользователи с такими id = " + yourId + " и " + friendId + " не найдены");
        }
        userService.addFriendById(yourId, friendId);
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        log.info("Начинается обновление пользователя: {}", user);
        // проверяем необходимые условия
        if (!userService.exists(user)) {
            log.error("Пользователь с ID {} не найден", user.getId());
            throw new NoFoundIdException("Пользователь с id = " + user.getId() + " не найден");
        }
        if (!validateUser(user)) {
            log.error("Поля пользователя {} заполнены некорректно", user.toString());
            throw new ValidationException("некорректно заполнены поля");
        }
        //если имя пустое-приравняем имя к Login
        if (!StringUtils.hasText(user.getName())) {
            log.debug("Имя пользователя {} пустое", user);
            user.setName(user.getLogin());
            log.debug("Имя изменяемого пользователя приравняли логину: name = {}", user.getName());
        }
        userService.updateUser(user);
        // allUsers.put(user.getId(), user);
        log.info("Изменили данные пользователя");
        return user;
    }

    public boolean validateUser(User user) {
        // Проверка электронной почты
        if (!StringUtils.hasText(user.getEmail()) || !user.getEmail().contains("@")) {
            log.error("Не заполнено email или заполнен некорректно");
            return false;
        }

        // Проверка логина
        if (!StringUtils.hasText(user.getLogin()) || user.getLogin().contains(" ")) {
            log.error("Не заполнен login или заполнен некорректно");
            return false;
        }

        // Дата рождения не может быть в будущем
        LocalDate today = LocalDate.now();
        if (user.getBirthday() == null || user.getBirthday().isAfter(today)) {
            log.error("Не заполнена дата или заполнен некорректно");
            return false;
        }

        return true; // Все проверки пройдены успешно
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
