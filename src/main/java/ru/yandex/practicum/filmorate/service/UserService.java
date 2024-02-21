package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

public interface UserService {
    List<User> getAllUsers();

    User getUser(final long id);

    User createUser(final User user);

    User updateUser(final User user);

    void addFriend(final long userId, final long friendId);

    void deleteFriend(final long userId, final long friendId);

    List<User> getUserFriends(final long userId);

    List<User> getUsersCommonFriends(final long userId, final long otherUserId);
}
