package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

public abstract class InMemoryController<T extends StorageData> {
    protected final HashMap<Integer, T> storage = new HashMap<>();
    private int idCounter = 1;
    @GetMapping
    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }
    @PostMapping
    public T create(final T data) {
        data.setId(idCounter++);
        storage.put(data.getId(), data);
        return data;
    }
    @PutMapping
    public T update(final T data) throws IdNotFoundException {
        if (!storage.containsKey(data.getId())) {
            throw new IdNotFoundException("id не найден: ", data.getId());
        }
        storage.put(data.getId(), data);
        return data;
    }
}