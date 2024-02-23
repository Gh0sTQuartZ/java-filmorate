package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

public interface FilmService {
    List<Film> getAll();

    Film get(final long id);

    Film create(final Film film);

    Film update(final Film film);

    void addLike(final long filmId, final long userId);

    void deleteLike(final long filmId, final long userId);

    List<Film> getPopular(final long size);
}
