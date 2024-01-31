package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.*;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;

import javax.validation.*;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> filmStorage = new HashMap<>();
    private int idCounter = 1;

    @GetMapping
    public List<Film> getAll() {
        log.info("Получен GET запрос на эндпоинт /films");

        return new ArrayList<>(filmStorage.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody final Film film) {
        log.info("Получен POST запрос на эндпоинт /films");

        film.setId(idCounter++);

        filmStorage.put(film.getId(), film);
        log.info("Добавлен новый фильм: {}", film);

        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody final Film film) throws NotFoundException {
        log.info("Получен PUT запрос на эндпоинт /films");

        if (!filmStorage.containsKey(film.getId())) {
            throw new NotFoundException("id не найден: ", film.getId());
        }

        filmStorage.put(film.getId(), film);
        log.info("Фильм с идентификационным номером {} обновлён, данные обновлённого фильма: {}", film.getId(), film);

        return film;
    }
}
