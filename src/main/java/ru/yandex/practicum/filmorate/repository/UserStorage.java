package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface UserStorage {
    User addUser(User user);

    User update (User user);

    List<User> getAllUsers();

    Optional<User> findById(Integer user_id);

    void addFriend(Integer userId, Integer friendId);

    List<User> getListOfFriendsById(Integer userId);

    void removeFriend(Integer userId, Integer friendId);
}
