package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

public interface FilmServiceInterface {
    List<Film> getAllFilms();

    Film getFilm(final long id);

    Film createFilm(final Film film);

    Film updateFilm(final Film film);

    void addLike(final long filmId, final long userId);

    void deleteLike(final long filmId, final long userId);

    List<Film> getPopularFilms(final long size);
}
