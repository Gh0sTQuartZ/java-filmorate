package ru.yandex.practicum.filmorate.validation;

import javax.validation.*;

public class UserLoginConstraintValidator implements ConstraintValidator<UserLoginConstraint, String> {
    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        if (login == null) {
            return false;
        }
        return !login.contains(" ");
    }
}