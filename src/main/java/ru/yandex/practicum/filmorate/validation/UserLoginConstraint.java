package ru.yandex.practicum.filmorate.validation;

import javax.validation.*;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserLoginConstraintValidator.class)
public @interface UserLoginConstraint {
    String message() default "must be a String without whitespace." +
            " found: ${validatedValue}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
