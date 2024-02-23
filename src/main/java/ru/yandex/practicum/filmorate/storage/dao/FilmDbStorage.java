package ru.yandex.practicum.filmorate.storage.dao;

import lombok.*;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.support.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.sql.*;
import java.util.*;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<Film> getAll() {
        String sql = "SELECT f.film_id, " +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "m.mpa_id, " +
                "m.code, " +
                "g.genre_id, " +
                "g.name " +
                "FROM films AS f " +
                "LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id";
        return jdbcOperations.query(sql, this::extractFilmsList);
    }

    @Override
    public Optional<Film> get(long id) {
        String sql = "SELECT f.film_id, " +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "m.mpa_id, " +
                "m.code, " +
                "g.genre_id, " +
                "g.name " +
                "FROM films AS f " +
                "LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "WHERE f.film_id = :id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        return jdbcOperations.query(sql, parameterSource, this::extractFilm);
    }

    @Override
    public Film create(Film film) {
        String sqlFilm = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                "VALUES (:name, :description, :releaseDate, :duration, :mpa_id)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("name", film.getName());
        parameterSource.addValue("description", film.getDescription());
        parameterSource.addValue("releaseDate", film.getReleaseDate());
        parameterSource.addValue("duration", film.getDuration());
        parameterSource.addValue("mpa_id", film.getMpa().getId());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcOperations.update(sqlFilm, parameterSource, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        insertGenres(film.getId(), film.getGenres());

        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlFilm = "UPDATE FILMS " +
                "SET name = :name, " +
                "description = :description, " +
                "release_date = :releaseDate, " +
                "duration = :duration, " +
                "mpa_id = :mpa_id " +
                "WHERE film_id = :id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", film.getId());
        parameterSource.addValue("name", film.getName());
        parameterSource.addValue("description", film.getDescription());
        parameterSource.addValue("releaseDate", film.getReleaseDate());
        parameterSource.addValue("duration", film.getDuration());
        parameterSource.addValue("mpa_id", film.getMpa().getId());


        jdbcOperations.update(sqlFilm, parameterSource);

        String sqlGenresDelete = "DELETE FROM film_genres " +
                "WHERE film_id = :id";
        parameterSource = new MapSqlParameterSource("id", film.getId());
        jdbcOperations.update(sqlGenresDelete, parameterSource);

        insertGenres(film.getId(), film.getGenres());

        return film;
    }

    @Override
    public void addLike(long id, long userId) {
        String sql = "MERGE INTO film_likes (film_id, user_id)" +
                "KEY(film_id)" +
                "VALUES (:id, :user_id)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        parameterSource.addValue("user_id", userId);

        jdbcOperations.update(sql, parameterSource);
    }

    @Override
    public void deleteLike(long id, long userId) {
        String sql = "DELETE FROM film_likes " +
                "WHERE film_id = :id AND user_id = :user_id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        parameterSource.addValue("user_id", userId);

        jdbcOperations.update(sql, parameterSource);
    }

    @Override
    public List<Film> getPopular(long size) {
        String sqlForFilms = "SELECT f.film_id, " +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "m.mpa_id, " +
                "m.code, " +
                "g.genre_id, " +
                "g.name " +
                "FROM films AS f " +
                "LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "WHERE f.film_id IN (SELECT f2.film_id " +
                "FROM films AS f2 " +
                "LEFT JOIN film_likes AS fl ON f2.film_id = fl.film_id " +
                "GROUP BY f2.film_id " +
                "ORDER BY COUNT(fl.user_id) DESC " +
                "LIMIT :size)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("size", size);

        return jdbcOperations.query(sqlForFilms, parameterSource, this::extractFilmsList);
    }

    private void insertGenres(final Long id, final LinkedHashSet<Genre> genres) {
        if (genres != null) {
            String sql = "INSERT INTO film_genres " +
                    "VALUES (:id, :genre_id)";
            Map<String, Long>[] batchValues = new HashMap[genres.size()];
            int count = 0;
            for (Genre genre : genres) {
                Map<String, Long> map = new HashMap<>();
                map.put("id", id);
                map.put("genre_id", genre.getId());
                batchValues[count++] = map;
            }
            jdbcOperations.batchUpdate(sql, batchValues);
        }
    }

    private Optional<Film> extractFilm(final ResultSet rs) throws SQLException {
        if (rs.next()) {
            return Optional.of(makeFilm(rs));
        } else {
            return Optional.empty();
        }
    }

    private List<Film> extractFilmsList(final ResultSet rs) throws SQLException {
        List<Film> filmList = new ArrayList<>();
        if (rs.next()) {
            while (!rs.isAfterLast()) {
                filmList.add(makeFilm(rs));
            }
        }
        return filmList;
    }

    private Film makeFilm(final ResultSet rs) throws SQLException {
        Film film;

        film = Film.builder()
                .id(rs.getLong("FILM_ID"))
                .name(rs.getString(2))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getInt("DURATION"))
                .mpa(new MPA(rs.getLong("MPA_ID"), rs.getString("CODE")))
                .build();


        Long id = rs.getLong("FILM_ID");
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();

        do {
            if (id != rs.getLong("FILM_ID")) {
                break;
            }
            Genre genre = new Genre(rs.getLong("GENRE_ID"), rs.getString(9));
            if (genre.getId() != 0) {
                genres.add(genre);
            }
        } while (rs.next());
        film.setGenres(genres);
        return film;
    }
}