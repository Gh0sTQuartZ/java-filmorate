package ru.yandex.practicum.filmorate.validation;

import javax.validation.*;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FilmReleaseDateConstraintValidator.class)
public @interface FilmReleaseDateConstraint {
    String message() default "must be a Date after 1895.12.27." +
            " found: ${validatedValue}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}