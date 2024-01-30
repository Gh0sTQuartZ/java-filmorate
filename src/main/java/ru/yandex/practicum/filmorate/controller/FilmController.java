package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.*;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;

import javax.validation.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends InMemoryController<Film> {
    @Override
    @PostMapping
    public Film create(@Valid @RequestBody Film body) {
        Film createdFilm = super.create(body);
        log.info("Добавлен новый фильм: {}", body);
        return createdFilm;
    }

    @Override
    @PutMapping
    public Film update(@Valid @RequestBody Film body) throws IdNotFoundException {
        Film updatedFilm = super.update(body);
        log.info("Фильм с идентификационным номером {} обновлён, данные обновлённого фильма: {}",
                updatedFilm.getId(), updatedFilm);
        return updatedFilm;
    }
}
