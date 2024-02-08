package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

public interface UserStorage {
    List<User> getAll();

    Optional<User> get(final long id);

    User create(final User user);

    User update(final User user);

    Set<Long> getFriends(final long id);

    void addFriend(final long id, final long friendId);

    void deleteFriend(final long id, final long friendId);
}
