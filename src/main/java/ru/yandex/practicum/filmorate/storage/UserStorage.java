package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

public interface UserStorage {
    List<User> getAll();

    User get(final long id);

    boolean contains(final long id);

    User create(final User user);

    User update(final User user);
}
