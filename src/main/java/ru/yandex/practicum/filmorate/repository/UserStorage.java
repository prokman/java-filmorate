package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface UserStorage {
    void addUser(User user);

    Map<Integer, User> getAllUsers();

    User getUserById(Integer id);

    void addFriend(Integer userId, User friend);

    Optional<Collection<User>> getSetOfFriendsById(Integer userId);

    void removeFriend(Integer userId, User friend);

    int getNextId();


}
