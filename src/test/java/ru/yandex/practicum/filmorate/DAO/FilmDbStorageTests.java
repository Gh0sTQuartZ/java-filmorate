package ru.yandex.practicum.filmorate.DAO;

import lombok.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.jdbc.*;
import org.springframework.jdbc.core.*;
import org.springframework.test.annotation.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.dao.*;

import java.sql.*;
import java.time.*;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmDbStorageTests {
    private final JdbcTemplate jdbcTemplate;
    private FilmDbStorage storage;
    private UserDbStorage userStorage;
    private Film film1;
    private Film film2;
    private final User user = new User(1L, "test@email.ru", "first", "for film likes",
            LocalDate.of(2001, 01, 01));

    @BeforeEach
    public void beforeEach() {
        storage = new FilmDbStorage(jdbcTemplate);
        userStorage = new UserDbStorage(jdbcTemplate);

        film1 = Film.builder()
                .id(1L)
                .name("first")
                .description("test")
                .releaseDate(LocalDate.of(2001, 01, 01))
                .duration(120)
                .genres(new LinkedHashSet<>())
                .mpa(new MPA(1L, "G"))
                .build();

        LinkedHashSet<Genre> set = new LinkedHashSet<>();
        set.add(new Genre(1L, "Комедия"));
        set.add(new Genre(2L, "Драма"));
        film2 = Film.builder()
                .id(2L)
                .name("second")
                .description("test")
                .releaseDate(LocalDate.of(2002, 02, 02))
                .duration(120)
                .genres(set)
                .mpa(new MPA(1L, "G"))
                .build();
    }

    @Test
    @DisplayName("Получение списка всех фильмов при наличии сохранённых фильмов")
    public void shouldReturnListOfAllFilmsWhenFilmsAdded() throws SQLException {
        storage.create(film1);
        storage.create(film2);

        List<Film> all = storage.getAll();

        assertEquals(2, all.size());
        assertThat(all.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film1);
        assertThat(all.get(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film2);
    }

    @Test
    @DisplayName("Получение списка всех фильмов при отсутствии сохранённых фильмов")
    public void shouldReturnEmptyListOfAllFilmsWhenFilmsNotAdded() throws SQLException {
        List<Film> all = storage.getAll();

        assertTrue(all.isEmpty());
    }

    @Test
    @DisplayName("Получение фильма при наличии этого фильма")
    public void shouldReturnFilmWhenFilmAdded() throws SQLException {
        storage.create(film1);

        Optional<Film> optional = storage.get(film1.getId());

        assertTrue(optional.isPresent());
        assertThat(optional.get())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film1);
    }

    @Test
    @DisplayName("Получение фильма при отсутствии этого фильма")
    public void shouldReturnEmptyOptionalWhenFilmNotAdded() throws SQLException {
        Optional<Film> optional = storage.get(film1.getId());

        assertTrue(optional.isEmpty());
    }

    @Test
    @DisplayName("Создание пользователя")
    public void shouldReturnFilmWhenFilmCreated() {
        Film createdFilm = storage.create(film1);

        assertThat(createdFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film1);
    }

    @Test
    @DisplayName("Обновление фильма")
    public void shouldReturnUpdatedFilmWhenFilmUpdated() {
        LinkedHashSet<Genre> set = new LinkedHashSet<>();
        set.add(new Genre(1L, "Комедия"));
        set.add(new Genre(2L, "Драма"));
        Film update = Film.builder()
                .id(1L)
                .name("update")
                .description("update")
                .releaseDate(LocalDate.of(2007, 07, 07))
                .duration(120)
                .genres(set)
                .mpa(new MPA(2L, "PG"))
                .build();
        storage.create(film1);

        Film updated = storage.update(update);

        assertThat(updated)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(update);
    }

    @Test
    @DisplayName("Получение списка популярных фильмов при наличии лайков")
    public void shouldReturnPopularFilmsListWhenLikesAdded() throws SQLException {
        userStorage.create(user);
        storage.create(film1);
        storage.create(film2);
        storage.addLike(film2.getId(), user.getId());

        List<Film> popularFilms = storage.getPopularFilms(10);

        assertEquals(2, popularFilms.size());
        assertThat(popularFilms.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film2);
        assertThat(popularFilms.get(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film1);
    }

    @Test
    @DisplayName("Получение списка популярных фильмов при отсутствии лайков")
    public void shouldReturnPopularFilmsListWhenLikesNotAdded() throws SQLException {
        storage.create(film1);
        storage.create(film2);

        List<Film> popularFilms = storage.getPopularFilms(10);

        assertEquals(2, popularFilms.size());
        assertThat(popularFilms.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film1);
        assertThat(popularFilms.get(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film2);
    }

    @Test
    @DisplayName("Получение списка популярных фильмов при отсутствии сохранённых фильмов")
    private void shouldReturnEmptyPopularFilmsListWhenFilmsNotAdded() throws SQLException {
        List<Film> popularFilms = storage.getPopularFilms(10);

        assertTrue(popularFilms.isEmpty());
    }

    @Test
    @DisplayName("Получение списка популярных фильмов после удаления лайка")
    private void shouldReturnResortedPopularFilmsListWhenLikeDeleted() throws SQLException {
        shouldReturnPopularFilmsListWhenLikesAdded();
        storage.deleteLike(film2.getId(), user.getId());

        List<Film> popularFilms = storage.getPopularFilms(1);

        assertEquals(1, popularFilms.size());
        assertThat(popularFilms.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film1);
    }
}