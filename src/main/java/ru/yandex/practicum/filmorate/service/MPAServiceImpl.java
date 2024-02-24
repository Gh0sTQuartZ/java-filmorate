package ru.yandex.practicum.filmorate.service;

import lombok.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MPAServiceImpl implements MPAService {
    private final MPAStorage mpaStorage;

    @Override
    public List<MPA> getAll() {
        return mpaStorage.getAll();
    }

    @Override
    public MPA get(Long id) {
        MPA mpa = mpaStorage.get(id)
                .orElseThrow(() -> new NotFoundException("id рейтинга не найден: ", id));
        return mpa;
    }
}