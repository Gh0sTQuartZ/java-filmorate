package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class Genre {
    Long id;
    String name;
}
