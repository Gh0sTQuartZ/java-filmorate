package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.model.*;

import static org.junit.jupiter.api.Assertions.*;

import javax.validation.*;
import java.util.*;

public class ValidationAssertions {
    private ValidationAssertions() {
    }

    private static Validator validator = Validation.buildDefaultValidatorFactory().usingContext().getValidator();

    public static <T extends StorageData> void assertValid(T object) {
        Set<ConstraintViolation<T>> validate = validator.validate(object);
        assertTrue(validate.size() == 0, "Объект не прошёл валидацию");
    }

    public static <T extends StorageData> void assertNotValid(T object) {
        Set<ConstraintViolation<T>> validate = validator.validate(object);
        assertTrue(validate.size() != 0, "Объект прошёл валидацию");
        validate.stream().map(v -> v.getConstraintDescriptor()).forEach(System.out::println);
    }
}