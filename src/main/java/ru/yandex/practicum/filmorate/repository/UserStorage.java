package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {
    void addUser(User user);

    Map<Integer, User> getAllUsers();

    User getUserById(Integer id);



}
