package ru.yandex.practicum.filmorate.validation;

import javax.validation.*;
import java.time.*;

public class FilmReleaseDateConstraintValidator implements ConstraintValidator<FilmReleaseDateConstraint, LocalDate> {
    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext context) {
        if (releaseDate == null) {
            return false;
        }
        return releaseDate.isAfter(LocalDate.of(1895, 12, 27));
    }
}
