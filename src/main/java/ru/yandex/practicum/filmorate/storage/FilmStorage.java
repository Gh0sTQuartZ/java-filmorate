package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

public interface FilmStorage {
    List<Film> getAll();

    Film get(final long id);

    boolean contains(final long id);

    Film create(final Film film);

    Film update(final Film film);
}
