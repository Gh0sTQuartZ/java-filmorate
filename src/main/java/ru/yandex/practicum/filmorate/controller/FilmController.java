package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.*;

import javax.validation.*;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmServiceImpl filmServiceImpl;

    @Autowired
    public FilmController(FilmServiceImpl filmServiceImpl) {
        this.filmServiceImpl = filmServiceImpl;
    }

    @GetMapping
    public List<Film> getAllFilms() throws SQLException {
        log.info("Получение списка всех фильмов");
        return filmServiceImpl.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable final long id) throws SQLException {
        log.info("Получение фильма id={}", id);
        return filmServiceImpl.getFilm(id);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody final Film film) {
        log.info("Добавление фильма, присвоенный id=", film.getId());
        return filmServiceImpl.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody final Film film) throws SQLException {
        log.info("Обновление фильма id={}", film.getId());
        return filmServiceImpl.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable final long id, @PathVariable final long userId) throws SQLException {
        log.info("Пользователь id={} ставит лайк фильму id={}", userId, id);
        filmServiceImpl.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable final long id, @PathVariable final long userId) throws SQLException {
        log.info("Пользователь id={} удаляет лайк фильма id={}", userId, id);
        filmServiceImpl.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") final Long count) throws SQLException {
        log.info("Получение списка популярных фильмов, размер={}", count);
        return filmServiceImpl.getPopularFilms(count);
    }
}