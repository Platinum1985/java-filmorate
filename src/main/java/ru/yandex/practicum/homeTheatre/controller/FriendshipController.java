package ru.yandex.practicum.homeTheatre.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.homeTheatre.dto.FriendshipDto;
import ru.yandex.practicum.homeTheatre.exceptions.NoFoundIdException;
import ru.yandex.practicum.homeTheatre.exceptions.ValidationException;
import ru.yandex.practicum.homeTheatre.service.FriendshipService;

import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FriendshipController {
    private final FriendshipService friendshipService;

    @GetMapping("/users/{id}/friends")
    public Set<Integer> getFriendsByUserId(@PathVariable("id") int id) {
        return friendshipService.getFriendsById(id);
    }
    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Set<Integer> getMutualFriends(@PathVariable("id") int id1, @PathVariable("otherId") int id2) {
        return friendshipService.getMutualFriends(id1, id2);
    }

    @PutMapping("/users/{id}/friends/{friendId}") // +
    public void addFriendById(@PathVariable("id") int yourId, @PathVariable("friendId") int friendId) {
        FriendshipDto friendshipDto=new FriendshipDto();
        friendshipDto.setUserId_1(yourId);
        friendshipDto.setUserId_2(friendId);
        friendshipService.addFriendById(friendshipDto);
    }
    @DeleteMapping("/users/{id}/friends/{friendId}") // +
    public void deleteFriendById(@PathVariable("id") int yourId, @PathVariable("friendId") int friendId) {

        friendshipService.deleteFriendById(yourId, friendId);
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
