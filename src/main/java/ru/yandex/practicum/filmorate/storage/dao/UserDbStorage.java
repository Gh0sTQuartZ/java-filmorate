package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.sql.*;
import java.util.*;

@Component
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public Optional<User> get(long id) {
        String sql = "SELECT * FROM users " +
                "WHERE user_id = ?";
        List<User> user = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);
        if (user.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(user.get(0));
        }
    }

    @Override
    public User create(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users " +
                "SET email = ?, " +
                "login = ?, " +
                "name = ?, " +
                "birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
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
                "LEFT OUTER JOIN users AS u ON uf.friend_id = u.user_id " +
                "WHERE uf.user_id = ? AND uf.confirmed = TRUE";
        return jdbcTemplate.query(sql, (rs, rowSet) -> makeUser(rs), id);
    }

    @Override
    public void addFriend(long id, long friendId) {
        String sql = "INSERT INTO user_friends " +
                "VALUES (?, ?, TRUE), " +
                "(?, ?, FALSE)";
        jdbcTemplate.update(sql,
                id,
                friendId,
                friendId,
                id);
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        String sql = "DELETE FROM user_friends " +
                "WHERE user_id = ? AND friend_id = ? " +
                "OR user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql,
                id,
                friendId,
                friendId,
                id);
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherUserId) {
        String sql = "select u.user_id, " +
                "u.email, " +
                "u.login, " +
                "u.name, " +
                "u.birthday " +
                "FROM user_friends AS uf " +
                "LEFT OUTER JOIN users AS u ON uf.friend_id = u.user_id " +
                "WHERE uf.user_id = ? " +
                "AND uf.confirmed = TRUE " +
                "AND u.user_id IN (" +
                "SELECT uf2.friend_id " +
                "FROM user_friends AS uf2 " +
                "WHERE uf2.user_id = ? " +
                "AND uf2.confirmed =TRUE)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), userId, otherUserId);
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