package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.*;

import javax.validation.*;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Получение списка всех пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable final long id) {
        log.info("Получение пользователя id={}", id);
        return userService.getUser(id);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody final User user) {
        log.info("Добавление пользователя, присвоенный id={}", user.getId());
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody final User user) {
        log.info("Обновление пользователя id={}", user.getId());
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable final long id, @PathVariable final long friendId) {
        log.info("Пользователь id={} добавляет пользователя id={} в друзья", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable final long id, @PathVariable final long friendId) {
        log.info("Пользователь id={} удаляет пользователя id={} из друзей", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable final long id) {
        log.info("Получение списка друзей пользователя id={}", id);
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getUsersCommonFriends(@PathVariable final long id, @PathVariable final long otherId) {
        log.info("Получение списка общих друзей пользователей id={} и id={}", id, otherId);
        return userService.getUsersCommonFriends(id, otherId);
    }
}