package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.*;

@Service
public class FilmService implements FilmServiceInterface {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private long idCounter = 1;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    @Override
    public Film getFilm(final long id) {
        Film film = filmStorage.get(id)
                .orElseThrow(() -> new NotFoundException("id фильма не найден: ", id));
        return film;
    }

    @Override
    public Film createFilm(final Film film) {
        film.setId(idCounter++);
        return filmStorage.create(film);
    }

    @Override
    public Film updateFilm(final Film film) {
        if (film.getId() == null) {
            throw new ValidationException("id фильма не передан");
        }
        filmStorage.get(film.getId())
                .orElseThrow(() -> new NotFoundException("id фильма не найден: ", film.getId()));

        return filmStorage.update(film);
    }

    @Override
    public void addLike(final long filmId, final long userId) {
        filmStorage.get(filmId)
                .orElseThrow(() -> new NotFoundException("id фильма не найден: ", filmId));
        userStorage.get(userId)
                .orElseThrow(() -> new NotFoundException("id пользователя не найден: ", userId));

        filmStorage.addLike(filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        filmStorage.get(filmId)
                .orElseThrow(() -> new NotFoundException("id фильма не найден: ", filmId));
        userStorage.get(userId)
                .orElseThrow(() -> new NotFoundException("id пользователя не найден: ", userId));

        filmStorage.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(final long size) {
        return filmStorage.getPopularFilms(size);
    }
}