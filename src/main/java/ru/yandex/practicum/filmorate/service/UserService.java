package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.*;
import java.util.stream.*;

import static java.lang.Long.*;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private long idCounter = 1;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {
        log.info("Получение списка всех пользователей");
        return userStorage.getAll();
    }

    public User getUser(final String idParam) {
        long id = parseLong(idParam);

        if (!userStorage.contains(id)) {
            throw new NotFoundException("id пользователя не найден: ", id);
        }
        log.info("Получение фильма id={}", id);
        return userStorage.get(id);
    }

    public User createUser(final User user) {
        user.setId(idCounter++);
        initName(user);
        user.setFriends(new HashSet<>());
        log.info("Добавление фильма, присвоенный id={}", user.getId());
        return userStorage.create(user);
    }

    public User updateUser(final User user) {
        if (user.getId() == null) {
            throw new ValidationException("Id пользователя не передан");
        }
        if (!userStorage.contains(user.getId())) {
            throw new NotFoundException("id пользователя не найден: ", user.getId());
        }

        initName(user);
        user.setFriends(userStorage.get(user.getId()).getFriends());
        log.info("Обновление фильма id={}", user.getId());
        return userStorage.update(user);
    }

    public void addFriend(final String userIdParam, final String friendIdParam) {
        final long userId = parseLong(userIdParam);
        final long friendId = parseLong(friendIdParam);

        if (!userStorage.contains(userId)) {
            throw new NotFoundException("id пользователя не найден: ", userId);
        }
        if (!userStorage.contains(friendId)) {
            throw new NotFoundException("id друга не найден: ", friendId);
        }

        log.info("Пользователь id={} добавляет пользователя id={} в друзья", userId, friendId);
        userStorage.get(userId).addFriend(friendId);
        userStorage.get(friendId).addFriend(userId);
    }


    public void deleteFriend(final String userIdParam, final String friendIdParam) {
        final long userId = parseLong(userIdParam);
        final long friendId = parseLong(friendIdParam);

        if (!userStorage.contains(userId)) {
            throw new NotFoundException("id пользователя не найден: ", userId);
        }
        if (!userStorage.contains(friendId)) {
            throw new NotFoundException("id друга не найден: ", friendId);
        }

        log.info("Пользователь id={} удаляет пользователя id={} из друзей", userId, friendId);
        userStorage.get(userId).deleteFriend(friendId);
        userStorage.get(friendId).deleteFriend(userId);
    }


    public List<User> getUserFriends(String userIdParam) {
        final long userId = parseLong(userIdParam);

        if (!userStorage.contains(userId)) {
            throw new NotFoundException("id пользователя не найден: ", userId);
        }

        log.info("Получение списка друзей пользователя id={}", userId);
        return userStorage.get(userId).getFriends()
                .stream()
                .map(userStorage::get)
                .collect(Collectors.toList());
    }

    public List<User> getUsersCommonFriends(String userIdParam, String otherUserIdParam) {
        final long userId = parseLong(userIdParam);
        final long otherUserId = parseLong(otherUserIdParam);

        if (!userStorage.contains(userId)) {
            throw new NotFoundException("id пользователя не найден: ", userId);
        }
        if (!userStorage.contains(otherUserId)) {
            throw new NotFoundException("id другого пользователя не найден: ", otherUserId);
        }

        Set<Long> commons = new HashSet<>(userStorage.get(userId).getFriends());
        commons.retainAll(new HashSet<>(userStorage.get(otherUserId).getFriends()));

        log.info("Получение списка общих друзей пользователей id={} и id={}", userId, otherUserId);
        return commons.stream()
                .map(userStorage::get)
                .collect(Collectors.toList());
    }

    private void initName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}