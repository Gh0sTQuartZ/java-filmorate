package ru.yandex.practicum.filmorate.storage.dao;

import lombok.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.jdbc.*;
import org.springframework.jdbc.core.namedparam.*;
import ru.yandex.practicum.filmorate.model.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MPADbStorageTests {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private MPADbStorage storage;

    @BeforeEach
    public void beforeEach() {
        storage = new MPADbStorage(jdbcTemplate);
    }

    @Test
    @DisplayName("Получение списка всех рейтингов")
    public void shouldReturnListOfMPA() {
        List<MPA> mpaList = storage.getAll();

        assertEquals(5, mpaList.size());
    }

    @Test
    @DisplayName("Получение рейтинга id = 1 корректный")
    public void shouldReturnNotEmptyOptionalOfMPA() {
        Optional<MPA> optional = storage.get(1);
        assertTrue(optional.isPresent());
        MPA mpa = optional.get();

        assertEquals(1, (long) mpa.getId());
        assertEquals("G", mpa.getName());
    }

    @Test
    @DisplayName("Получение ретинга id = 999 некорректный")
    public void shouldReturnEmptyOptionalOfMPA() {
        Optional<MPA> optional = storage.get(999);

        assertTrue(optional.isEmpty());
    }
}