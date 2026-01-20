package ru.yandex.practicum.homeTheatre.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.homeTheatre.dal.FriendshipRepository;
import ru.yandex.practicum.homeTheatre.dto.FriendshipDto;
import ru.yandex.practicum.homeTheatre.mapper.FriendshipMapper;


import java.util.Set;


@Slf4j
@Service
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;

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
}
