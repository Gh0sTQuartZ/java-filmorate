package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

public interface GenreService {
    List<Genre> getAllGenres();

    Genre getGenre(final Long id);
}