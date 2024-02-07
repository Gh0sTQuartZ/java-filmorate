package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> filmStorage = new HashMap<>();

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(filmStorage.values());
    }

    @Override
    public Film get(long id) {
        return filmStorage.get(id);
    }

    @Override
    public boolean contains(long id) {
        return filmStorage.containsKey(id);
    }

    @Override
    public Film create(final Film film) {
        filmStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(final Film film) {
        filmStorage.put(film.getId(), film);
        return film;
    }
}
