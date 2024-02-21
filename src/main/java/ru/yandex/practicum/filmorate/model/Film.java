package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validation.*;

import javax.validation.constraints.*;
import java.time.*;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
public class Film {
    Long id;
    @NotBlank String name;
    @NotBlank @Size(max = 200) String description;
    @FilmReleaseDateConstraint
    LocalDate releaseDate;
    @Positive int duration;
    Set<Genre> genres;
    MPA mpa;
}