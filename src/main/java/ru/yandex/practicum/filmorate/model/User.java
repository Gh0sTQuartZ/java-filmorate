package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validation.*;

import javax.validation.constraints.*;
import java.time.*;
import java.util.*;

@Data
@AllArgsConstructor
public class User {
    Long id;
    @NotNull
    @Email
    String email;
    @UserLoginConstraint
    String login;
    String name;
    @NotNull
    @Past
    LocalDate birthday;
    Set<Long> friends;

    public void addFriend(final long id) {
        friends.add(id);
    }

    public void deleteFriend(final long id) {
        friends.remove(id);
    }
}