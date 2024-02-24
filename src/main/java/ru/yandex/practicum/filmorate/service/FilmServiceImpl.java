package ru.yandex.practicum.filmorate.service;

import lombok.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;
    private final MPAStorage mpaStorage;

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film get(final long id) {
        Film film = filmStorage.get(id)
                .orElseThrow(() -> new NotFoundException("id фильма не найден: ", id));
        return film;
    }

    @Override
    public Film create(final Film film) {
        MPA mpa = film.getMpa();
        if (mpa != null) {
            mpaStorage.get(mpa.getId())
                    .orElseThrow(() -> new NotFoundException("id рейтинга не был найден: ", mpa.getId()));
        }
        LinkedHashSet<Genre> genres = film.getGenres();
        if (genres != null && !genres.isEmpty()) {
            for (Genre genre : genres) {
                genreStorage.get(genre.getId())
                        .orElseThrow(() -> new NotFoundException("id жанра не был найден: ", genre.getId()));
            }
        }

        return filmStorage.create(film);
    }

    @Override
    public Film update(final Film film) {
        if (film.getId() == null) {
            throw new ValidationException("id фильма не передан");
        }
        filmStorage.get(film.getId())
                .orElseThrow(() -> new NotFoundException("id фильма не найден: ", film.getId()));

        MPA mpa = film.getMpa();
        if (mpa != null) {
            mpaStorage.get(mpa.getId())
                    .orElseThrow(() -> new NotFoundException("id рейтинга не был найден: ", mpa.getId()));
        }
        LinkedHashSet<Genre> genres = film.getGenres();
        if (genres != null && !genres.isEmpty()) {
            for (Genre genre : genres) {
                genreStorage.get(genre.getId())
                        .orElseThrow(() -> new NotFoundException("id жанра не был найден: ", genre.getId()));
            }
        }
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
    public List<Film> getPopular(final long size) {
        return filmStorage.getPopular(size);
    }


}