package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
public class ErrorDto<T extends Throwable> {
    private final Class<T> exceptionClass;
    private final String message;

    public ErrorDto(final Class<T> exceptionClass, final String message) {
        this.exceptionClass = exceptionClass;
        this.message = message;
    }
}
