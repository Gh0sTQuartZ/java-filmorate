package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.*;

import java.sql.*;
import java.util.*;

public interface FilmService {
    List<Film> getAllFilms() throws SQLException;

    Film getFilm(final long id) throws SQLException;

    Film createFilm(final Film film);

    Film updateFilm(final Film film) throws SQLException;

    void addLike(final long filmId, final long userId) throws SQLException;

    void deleteLike(final long filmId, final long userId) throws SQLException;

    List<Film> getPopularFilms(final long size) throws SQLException;
}
