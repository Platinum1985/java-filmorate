package ru.yandex.practicum.homeTheatre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.homeTheatre.exceptions.ValidationException;
import ru.yandex.practicum.homeTheatre.model.User;
import ru.yandex.practicum.homeTheatre.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final User user;

    public Collection<User> getAllUsersStorage() {
        return userStorage.getAllUsers();
    }

    public Collection<User> getMutualFriends(int userId1, int userId2) {
        return userStorage.getMutualFriends(userId1, userId2);
    }

    public Collection<User> getFriends(int userId) {
        return userStorage.getFriends(userId);
    }

    public void addFriendById(int yourId, int friendId) {
        userStorage.addFriendById(yourId, friendId);
    }

    public void deleteFriendById(int yourId, int friendId) {
        userStorage.deleteFriendById(yourId, friendId);
    }

    public void removeUserById(int id) {
        userStorage.removeUser(id);
    }

    public boolean exists(User user) {
        return userStorage.exists(user);
    }

    public void addUser(User user) {
        if (validateUser(user)) {
            log.info("Валидация пользователя {} прошла успешно", user);
            userStorage.addUser(user);
        } else {
            log.error("Некорректно заполнены поля");
            throw new ValidationException("некорректно заполнены поля");

        }
    }

    public void updateUser(User user) {
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
        userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        return userStorage.getUser(id);
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
}
