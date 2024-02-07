package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.*;

import javax.servlet.http.*;
import javax.validation.*;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable final String id) {
        return filmService.getFilm(id);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody final Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody final Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable final String id, @PathVariable final String userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable final String id, @PathVariable final String userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") final String count) {
        return filmService.getPopularFilms(count);
    }

    @ExceptionHandler()
    public ModelAndView handleNotFoundException(final HttpServletRequest request, final NotFoundException exception) {
        final ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("message", exception.getMessage());
        mav.addObject("url", request.getRequestURL());
        mav.setStatus(HttpStatus.NOT_FOUND);

        log.warn(exception.getMessage(), exception);
        return mav;
    }

    @ExceptionHandler()
    public ModelAndView handleValidationException(final HttpServletRequest request, final ValidationException exception) {
        final ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("message", exception.getMessage());
        mav.addObject("url", request.getRequestURL());
        mav.setStatus(HttpStatus.BAD_REQUEST);

        log.warn(exception.getMessage(), exception);
        return mav;
    }

    @ExceptionHandler()
    public ModelAndView handleException(final HttpServletRequest request, final Exception exception) {
        final ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("message", exception.getMessage());
        mav.addObject("url", request.getRequestURL());
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        log.warn(exception.getMessage(), exception);
        return mav;
    }
}