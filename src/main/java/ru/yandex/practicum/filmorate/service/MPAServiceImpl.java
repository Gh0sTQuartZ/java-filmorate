package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.*;

@Service
public class MPAServiceImpl implements MPAService {
    private final MPAStorage mpaStorage;

    @Autowired
    public MPAServiceImpl(final MPAStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Override
    public List<MPA> getAllMPA() {
        return mpaStorage.getAll();
    }

    @Override
    public MPA getMPA(Long id) {
        MPA mpa = mpaStorage.get(id)
                .orElseThrow(() -> new NotFoundException("id рейтинга не найден: ", id));
        return mpa;
    }
}