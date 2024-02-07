package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validation.*;

import javax.validation.constraints.*;
import java.time.*;
import java.util.*;

@Data
@AllArgsConstructor
public class Film {
    Long id;
    @NotBlank String name;
    @NotBlank @Size(max = 200) String description;
    @FilmReleaseDateConstraint
    LocalDate releaseDate;
    @Positive int duration;
    Set<Long> likes;

    public void addLike(final long id) {
        likes.add(id);
    }

    public void deleteLike(final long id) {
        likes.remove(id);
    }
}