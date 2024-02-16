package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;

@RestControllerAdvice
@Slf4j
public class RestControllerAdviser {
    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleNotFoundException(final NotFoundException exception) {
        final ErrorDto errorDto = new ErrorDto(exception.getClass().getName(), exception.getMessage());
        log.warn(exception.getMessage());
        return errorDto;
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleValidationException(final ValidationException exception) {
        final ErrorDto errorDto = new ErrorDto(exception.getClass().getName(), exception.getMessage());
        log.warn(exception.getMessage());
        return errorDto;
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleException(final Exception exception) {
        final ErrorDto errorDto = new ErrorDto(exception.getClass().getName(), exception.getMessage());
        log.warn(exception.getMessage(), exception);
        return errorDto;
    }
}
