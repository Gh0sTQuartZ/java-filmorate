package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.model.*;

import java.util.*;
import java.util.stream.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> filmStorage = new HashMap<>();
    private final HashMap<Long, Set<Long>> likes = new HashMap<>();

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(filmStorage.values());
    }

    @Override
    public Optional<Film> get(long id) {
        return Optional.ofNullable(filmStorage.get(id));
    }

    @Override
    public Film create(final Film film) {
        filmStorage.put(film.getId(), film);
        likes.put(film.getId(), new HashSet<>());
        return film;
    }

    @Override
    public Film update(final Film film) {
        filmStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public void addLike(final long id, final long userId) {
        likes.get(id).add(userId);
    }

    @Override
    public void deleteLike(final long id, final long userId) {
        likes.get(id).remove(userId);
    }

    @Override
    public List<Film> getPopularFilms(final long size) {
        List<Film> sorted = filmStorage.values().stream()
                .sorted((a, b) -> Integer.compare(likes.get(b.getId()).size(), likes.get(a.getId()).size()))
                .collect(Collectors.toList());

        List<Film> ranged = new ArrayList<>();
        for (int i = 0; i < size && i < sorted.size(); i++) {
            ranged.add(sorted.get(i));
        }

        return ranged;
    }
}
