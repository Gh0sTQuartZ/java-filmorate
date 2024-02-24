package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

public interface MPAService {
    List<MPA> getAll();

    MPA get(final Long id);
}