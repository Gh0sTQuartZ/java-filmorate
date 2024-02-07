package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.*;
import java.util.stream.*;

import static java.lang.Long.*;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private long idCounter = 1;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAllFilms() {
        log.info("Получение списка всех фильмов");
        return filmStorage.getAll();
    }

    public Film getFilm(final String idParam) {
        long id = parseLong(idParam);

        if (!filmStorage.contains(id)) {
            throw new NotFoundException("id фильма не найден: ", id);
        }
        log.info("Получение фильма id={}", id);
        return filmStorage.get(id);
    }

    public Film createFilm(final Film film) {
        film.setId(idCounter++);
        film.setLikes(new HashSet<>());
        log.info("Добавление фильма, присвоенный id=", film.getId());
        return filmStorage.create(film);
    }

    public Film updateFilm(final Film film) {
        if (film.getId() == null) {
            throw new ValidationException("id фильма не передан");
        }
        if (!filmStorage.contains(film.getId())) {
            throw new NotFoundException("id фильма не найден: ", film.getId());
        }
        film.setLikes(filmStorage.get(film.getId()).getLikes());
        log.info("Обновление фильма id={}", film.getId());
        return filmStorage.update(film);
    }

    public void addLike(final String filmIdParam, final String userIdParam) {
        long filmId = parseLong(filmIdParam);
        long userId = parseLong(userIdParam);

        if (!filmStorage.contains(filmId)) {
            throw new NotFoundException("id фильма не найден: ", filmId);
        }
        if (!userStorage.contains(userId)) {
            throw new NotFoundException("id пользователя не найден: ", userId);
        }
        log.info("Пользователь id={} ставит лайк фильму id={}", userId, filmId);
        filmStorage.get(filmId).addLike(userId);
    }


    public void deleteLike(String filmIdParam, String userIdParam) {
        long filmId = parseLong(filmIdParam);
        long userId = parseLong(userIdParam);

        if (!filmStorage.contains(filmId)) {
            throw new NotFoundException("id фильма не был найден: ", filmId);
        }
        if (!userStorage.contains(userId)) {
            throw new NotFoundException("id пользователя не был найден: ", userId);
        }
        log.info("Пользователь id={} удаляет лайк фильма id={}", userId, filmId);
        filmStorage.get(filmId).deleteLike(userId);
    }

    public List<Film> getPopularFilms(String sizeParam) {
        int range = Integer.parseInt(sizeParam);

        List<Film> sorted = filmStorage.getAll().stream()
                .sorted((a, b) -> Integer.compare(b.getLikes().size(), a.getLikes().size()))
                .collect(Collectors.toList());

        List<Film> ranged = new ArrayList<>();
        for (int i = 0; i < range && i < sorted.size(); i++) {
            ranged.add(sorted.get(i));
        }

        log.info("Получение списка популярных фильмов, размер={}", range);
        return ranged;
    }
}