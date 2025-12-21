package ru.yandex.practicum.homeTheatre.storage.user;

import ru.yandex.practicum.homeTheatre.model.User;

import java.util.Collection;

public interface UserStorage {
    void addUser(User user);

    void removeUser(int id);

    User getUser(int id);

    Collection<User> getAllUsers();

    void updateUser(User user);

    void deleteFriendById(int yourId, int friendId);

    void addFriendById(int yourId, int friendId);

    Collection<User> getFriends(int userId);

    Collection<User> getMutualFriends(int user1, int user2);

    boolean exists(User user);

}