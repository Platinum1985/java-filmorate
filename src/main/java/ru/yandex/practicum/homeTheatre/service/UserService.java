package ru.yandex.practicum.homeTheatre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.homeTheatre.dal.FriendshipRepository;
import ru.yandex.practicum.homeTheatre.dal.UserRepository;
import ru.yandex.practicum.homeTheatre.exceptions.NoFoundIdException;
import ru.yandex.practicum.homeTheatre.exceptions.ValidationException;
import ru.yandex.practicum.homeTheatre.model.Friendship;
import ru.yandex.practicum.homeTheatre.model.User;


import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    public Collection<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            Set<Integer> friendIds = friendshipRepository.getFriendsById(user.getId());
            Set<User> friends = new HashSet<>();
            for (int i : friendIds) {
                friends.add(userRepository.findById(i).get());
            }
            user.setFriends(friends);
        }
        return users;
    }  // +


    public void removeUserById(int id) {
        userRepository.removeUserById(id);
    }


    public User addUser(User user) { // +
        if (validateUser(user)) {
            log.info("Валидация пользователя {} прошла успешно", user);
            userRepository.save(user);
        } else {
            log.error("Некорректно заполнены поля");
            throw new ValidationException("некорректно заполнены поля");

        }
        return user;
    }

    public void updateUser(User user) { // +
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
        userRepository.update(user);
    }

    public User getUserById(int userId) { // +
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NoFoundIdException("Пользователь с таким id не найден");
        } else {
            user.get().setFriends(getFriendsById(userId));
        }
        return user.get();

    }

    public void addFriendById(Friendship friendship) {
        if (getUserById(friendship.getUserId1()) == null || getUserById(friendship.getUserId2()) == null) {
            throw new NoFoundIdException("Пользователи с такими id не найдены");
        } else {
            friendshipRepository.save(friendship);
        }
    }

    public Set<User> getFriendsById(int id) {
        if (getUserById(id) == null) {
            throw new NoFoundIdException("Пользователь с таким id не найден");
        } else {
            Set<Integer> friendIds = friendshipRepository.getFriendsById(id);
            Set<User> friends = new HashSet<>();
            for (int i : friendIds) {
                friends.add(getUserById(i));
            }
            return friends;
        }
    }

    public void deleteFriendById(Friendship friendship) {
        friendshipRepository.deleteFriendById(friendship);
    }

    public Set<User> getMutualFriends(int id1, int id2) {
        if (userRepository.findById(id1).isEmpty() || userRepository.findById(id2).isEmpty()) {
            throw new NoFoundIdException("Пользователей с такими id нет");
        } else {
            Set<Integer> friendIds = friendshipRepository.findMutualFriends(id1, id2);
            Set<User> friends = new HashSet<>();
            for (int i : friendIds) {
                friends.add(getUserById(i));
            }
            return friends;
        }
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
