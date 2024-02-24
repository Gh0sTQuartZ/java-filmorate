package ru.yandex.practicum.filmorate.controller;

import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.*;

import javax.validation.*;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmServiceImpl;

    @GetMapping
    public List<Film> getAll() {
        log.info("Получение списка всех фильмов");
        return filmServiceImpl.getAll();
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable final long id) {
        log.info("Получение фильма id={}", id);
        return filmServiceImpl.get(id);
    }

    @PostMapping
    public Film create(@Valid @RequestBody final Film film) {
        log.info("Добавление фильма, присвоенный id=", film.getId());
        return filmServiceImpl.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody final Film film) {
        log.info("Обновление фильма id={}", film.getId());
        return filmServiceImpl.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable final long id, @PathVariable final long userId) {
        log.info("Пользователь id={} ставит лайк фильму id={}", userId, id);
        filmServiceImpl.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable final long id, @PathVariable final long userId) {
        log.info("Пользователь id={} удаляет лайк фильма id={}", userId, id);
        filmServiceImpl.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") final Long count) {
        log.info("Получение списка популярных фильмов, размер={}", count);
        return filmServiceImpl.getPopular(count);
    }
}