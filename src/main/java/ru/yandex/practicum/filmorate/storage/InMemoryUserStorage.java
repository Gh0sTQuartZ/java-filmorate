package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.model.*;

import java.util.*;
import java.util.stream.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long, User> userStorage = new HashMap<>();
    private final HashMap<Long, Set<Long>> friends = new HashMap<>();

    public List<User> getAll() {
        return new ArrayList<>(userStorage.values());
    }

    @Override
    public Optional<User> get(long id) {
        return Optional.ofNullable(userStorage.get(id));
    }

    @Override
    public User create(final User user) {
        userStorage.put(user.getId(), user);
        friends.put(user.getId(), new HashSet<>());
        return user;
    }

    @Override
    public User update(final User user) {
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getFriends(final long id) {
        return friends.get(id)
                .stream()
                .map(userStorage::get)
                .collect(Collectors.toList());
    }

    @Override
    public void addFriend(final long id, final long friendId) {
        friends.get(id).add(friendId);
        friends.get(friendId).add(id);
    }

    @Override
    public void deleteFriend(final long id, final long friendId) {
        friends.get(id).remove(friendId);
        friends.get(friendId).remove(id);
    }

    @Override
    public List<User> getCommonFriends(final long userId, final long otherUserId) {
        Set<Long> commons = new HashSet<>(friends.get(userId));
        commons.retainAll(new HashSet<>(friends.get(otherUserId)));

        return commons.stream()
                .map(userStorage::get)
                .collect(Collectors.toList());
    }
}
