package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validation.*;

import javax.validation.constraints.*;
import java.time.*;

@Data
@AllArgsConstructor
public class User extends StorageData {
    @NotNull @Email String email;
    @UserLoginConstraint
    String login;
    String name;
    @Past @NotNull LocalDate birthday;
}
