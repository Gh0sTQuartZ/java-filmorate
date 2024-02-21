package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

public interface MPAService {
    List<MPA> getAllMPA();

    MPA getMPA(final Long id);
}