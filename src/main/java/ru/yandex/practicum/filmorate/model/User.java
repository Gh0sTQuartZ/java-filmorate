package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validation.*;

import javax.validation.constraints.*;
import java.time.*;

@Data
@AllArgsConstructor
public class User {
    @NotNull
    int id;
    @NotNull
    @Email
    String email;
    @UserLoginConstraint
    String login;
    String name;
    @NotNull
    @Past
    LocalDate birthday;
}
