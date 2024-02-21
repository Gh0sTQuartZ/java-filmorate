package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.*;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreServiceImpl(final GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreStorage.getAll();
    }

    @Override
    public Genre getGenre(Long id) {
        Genre genre = genreStorage.get(id)
                .orElseThrow(() -> new NotFoundException("id жанра не найден: ", id));
        return genre;
    }
}