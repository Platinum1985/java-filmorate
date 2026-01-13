package ru.yandex.practicum.homeTheatre.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.homeTheatre.exceptions.NoFoundIdException;
import ru.yandex.practicum.homeTheatre.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> allUsers = new HashMap<>();

    @Override
    public void addUser(User user) {
        user.setId(getNextId());
        log.info("Присвоили id {} для пользователя {}", user.getId(), user);
        //если имя пустое-приравняем имя к Login
        if (!StringUtils.hasText(user.getName())) {
            log.debug("Имя добавляемого пользователя {} пустое", user);
            user.setName(user.getLogin());
            log.debug("Имя пользователя {} приравняли логину", user);
        }
        allUsers.put(user.getId(), user);
        log.info("Размер HashMap ={}", allUsers.size());

    }

    @Override
    public Collection<User> getFriends(int userId) {
        if (getUser(userId) == null) {
            throw new NoFoundIdException("Пользователь с id = " + userId + " не найден");
        } else {
            allUsers.get(userId).getFriends().stream()
                    .map(allUsers::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            return allUsers.get(userId).getFriends().stream()
                    .map(allUsers::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Collection<User> getMutualFriends(int userId1, int userId2) {
        if (getUser(userId1) == null || getUser(userId2) == null) {
            throw new NoFoundIdException("Пользователи с такими id = " + userId1 + " и " + userId2 + " не найдены");
        } else {
            Set<Integer> commonFriends = allUsers.get(userId1).getFriends().stream()
                    .filter(allUsers.get(userId2).getFriends()::contains)
                    .collect(Collectors.toSet());

            commonFriends.stream()
                    .map(allUsers::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            return commonFriends.stream()
                    .map(allUsers::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void removeUser(int id) {
        if (getUser(id) == null) {
            throw new NoFoundIdException("Пользователь с id = " + id + " не найден");
        }
        allUsers.remove(id);
    }

    @Override
    public void addFriendById(int yourId, int friendId) {
        if (getUser(yourId) == null || getUser(friendId) == null) {
            throw new NoFoundIdException("Пользователи с такими id = " + yourId + " и " + friendId + " не найдены");
        }
        allUsers.get(yourId).getFriends().add(friendId);////
        allUsers.get(friendId).getFriends().add(yourId);////
    }

    @Override
    public void deleteFriendById(int yourId, int friendId) {
        if (getUser(yourId) == null || getUser(friendId) == null) {
            throw new NoFoundIdException("Пользователи с такими id = " + yourId + " и " + friendId + " не найдены");
        }
        allUsers.get(yourId).getFriends().remove(friendId);
        allUsers.get(friendId).getFriends().remove(yourId);
    }

    @Override
    public User getUser(int id) {
        return allUsers.get(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return allUsers.values();
    }

    @Override
    public void updateUser(User user) {
        if (!exists(user)) {
            log.error("Пользователь с ID {} не найден", user.getId());
            throw new NoFoundIdException("Пользователь с id = " + user.getId() + " не найден");
        }
        allUsers.put(user.getId(), user);
    }

    private int getNextId() {
        int currentMaxId = Math.toIntExact(allUsers.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0));
        int nextId = ++currentMaxId;
        return nextId;
    }

    public boolean exists(User user) {
        return allUsers.containsKey(user.getId());
    }
}

