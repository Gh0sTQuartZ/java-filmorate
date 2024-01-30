package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.model.*;

import java.time.*;

public class FilmValidationTests {
    private Film film;

    @BeforeEach
    private void beforeEach() {
        // Перед каждым тестом приводим фильм к валидному состоянию
        film = new Film("testName", "testDescription",
                LocalDate.of(2001, 1, 1), 120);
    }

    @Test
    @DisplayName("Успешная валидация при валидных данных")
    public void shouldValidateWhenDataIsValid() {
        ValidationAssertions.assertValid(film);
    }

    @Test
    @DisplayName("Неуспешная валидация при пустом названии")
    public void shouldNotValidateWhenNameEmpty() {
        film.setName(" ");

        ValidationAssertions.assertNotValid(film);
    }

    @Test
    @DisplayName("Неуспешная валидация при пустом описании")
    public void shouldNotValidateWhenDescriptionEmpty() {
        film.setDescription(" ");

        ValidationAssertions.assertNotValid(film);
    }

    @Test
    @DisplayName("Неуспешная валидация при описании длиннее 200 символов")
    public void shouldNotValidateWhenDescriptionMoreThan200() {
        film.setDescription(film.getDescription().repeat(15));

        ValidationAssertions.assertNotValid(film);
    }

    @Test
    @DisplayName("Неуспешная валидация при дате выхода равной null")
    public void shouldNotValidateWhenReleaseDateIsNull() {
        film.setReleaseDate(null);

        ValidationAssertions.assertNotValid(film);
    }

    @Test
    @DisplayName("Неуспешная валидация при дате выхода раньше 28 Декабря 1895 года")
    public void shouldNotValidateWhenReleaseDateBeforeThan28DecemberOf1895() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        ValidationAssertions.assertNotValid(film);
    }

    @Test
    @DisplayName("Неуспешная валидация при отрицательной длительности")
    public void shouldNotValidateWhenDurationIsNegative() {
        film.setDuration(-120);

        ValidationAssertions.assertNotValid(film);
    }
}