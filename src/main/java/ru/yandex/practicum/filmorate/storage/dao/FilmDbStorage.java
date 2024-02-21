package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.rowset.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.sql.*;
import java.util.*;
import java.util.stream.*;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAll() throws SQLException {
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
                "LEFT OUTER JOIN film_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT OUTER JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "LEFT OUTER JOIN mpa AS m ON f.mpa_id = m.mpa_id";
        return makeFilmsList(jdbcTemplate.queryForRowSet(sql));
    }

    @Override
    public Optional<Film> get(long id) throws SQLException {
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
                "LEFT OUTER JOIN film_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT OUTER JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "LEFT OUTER JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "WHERE f.film_id = ?";
        return makeFilm(jdbcTemplate.queryForRowSet(sql, id));
    }

    @Override
    public Film create(Film film) {
        String sqlFilm = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlFilm,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());

        String sqlGenres = "INSERT INTO film_genres " +
                "VALUES (?, ?)";
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlGenres,
                        film.getId(),
                        genre.getId());
            }
            film.setGenres(film.getGenres().stream()
                    .sorted(Comparator.comparingLong(Genre::getId))
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlFilm = "UPDATE FILMS " +
                "SET name = ?, " +
                "description = ?, " +
                "release_date = ?, " +
                "duration = ?, " +
                "mpa_id = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlFilm,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        String sqlGenresDelete = "DELETE FROM film_genres " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlGenresDelete, film.getId());

        String sqlGenresInsert = "INSERT INTO film_genres " +
                "VALUES (?, ?)";
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlGenresInsert,
                        film.getId(),
                        genre.getId());
            }
            film.setGenres(film.getGenres().stream()
                    .sorted(Comparator.comparingLong(Genre::getId))
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
        }

        return film;
    }

    @Override
    public void addLike(long id, long userId) {
        String sql = "INSERT INTO film_likes " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql,
                id,
                userId);
    }

    @Override
    public void deleteLike(long id, long userId) {
        String sql = "DELETE FROM film_likes " +
                "WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql,
                id,
                userId);
    }

    @Override
    public List<Film> getPopularFilms(long size) throws SQLException {
        // подзапрос ограничивает количество выгружаемых фильмов, чтобы не выгружать абсолютно все фильмы
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
                "LEFT OUTER JOIN film_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT OUTER JOIN genre AS g ON fg.genre_id = g.genre_id " +
                "LEFT OUTER JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "WHERE f.film_id IN (SELECT f2.film_id " +
                "FROM films AS f2 " +
                "LEFT OUTER JOIN film_likes AS fl ON f2.film_id = fl.film_id " +
                "GROUP BY f2.film_id " +
                "ORDER BY COUNT(fl.user_id) DESC " +
                "LIMIT ?)";
        List<Film> films = makeFilmsList(jdbcTemplate.queryForRowSet(sqlForFilms, size));

        String sqlForPopularity = "SELECT f.film_id " +
                "FROM films AS f " +
                "LEFT OUTER JOIN film_likes AS fl ON f.film_id = fl.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(fl.user_id) DESC " +
                "LIMIT ?";
        List<Long> filmId = jdbcTemplate.query(sqlForPopularity,
                (rs, rowNum) -> rs.getLong("FILM_ID"), size);

        List<Film> result = new ArrayList<>();

        for (Long id : filmId) {
            for (Film film : films) {
                if (film.getId() == id) {
                    result.add(film);
                }
            }
        }

        return result;
    }

    private Optional<Film> makeFilm(final SqlRowSet rs) throws SQLException {
        Film film;
        if (rs.next()) {
            film = Film.builder()
                    .id(rs.getLong("FILM_ID"))
                    .name(rs.getString(2))
                    .description(rs.getString("DESCRIPTION"))
                    .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                    .duration(rs.getInt("DURATION"))
                    .mpa(new MPA(rs.getLong("MPA_ID"), rs.getString("CODE")))
                    .build();
        } else {
            return Optional.empty();
        }

        Long id = rs.getLong("FILM_ID");
        Set<Genre> genres = new HashSet<>();

        do {
            if (id != rs.getLong("FILM_ID")) {
                break;
            }
            Genre genre = new Genre(rs.getLong("GENRE_ID"), rs.getString(9));
            if (genre.getId() == 0) {
                continue;
            }
            genres.add(genre);
        } while (rs.next());
        if (!rs.isBeforeFirst()) {
            rs.previous();
        }
        film.setGenres(genres.stream()
                .sorted(Comparator.comparingLong(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        return Optional.of(film);
    }

    private List<Film> makeFilmsList(final SqlRowSet rs) throws SQLException {
        List<Film> films = new ArrayList<>();
        do {
            Optional<Film> optional = makeFilm(rs);
            if (optional.isEmpty()) {
                break;
            }
            films.add(optional.get());
        } while (!rs.isAfterLast());
        return films;
    }
}