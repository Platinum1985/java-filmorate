package ru.yandex.practicum.homeTheatre.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.homeTheatre.dto.FriendshipDto;
import ru.yandex.practicum.homeTheatre.model.Friendship;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FriendshipMapper {
    public static Friendship mapToFriendship(FriendshipDto dto) {
        Friendship friendship = new Friendship();
        friendship.setId(dto.getId());
        friendship.setUserId_1(dto.getUserId1());
        friendship.setUserId_2(dto.getUserId2());
        return friendship;
    }

    public static FriendshipDto mapToFriendshipDto(Friendship friendship) {
        FriendshipDto friendshipDto = new FriendshipDto();
        friendshipDto.setId(friendship.getId());
        friendshipDto.setUserId1(friendship.getUserId_1());
        friendshipDto.setUserId2(friendship.getUserId_2());
        return friendshipDto;
    }

}
