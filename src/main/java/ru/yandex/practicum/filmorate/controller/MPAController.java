package ru.yandex.practicum.filmorate.controller;

import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.*;

import java.util.*;

@RestController
@RequestMapping("mpa")
@Slf4j
@RequiredArgsConstructor
public class MPAController extends RestControllerAdviser {
    private final MPAService mpaService;

    @GetMapping
    public List<MPA> getAll() {
        log.info("Получение списка всех рейтингов");
        return mpaService.getAll();
    }

    @GetMapping("/{id}")
    public MPA get(@PathVariable final Long id) {
        log.info("Получение рейтинга id={}", id);
        return mpaService.get(id);
    }
}
