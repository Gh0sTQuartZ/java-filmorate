package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

public interface MPAStorage {
    List<MPA> getAll();

    Optional<MPA> get(final long id);
}
