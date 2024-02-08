package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

@Component
@Slf4j
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
    public Set<Long> getFriends(final long id) {
        return friends.get(id);
    }

    @Override
    public void addFriend(final long id, final long friendId) {
        friends.get(id).add(friendId);
    }

    @Override
    public void deleteFriend(final long id, final long friendId) {
        friends.get(id).remove(friendId);
    }
}
