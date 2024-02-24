package ru.yandex.practicum.filmorate.controller;

import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.*;

import java.util.*;

@RestController
@RequestMapping("/genres")
@Slf4j
@RequiredArgsConstructor
public class GenreController extends RestControllerAdviser {
    private final GenreService genreService;

    @GetMapping
    public List<Genre> getAll() {
        log.info("Получение списка всех жанров");
        return genreService.getAll();
    }

    @GetMapping("/{id}")
    public Genre get(@PathVariable final Long id) {
        log.info("Получение жанра id={}", id);
        return genreService.get(id);
    }
}
