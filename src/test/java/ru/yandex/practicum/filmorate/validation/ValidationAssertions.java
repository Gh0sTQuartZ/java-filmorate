package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.model.*;

import static org.junit.jupiter.api.Assertions.*;

import javax.validation.*;
import javax.validation.constraints.*;
import java.lang.annotation.*;
import java.util.*;

public class ValidationAssertions {
    private ValidationAssertions() {
    }

    private static Validator validator = Validation.buildDefaultValidatorFactory().usingContext().getValidator();

    public static <T> void assertValid(T object) {
        Set<ConstraintViolation<T>> validate = validator.validate(object);
        assertTrue(validate.isEmpty(), "Объект не прошёл валидацию");
    }

    public static <T> void assertNotValid(T object) {
        Set<ConstraintViolation<T>> validate = validator.validate(object);
        assertTrue(!validate.isEmpty(), "Объект прошёл валидацию");
    }
}