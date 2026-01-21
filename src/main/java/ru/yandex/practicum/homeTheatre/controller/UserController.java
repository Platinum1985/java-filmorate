package ru.yandex.practicum.homeTheatre.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.homeTheatre.dto.FriendshipDto;
import ru.yandex.practicum.homeTheatre.dto.UserDto;
import ru.yandex.practicum.homeTheatre.exceptions.NoFoundIdException;
import ru.yandex.practicum.homeTheatre.exceptions.ValidationException;
import ru.yandex.practicum.homeTheatre.model.Friendship;
import ru.yandex.practicum.homeTheatre.model.User;
import ru.yandex.practicum.homeTheatre.service.UserService;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

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

    @GetMapping("/users/{id}/friends")
    public Set<Integer> getFriendsByUserId(@PathVariable("id") int id) {
        return userService.getFriendsById(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Set<Integer> getMutualFriends(@PathVariable("id") int id1, @PathVariable("otherId") int id2) {
        return userService.getMutualFriends(id1, id2);
    }

    @PutMapping("/users/{id}/friends/{friendId}") // +
    public void addFriendById(@PathVariable("id") int yourId, @PathVariable("friendId") int friendId) {
        FriendshipDto friendshipDto = new FriendshipDto();
        friendshipDto.setUserId1(yourId);
        friendshipDto.setUserId2(friendId);
        userService.addFriendById(friendshipDto);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}") // +
    public void deleteFriendById(@PathVariable("id") int yourId, @PathVariable("friendId") int friendId) {
        userService.deleteFriendById(yourId, friendId);
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
