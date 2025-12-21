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
            log.info("Размер HashMap ={}",allUsers.size());

    }
    @Override
    public Collection<User> getFriends(int userId) {
        if (allUsers.get(userId) != null) {
             allUsers.get(userId).getFriends().stream()
                    .map(allUsers::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            return allUsers.get(userId).getFriends().stream()
                    .map(allUsers::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } else {
            // пользователь с указанным userId не найден
            return Collections.emptyList();
        }
    }
    @Override
    public Collection<User> getMutualFriends(int userId1, int userId2) {
        if (allUsers.get(userId1) != null && allUsers.get(userId2) != null) {
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
        } else {
            // Один или оба пользователя не найдены
            return Collections.emptyList();
        }
    }

    @Override
    public void removeUser(int id) {
        allUsers.remove(id);
    }
    @Override
    public void addFriendById(int yourId, int friendId) {
        allUsers.get(yourId).getFriends().add(friendId);
        allUsers.get(friendId).getFriends().add(yourId);
    }

    @Override
    public void deleteFriendById(int yourId, int friendId) {
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

