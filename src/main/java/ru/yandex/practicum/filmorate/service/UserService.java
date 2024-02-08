package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.*;
import java.util.stream.*;


@Service
@Slf4j
public class UserService implements UserServiceInterface {
    private final UserStorage userStorage;
    private long idCounter = 1;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получение списка всех пользователей");
        return userStorage.getAll();
    }

    @Override
    public User getUser(final long id) {
        User user = userStorage.get(id).orElseThrow(() -> new NotFoundException("id пользователя не найден: ", id));

        log.info("Получение фильма id={}", id);
        return user;
    }

    @Override
    public User createUser(final User user) {
        user.setId(idCounter++);
        initName(user);
        log.info("Добавление фильма, присвоенный id={}", user.getId());
        return userStorage.create(user);
    }

    @Override
    public User updateUser(final User user) {
        if (user.getId() == null) {
            throw new ValidationException("Id пользователя не передан");
        }
        userStorage.get(user.getId()).orElseThrow(
                () -> new NotFoundException("id пользователя не найден: ", user.getId())
        );

        initName(user);
        log.info("Обновление фильма id={}", user.getId());
        return userStorage.update(user);
    }

    @Override
    public void addFriend(final long userId, final long friendId) {
        userStorage.get(userId).orElseThrow(() -> new NotFoundException("id пользователя не найден: ", userId));
        userStorage.get(friendId).orElseThrow(() -> new NotFoundException("id друга не найден: ", friendId));

        log.info("Пользователь id={} добавляет пользователя id={} в друзья", userId, friendId);
        userStorage.addFriend(userId, friendId);
        userStorage.addFriend(friendId, userId);
    }

    @Override
    public void deleteFriend(final long userId, final long friendId) {
        userStorage.get(userId).orElseThrow(() -> new NotFoundException("id пользователя не найден: ", userId));
        userStorage.get(friendId).orElseThrow(() -> new NotFoundException("id друга не найден: ", friendId));

        log.info("Пользователь id={} удаляет пользователя id={} из друзей", userId, friendId);
        userStorage.deleteFriend(userId, friendId);
        userStorage.deleteFriend(friendId, userId);
    }

    @Override
    public List<User> getUserFriends(final long userId) {
        userStorage.get(userId).orElseThrow(() -> new NotFoundException("id пользователя не найден: ", userId));

        log.info("Получение списка друзей пользователя id={}", userId);
        return userStorage.getFriends(userId)
                .stream()
                .map((id) -> userStorage.get(id).get())
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getUsersCommonFriends(final long userId, final long otherUserId) {
        userStorage.get(userId).orElseThrow(() -> new NotFoundException("id пользователя не найден: ", userId));
        userStorage.get(otherUserId).orElseThrow(() -> new NotFoundException("id другого пользователя не найден: ", otherUserId));


        Set<Long> commons = new HashSet<>(userStorage.getFriends(userId));
        commons.retainAll(new HashSet<>(userStorage.getFriends(otherUserId)));

        log.info("Получение списка общих друзей пользователей id={} и id={}", userId, otherUserId);
        return commons.stream()
                .map((id) -> userStorage.get(id).get())
                .collect(Collectors.toList());
    }

    private void initName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}