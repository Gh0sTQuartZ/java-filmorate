package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class IdNotFoundException extends RuntimeException {
    public IdNotFoundException(String message, int id) {
        super(message + id);
    }
}
