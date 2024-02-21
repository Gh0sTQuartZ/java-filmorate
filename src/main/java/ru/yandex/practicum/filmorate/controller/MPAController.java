package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.*;

import java.util.*;

@RestController
@RequestMapping("mpa")
public class MPAController extends RestControllerAdviser {
    private final MPAServiceImpl mpaService;

    @Autowired
    public MPAController(final MPAServiceImpl mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public List<MPA> getAllMPA() {
        return mpaService.getAllMPA();
    }

    @GetMapping("/{id}")
    public MPA getMpa(@PathVariable final Long id) {
        return mpaService.getMPA(id);
    }
}
