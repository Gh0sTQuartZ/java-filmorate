package ru.yandex.practicum.filmorate.storage.dao;

import lombok.*;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.support.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.sql.*;
import java.util.*;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users";
        return jdbcOperations.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public Optional<User> get(long id) {
        String sql = "SELECT * FROM users " +
                "WHERE user_id = :id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("id", id);

        List<User> user = jdbcOperations.query(sql, parameterSource, (rs, rowNum) -> makeUser(rs));
        if (user.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(user.get(0));
        }
    }

    @Override
    public User create(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (:email, :login, :name, :birthday)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("email", user.getEmail());
        parameterSource.addValue("login", user.getLogin());
        parameterSource.addValue("name", user.getName());
        parameterSource.addValue("birthday", user.getBirthday());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcOperations.update(sql, parameterSource, keyHolder);
        user.setId(keyHolder.getKey().longValue());

        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users " +
                "SET email = :email, " +
                "login = :login, " +
                "name = :name, " +
                "birthday = :birthday " +
                "WHERE user_id = :id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", user.getId());
        parameterSource.addValue("email", user.getEmail());
        parameterSource.addValue("login", user.getLogin());
        parameterSource.addValue("name", user.getName());
        parameterSource.addValue("birthday", user.getBirthday());

        jdbcOperations.update(sql, parameterSource);
        return user;
    }

    @Override
    public List<User> getFriends(long id) {
        String sql = "SELECT u.user_id, " +
                "u.email, " +
                "u.login, " +
                "u.name, " +
                "u.birthday " +
                "FROM user_friends AS uf " +
                "LEFT JOIN users AS u ON uf.friend_id = u.user_id " +
                "WHERE uf.user_id = :id AND uf.confirmed = TRUE";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("id", id);

        return jdbcOperations.query(sql, parameterSource, (rs, rowSet) -> makeUser(rs));
    }

    @Override
    public void addFriend(long id, long friendId) {
        String sql = "INSERT INTO user_friends " +
                "VALUES (:id, :friend_id, TRUE), " +
                "(:friend_id, :id, FALSE)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        parameterSource.addValue("friend_id", friendId);

        jdbcOperations.update(sql, parameterSource);
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        String sql = "DELETE FROM user_friends " +
                "WHERE user_id = :id AND friend_id = :friend_id " +
                "OR user_id = :friend_id AND friend_id = :id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", id);
        parameterSource.addValue("friend_id", friendId);

        jdbcOperations.update(sql, parameterSource);
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherUserId) {
        String sql = "select u.user_id, " +
                "u.email, " +
                "u.login, " +
                "u.name, " +
                "u.birthday " +
                "FROM user_friends AS uf " +
                "LEFT JOIN users AS u ON uf.friend_id = u.user_id " +
                "WHERE uf.user_id = :user_id " +
                "AND uf.confirmed = TRUE " +
                "AND u.user_id IN (" +
                "SELECT uf2.friend_id " +
                "FROM user_friends AS uf2 " +
                "WHERE uf2.user_id = :other_user_id " +
                "AND uf2.confirmed = TRUE)";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("user_id", userId);
        parameterSource.addValue("other_user_id", otherUserId);

        return jdbcOperations.query(sql, parameterSource, (rs, rowNum) -> makeUser(rs));
    }

    User makeUser(final ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getLong("USER_ID"))
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .name(rs.getString("NAME"))
                .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                .build();
    }
}