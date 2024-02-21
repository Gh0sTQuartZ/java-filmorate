package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

public interface GenreStorage {
    List<Genre> getAll();

    Optional<Genre> get(final long id);
}
