package ru.yandex.practicum.homeTheatre.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.homeTheatre.model.User;
import ru.yandex.practicum.homeTheatre.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

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
        System.out.println("вкл метод из UserService");
        userStorage.addUser(user);
    }

    public void updateUser(User user) {
        userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        return userStorage.getUser(id);
    }
}
