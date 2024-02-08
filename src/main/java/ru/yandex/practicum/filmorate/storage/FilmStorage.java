package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

public interface FilmStorage {
    List<Film> getAll();

    Optional<Film> get(final long id);

    Film create(final Film film);

    Film update(final Film film);

    void addLike(final long id, final long userId);

    void deleteLike(final long id, final long userId);

    List<Film> getPopularFilms(final long size);
}
