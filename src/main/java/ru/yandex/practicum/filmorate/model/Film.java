package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validation.*;

import javax.validation.constraints.*;
import java.time.*;

@Data
@AllArgsConstructor
public class Film {
    Long id;
    @NotBlank String name;
    @NotBlank @Size(max = 200) String description;
    @FilmReleaseDateConstraint
    LocalDate releaseDate;
    @Positive int duration;
}