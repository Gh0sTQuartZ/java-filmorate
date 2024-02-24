package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

public interface UserService {
    List<User> getAll();

    User get(final long id);

    User create(final User user);

    User update(final User user);

    void addFriend(final long userId, final long friendId);

    void deleteFriend(final long userId, final long friendId);

    List<User> getFriends(final long userId);

    List<User> getCommonFriends(final long userId, final long otherUserId);
}
