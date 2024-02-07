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
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable final String id) {
        return userService.getUser(id);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody final User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody final User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable final String id, @PathVariable final String friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable final String id, @PathVariable final String friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable final String id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getUsersCommonFriends(@PathVariable final String id, @PathVariable final String otherId) {
        return userService.getUsersCommonFriends(id, otherId);
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