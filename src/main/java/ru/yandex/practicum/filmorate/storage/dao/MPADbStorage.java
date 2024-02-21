package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.sql.*;
import java.util.*;

@Component
public class MPADbStorage implements MPAStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPA> getAll() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMPA(rs));
    }

    @Override
    public Optional<MPA> get(long id) {
        String sql = "SELECT * FROM mpa " +
                "WHERE mpa_id = ?";
        List<MPA> mpa = jdbcTemplate.query(sql, (rs, rowNum) -> makeMPA(rs), id);
        if (mpa.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(mpa.get(0));
        }
    }

    MPA makeMPA(final ResultSet rs) throws SQLException {
        return MPA.builder()
                .id(rs.getLong("MPA_ID"))
                .name(rs.getString("CODE"))
                .build();
    }
}
