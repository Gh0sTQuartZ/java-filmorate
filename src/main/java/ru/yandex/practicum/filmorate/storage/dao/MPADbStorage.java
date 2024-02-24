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
public class MPADbStorage implements MPAStorage {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<MPA> getAll() {
        String sql = "SELECT * FROM mpa";
        return jdbcOperations.query(sql, (rs, rowNum) -> makeMPA(rs));
    }

    @Override
    public Optional<MPA> get(long id) {
        String sql = "SELECT * FROM mpa " +
                "WHERE mpa_id = :id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        List<MPA> mpa = jdbcOperations.query(sql, parameterSource, (rs, rowNum) -> makeMPA(rs));
        return mpa.stream().findFirst();
    }

    MPA makeMPA(final ResultSet rs) throws SQLException {
        return MPA.builder()
                .id(rs.getLong("MPA_ID"))
                .name(rs.getString("CODE"))
                .build();
    }
}
