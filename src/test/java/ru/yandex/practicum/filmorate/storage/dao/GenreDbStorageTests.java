package ru.yandex.practicum.filmorate.storage.dao;


import lombok.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.jdbc.*;
import org.springframework.jdbc.core.namedparam.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.dao.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTests {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private GenreDbStorage storage;

    @BeforeEach
    public void beforeEach() {
        storage = new GenreDbStorage(jdbcTemplate);
    }

    @Test
    @DisplayName("Получение списка всех жанров")
    public void shouldReturnListOfGenres() {
        List<Genre> genreList = storage.getAll();

        assertEquals(6, genreList.size());
    }

    @Test
    @DisplayName("Получние жанра id = 1 корретный")
    public void shouldReturnNotEmptyOptionalOfGenre() {
        Optional<Genre> optional = storage.get(1);
        assertTrue(optional.isPresent());
        Genre genre = optional.get();

        assertEquals(1, (long) genre.getId());
        assertEquals("Комедия", genre.getName());
    }

    @Test
    @DisplayName("Получение жанра id = 999 некорректный")
    public void shouldReturnEmptyOptionalOfGenre() {
        Optional<Genre> optional = storage.get(999);

        assertTrue(optional.isEmpty());
    }
}