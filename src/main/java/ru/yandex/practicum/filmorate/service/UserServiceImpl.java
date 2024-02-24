package ru.yandex.practicum.filmorate.service;

import lombok.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User get(final long id) {
        User user = userStorage.get(id)
                .orElseThrow(() -> new NotFoundException("id пользователя не найден: ", id));
        return user;
    }

    @Override
    public User create(final User user) {
        initName(user);
        return userStorage.create(user);
    }

    @Override
    public User update(final User user) {
        if (user.getId() == null) {
            throw new ValidationException("Id пользователя не передан");
        }
        userStorage.get(user.getId())
                .orElseThrow(() -> new NotFoundException("id пользователя не найден: ", user.getId())
                );

        initName(user);
        return userStorage.update(user);
    }

    @Override
    public void addFriend(final long userId, final long friendId) {
        userStorage.get(userId)
                .orElseThrow(() -> new NotFoundException("id пользователя не найден: ", userId));
        userStorage.get(friendId)
                .orElseThrow(() -> new NotFoundException("id друга не найден: ", friendId));

        userStorage.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(final long userId, final long friendId) {
        userStorage.get(userId)
                .orElseThrow(() -> new NotFoundException("id пользователя не найден: ", userId));
        userStorage.get(friendId)
                .orElseThrow(() -> new NotFoundException("id друга не найден: ", friendId));

        userStorage.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getFriends(final long userId) {
        userStorage.get(userId)
                .orElseThrow(() -> new NotFoundException("id пользователя не найден: ", userId));

        return userStorage.getFriends(userId);
    }

    @Override
    public List<User> getCommonFriends(final long userId, final long otherUserId) {
        userStorage.get(userId)
                .orElseThrow(() -> new NotFoundException("id пользователя не найден: ", userId));
        userStorage.get(otherUserId)
                .orElseThrow(() -> new NotFoundException("id другого пользователя не найден: ", otherUserId));

        return userStorage.getCommonFriends(userId, otherUserId);
    }

    private void initName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}