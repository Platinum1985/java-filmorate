package ru.yandex.practicum.homeTheatre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.homeTheatre.dal.FriendshipRepository;
import ru.yandex.practicum.homeTheatre.dal.UserRepository;
import ru.yandex.practicum.homeTheatre.dto.FriendshipDto;
import ru.yandex.practicum.homeTheatre.dto.UserDto;
import ru.yandex.practicum.homeTheatre.exceptions.NoFoundIdException;
import ru.yandex.practicum.homeTheatre.exceptions.ValidationException;
import ru.yandex.practicum.homeTheatre.mapper.FriendshipMapper;
import ru.yandex.practicum.homeTheatre.mapper.UserMapper;
import ru.yandex.practicum.homeTheatre.model.User;


import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    public Collection<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            Set<Integer> friends = friendshipRepository.getFriendsById(user.getId());
            user.setFriends(friends);
        }
        return users;
    }  // +


    public void removeUserById(int id) {
        userRepository.removeUserById(id);
    }


    public void addUser(UserDto userRequest) { // +
        if (validateUser(userRequest)) {
            log.info("Валидация пользователя {} прошла успешно", userRequest);
            userRepository.save(UserMapper.mapToUser(userRequest));
        } else {
            log.error("Некорректно заполнены поля");
            throw new ValidationException("некорректно заполнены поля");

        }
    }

    public void updateUser(UserDto userDto) { // +
        if (!validateUser(userDto)) {
            log.error("Поля пользователя {} заполнены некорректно", userDto.toString());
            throw new ValidationException("некорректно заполнены поля");
        }
        //если имя пустое-приравняем имя к Login
        if (!StringUtils.hasText(userDto.getName())) {
            log.debug("Имя пользователя {} пустое", userDto);
            userDto.setName(userDto.getLogin());
            log.debug("Имя изменяемого пользователя приравняли логину: name = {}", userDto.getName());
        }
        userRepository.update(UserMapper.mapToUser(userDto));
    }

    public UserDto getUserById(int userId) { // +
        Set<Integer> friends = friendshipRepository.getFriendsById(userId); // нашли список друзей
        return userRepository.findById(userId)
                .map(user -> {
                    user.setFriends(friends); // добавление нового поля
                    return UserMapper.mapToUserDto(user);
                })
                .orElseThrow(() -> new NoFoundIdException("Пользователь не найден с ID: " + userId));
    }

    public void addFriendById(FriendshipDto dto) {
        friendshipRepository.save(FriendshipMapper.mapToFriendship(dto));
    }

    public Set<Integer> getFriendsById(int id) {
        return friendshipRepository.getFriendsById(id);
    }

    public void deleteFriendById(int userId1, int userId2) {
        friendshipRepository.deleteFriendById(userId1, userId2);
    }

    public Set<Integer> getMutualFriends(int userId1, int userId2) {
        return friendshipRepository.findMutualFriends(userId1, userId2);
    }


    public boolean validateUser(UserDto user) {
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
