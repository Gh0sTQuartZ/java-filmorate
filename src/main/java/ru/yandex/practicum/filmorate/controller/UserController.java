package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.*;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;

import javax.validation.*;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashMap<Integer, User> userStorage = new HashMap<>();
    private int idCounter = 1;

    @GetMapping
    public List<User> getAll() {
        log.info("Получен GET запрос на эндпоинт /users");

        return new ArrayList<>(userStorage.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody final User user) {
        log.info("Получен POST запрос на эндпоинт /users");

        user.setId(idCounter++);
        initName(user);

        userStorage.put(user.getId(), user);
        log.info("Добавлен новый пользователь: {}", user);

        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody final User user) throws NotFoundException {
        log.info("Получен PUT запрос на эндпоинт /users");

        if (user.getId() == null) {
            throw new ValidationException("Айди пользователя не был передан");
        }
        if (!userStorage.containsKey(user.getId())) {
            throw new NotFoundException("id пользователя не найден, id: ", user.getId());
        }
        initName(user);

        userStorage.put(user.getId(), user);
        log.info("Пользователь с идентификационным номером {} обновлён, данные обновлённого пользователя: {}",
                user.getId(), user);

        return user;
    }

    private void initName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}