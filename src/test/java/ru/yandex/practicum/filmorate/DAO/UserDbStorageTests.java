package ru.yandex.practicum.filmorate.DAO;


import lombok.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.jdbc.*;
import org.springframework.jdbc.core.*;
import org.springframework.test.annotation.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.dao.*;

import java.time.*;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserDbStorageTests {
    private final JdbcTemplate jdbcTemplate;
    private UserDbStorage storage;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void beforeEach() {
        storage = new UserDbStorage(jdbcTemplate);
        user1 = new User(1L, "email", "first", "first",
                LocalDate.of(2001, 01, 01));
        user2 = new User(2L, "email", "second", "second",
                LocalDate.of(2002, 02, 02));
        user3 = new User(3L, "email", "third", "third",
                LocalDate.of(2003, 03, 03));
    }

    @Test
    @DisplayName("Получение списка всех пользователей при наличии сохранёных пользователей")
    public void shouldReturnListOfAllUsersWhenUsersAdded() {
        storage.create(user1);
        storage.create(user2);
        storage.create(user3);

        List<User> all = storage.getAll();

        System.out.println(all);

        assertEquals(3, all.size());
        assertEquals(all.get(0), user1);
        assertEquals(all.get(1), user2);
        assertEquals(all.get(2), user3);
    }

    @Test
    @DisplayName("Получение списка всех пользователей при отсутствии сохранённых пользователей")
    public void shouldReturnEmptyListOfAllUsersWhenNotAdded() {
        List<User> all = storage.getAll();

        assertTrue(all.isEmpty());
    }

    @Test
    @DisplayName("Получение пользователя при наличии этого пользователя")
    public void shouldReturnUserWhenUserAdded() {
        storage.create(user1);

        Optional<User> user = storage.get(user1.getId());

        assertTrue(user.isPresent());
        assertThat(user.get())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user1);
    }

    @Test
    @DisplayName("Получение пользователя при отсутствии этого пользователя")
    public void shouldReturnEmptyOptionalWhenUserNotAdded() {
        Optional<User> optionalUser = storage.get(user1.getId());

        assertTrue(optionalUser.isEmpty());
    }

    @Test
    @DisplayName("Создание пользователя")
    public void shouldReturnUserWhenUserCreated() {
        User createdUser = storage.create(user1);

        assertThat(createdUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user1);
    }

    @Test
    @DisplayName("Обновление пользователя")
    public void shouldReturnUpdatedUserWhenUserUpdated() {
        storage.create(user1);
        User update = new User(1L, "updated", "updated", "updated",
                LocalDate.of(2010, 10, 10));

        User updatedUser = storage.update(update);

        assertThat(updatedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(update);
    }

    @Test
    @DisplayName("Получение списка друзей пользователя при наличии друзей")
    public void shouldReturnFriendListWhenFriendsAdded() {
        storage.create(user1);
        storage.create(user2);
        storage.create(user3);
        storage.addFriend(user1.getId(), user2.getId());
        storage.addFriend(user1.getId(), user3.getId());

        List<User> friends = storage.getFriends(user1.getId());

        assertEquals(2, friends.size());
        assertEquals(user2, friends.get(0));
        assertEquals(user3, friends.get(1));
    }

    @Test
    @DisplayName("Получение списка друзей пользователя при отсутствии друзей")
    public void shouldReturnEmptyFriendsListWhenFriendsNotAdded() {
        storage.create(user1);

        List<User> friends = storage.getFriends(user1.getId());

        assertTrue(friends.isEmpty());
    }

    @Test
    @DisplayName("Получение списка друзей после удаления друга")
    public void shouldReturnSizedFriendsListAfterDeletingFriend() {
        shouldReturnFriendListWhenFriendsAdded();
        storage.deleteFriend(user1.getId(), user3.getId());

        List<User> friends = storage.getFriends(user1.getId());

        assertEquals(1, friends.size());
        assertThat(friends.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user2);
    }

    @Test
    @DisplayName("Получение списка общих друзей двух пользователей при наличии общих друзей")
    public void shouldReturnCommonFriendsListWhenCommonFriendsAdded() {
        storage.create(user1);
        storage.create(user2);
        storage.create(user3);
        storage.addFriend(user1.getId(), user2.getId());
        storage.addFriend(user3.getId(), user2.getId());

        List<User> commonFriends = storage.getCommonFriends(user1.getId(), user3.getId());

        assertEquals(1, commonFriends.size());
        assertThat(commonFriends.get(0))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user2);
    }

    @Test
    @DisplayName("Получение списка общих друзей двух пользователей при отсутствии общих друзей")
    public void shouldReturnEmptyCommonFriendsListWhenCommonFriendsNotAdded() {
        shouldReturnCommonFriendsListWhenCommonFriendsAdded();

        List<User> commonFriends = storage.getCommonFriends(user1.getId(), user2.getId());

        assertTrue(commonFriends.isEmpty());
    }
}