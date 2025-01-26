package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mapper.FriendsIdRawMapper;
import ru.yandex.practicum.filmorate.repository.mapper.UserRawMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcOperations jdbc;
    private final UserRawMapper userRawMapper;
    private final FriendsIdRawMapper friendsIdRawMapper;

    @Override
    public User addUser(User user) {
        final String INSERT_QUERY
                = "INSERT INTO USERS(NAME, LOGIN, EMAIL, BIRTHDAY) VALUES(?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, user.getName());
            ps.setObject(2, user.getLogin());
            ps.setObject(3, user.getEmail());
            ps.setObject(4, user.getBirthday());
            return ps;
        }, keyHolder);
        Integer id = keyHolder.getKeyAs(Integer.class);
        if (id != null) {
            user.setId(id);
            return user;
        } else {
            throw new RuntimeException("не удалось сохранить данные фильма");
        }
    }

    @Override
    public User update(User user) {
        final String UPDATE_QUERY = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? " +
                " WHERE user_id = ?";
        int rowsUpdated = jdbc.update(UPDATE_QUERY, user.getName(), user.getLogin(),
                user.getEmail(), user.getBirthday(), user.getId());
        if (rowsUpdated == 0) {
            throw new RuntimeException("не удалось обновить данные");
        } else {
            return user;
        }
    }

    @Override
    public List<User> getAllUsers() {
        final String Find_ALL_QUERY = "SELECT * FROM users";
        return jdbc.query(Find_ALL_QUERY, userRawMapper);
    }

    @Override
    public Optional<User> findById(Integer userId) {
        final String Find_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
        try {
            User result = jdbc.queryForObject(Find_BY_ID_QUERY, userRawMapper, userId);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        String query = "INSERT INTO friends(user_id, friend_id) VALUES(?, ?)";
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setObject(1, userId);
            ps.setObject(2, friendId);
            return ps;
        });
    }

    public List<User> getListOfFriendsById(Integer userId) {
        String query = "SELECT friend_id FROM friends WHERE user_id = ?";
        List<User> listOfFriends = jdbc.query(query, friendsIdRawMapper, userId)
                .stream()
                .map(integer -> {
                    return findById(integer).get();
                })
                .collect(Collectors.toList());
        return listOfFriends;
    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        String query = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        int rowsUpdated = jdbc.update(query, userId, friendId);
        if (rowsUpdated <= 0) {
            throw new RuntimeException("не удалось удалить данные");
        }
    }
}
