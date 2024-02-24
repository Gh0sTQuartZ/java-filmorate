package ru.yandex.practicum.filmorate.service;

import lombok.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreStorage genreStorage;

    @Override
    public List<Genre> getAll() {
        return genreStorage.getAll();
    }

    @Override
    public Genre get(Long id) {
        Genre genre = genreStorage.get(id)
                .orElseThrow(() -> new NotFoundException("id жанра не найден: ", id));
        return genre;
    }
}