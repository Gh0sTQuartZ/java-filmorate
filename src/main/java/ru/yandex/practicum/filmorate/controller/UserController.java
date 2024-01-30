package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.*;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;

import javax.validation.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends InMemoryController<User> {
    @Override
    public User create(@Valid @RequestBody final User body) {
        if (body.getName() == null || body.getName().isBlank()) {
            body.setName(body.getLogin());
        }
        User createdUser = super.create(body);
        log.info("Добавлен новый пользователь: {}", body);
        return createdUser;
    }

    @Override
    public User update(@Valid @RequestBody final User body) throws IdNotFoundException {
        if (body.getName() == null || body.getName().isBlank()) {
            body.setName(body.getLogin());
        }
        User updatedUser = super.update(body);
        log.info("Пользователь с идентификационным номером {} обновлён, данные обновлённого пользователя: {}",
                updatedUser.getId(), updatedUser);
        return updatedUser;
    }
}
