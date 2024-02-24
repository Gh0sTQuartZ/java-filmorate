package ru.yandex.practicum.filmorate.controller;

import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.*;

import javax.validation.*;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userServiceImpl;

    @GetMapping
    public List<User> getAll() {
        log.info("Получение списка всех пользователей");
        return userServiceImpl.getAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable final long id) {
        log.info("Получение пользователя id={}", id);
        return userServiceImpl.get(id);
    }

    @PostMapping
    public User create(@Valid @RequestBody final User user) {
        log.info("Добавление пользователя, присвоенный id={}", user.getId());
        return userServiceImpl.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody final User user) {
        log.info("Обновление пользователя id={}", user.getId());
        return userServiceImpl.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable final long id, @PathVariable final long friendId) {
        log.info("Пользователь id={} добавляет пользователя id={} в друзья", id, friendId);
        userServiceImpl.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable final long id, @PathVariable final long friendId) {
        log.info("Пользователь id={} удаляет пользователя id={} из друзей", id, friendId);
        userServiceImpl.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable final long id) {
        log.info("Получение списка друзей пользователя id={}", id);
        return userServiceImpl.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable final long id, @PathVariable final long otherId) {
        log.info("Получение списка общих друзей пользователей id={} и id={}", id, otherId);
        return userServiceImpl.getCommonFriends(id, otherId);
    }
}