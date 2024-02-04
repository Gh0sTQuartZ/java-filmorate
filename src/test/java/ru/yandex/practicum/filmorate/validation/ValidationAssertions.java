package ru.yandex.practicum.filmorate.validation;

import javax.validation.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationAssertions {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().usingContext().getValidator();

    private ValidationAssertions() {
    }

    public static <T> void assertValid(T object) {
        Set<ConstraintViolation<T>> validate = validator.validate(object);
        assertTrue(validate.isEmpty(), "Объект не прошёл валидацию");
    }

    public static <T> void assertNotValid(T object) {
        Set<ConstraintViolation<T>> validate = validator.validate(object);
        assertFalse(validate.isEmpty(), "Объект прошёл валидацию");
    }
}