package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.sql.*;
import java.util.*;

@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT * FROM genre";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Optional<Genre> get(long id) {
        String sql = "SELECT * FROM genre " +
                "WHERE genre_id = ?";
        List<Genre> genre = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id);
        if (genre.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(genre.get(0));
        }
    }

    Genre makeGenre(final ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getLong("GENRE_ID"))
                .name(rs.getString("NAME"))
                .build();
    }
}
