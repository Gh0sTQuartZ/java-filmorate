package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.*;

import java.util.*;

@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreController extends RestControllerAdviser {
    private final GenreServiceImpl genreService;

    @Autowired
    public GenreController(final GenreServiceImpl genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable final Long id) {
        return genreService.getGenre(id);
    }
}
