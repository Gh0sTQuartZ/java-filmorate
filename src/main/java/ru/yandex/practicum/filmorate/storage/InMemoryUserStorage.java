package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long, User> userStorage = new HashMap<>();

    public List<User> getAll() {
        return new ArrayList<>(userStorage.values());
    }

    @Override
    public User get(long id) {
        return userStorage.get(id);
    }

    @Override
    public boolean contains(long id) {
        return userStorage.containsKey(id);
    }

    @Override
    public User create(final User user) {
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(final User user) {
        userStorage.put(user.getId(), user);
        return user;
    }
}
