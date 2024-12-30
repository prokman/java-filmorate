package ru.yandex.practicum.filmorate.repository;

import ch.qos.logback.classic.Logger;
import lombok.Data;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;

import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Data
@Service
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users;
    private final Map<Integer, Set<User>> friends;
    private static final Logger log =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(FilmController.class);
    private int userId = 0;

    public InMemoryUserStorage() {
        this.users = new HashMap<>();
        this.friends = new HashMap<>();
    }

    @Autowired
    public InMemoryUserStorage(Map<Integer, User> users, Map<Integer, Set<User>> friends, Set<User> setOfFriends) {
        this.users = users;
        this.friends = friends;
    }

    @Override
    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Map<Integer, User> getAllUsers() {
        return users;
    }

    @Override
    public User getUserById(Integer id) {
        return users.get(id);
    }

    public void addFriend(Integer userId, User friend) {
        friends.computeIfAbsent(userId, key -> new HashSet<>()).add(friend);
    }

    public Optional<Collection<User>> getSetOfFriendsById(Integer userId) {
        return Optional.ofNullable(friends.get(userId));
    }

    public void removeFriend(Integer userId, User friend) {
        friends.computeIfPresent(userId, (key, value) -> value).remove(friend);
    }

    public int getNextId() {
        return ++userId;
    }
}
