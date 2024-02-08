package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.model.*;

import java.time.*;

public class UserValidationTests {
    private User user;

    @BeforeEach
    public void beforeEach() {
        user = new User(1L, "test@mail.ru", "testLogin", "testName",
                LocalDate.of(2001, 1, 1));
    }

    @Test
    @DisplayName("Успешная валидация при валидных данных")
    public void shouldValidateWhenDataIsValid() {
        ValidationAssertions.assertValid(user);
    }

    @Test
    @DisplayName("Неуспешная валидация при почте равной null")
    public void shouldNotValidateWhenEmailIsNull() {
        user.setEmail(null);

        ValidationAssertions.assertNotValid(user);
    }

    @Test
    @DisplayName("Неуспешная валидация при неверном формате почты")
    public void shouldNotValidateWhenEmailHasIncorrectFormat() {
        user.setEmail("mail.ru");

        ValidationAssertions.assertNotValid(user);
    }


    @Test
    @DisplayName("Неуспешная валидация при логине равному null")
    public void shouldNotValidateWhenLoginIsNull() {
        user.setLogin(null);

        ValidationAssertions.assertNotValid(user);
    }

    @Test
    @DisplayName("Неуспешная валидация при логине содержащем пробел")
    public void shouldNotValidateWhenLoginHasWhitespace() {
        user.setLogin("test login");

        ValidationAssertions.assertNotValid(user);
    }

    @Test
    @DisplayName("Неуспешная валидация при дате рождения равной null")
    public void shouldNotValidateWhenBirthdayIsNull() {
        user.setBirthday(null);

        ValidationAssertions.assertNotValid(user);
    }

    @Test
    @DisplayName("Неуспешная валидация при дате рождения в будущем")
    public void shouldNotValidateWhenBirthdayInFuture() {
        user.setBirthday(LocalDate.now().plusDays(1));

        ValidationAssertions.assertNotValid(user);
    }
}