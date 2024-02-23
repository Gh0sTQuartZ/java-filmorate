package ru.yandex.practicum.filmorate.storage.dao;

import lombok.*;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.sql.*;
import java.util.*;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT * FROM genre";
        return jdbcOperations.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Optional<Genre> get(long id) {
        String sql = "SELECT * FROM genre " +
                "WHERE genre_id = :id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("id", id);

        List<Genre> genre = jdbcOperations.query(sql, parameterSource, (rs, rowNum) -> makeGenre(rs));
        return genre.stream().findFirst();
    }

    Genre makeGenre(final ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getLong("GENRE_ID"))
                .name(rs.getString("NAME"))
                .build();
    }
}
