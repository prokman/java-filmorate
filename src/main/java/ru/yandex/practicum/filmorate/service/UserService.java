package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.InMemoryUserStorage;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final InMemoryUserStorage userStorage;

    public UserService() {
        this.userStorage = new InMemoryUserStorage();
    }

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(userStorage.getNextId());
        userStorage.addUser(user);
        return user;
    }

    public User updateUser(User newUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }

        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id пользователя должен быть указан");
        }
        User existingUser = userStorage.getUserById(newUser.getId());
        if (existingUser != null) {
            existingUser.setEmail(newUser.getEmail());
            existingUser.setLogin(newUser.getLogin());
            existingUser.setName(newUser.getName());
            existingUser.setBirthday(newUser.getBirthday());
            userStorage.addUser(existingUser);
            return existingUser;
        }
        throw new NotFoundException("пользователь с id = " + newUser.getId() + " не найден");
    }

    public void addFriend(Integer userId, Integer friendId) {
        if (userId == null) {
            throw new ConditionsNotMetException("Id пользователя должен быть указан");
        }
        if (friendId == null) {
            throw new ConditionsNotMetException("Id друга должен быть указан");
        }
        User existingUser = userStorage.getUserById(userId);
        if (existingUser != null && userStorage.getUserById(friendId) != null) {
            userStorage.addFriend(userId, userStorage.getUserById(friendId));
            userStorage.addFriend(friendId, userStorage.getUserById(userId));
        } else {
            throw new NotFoundException("пользователь с id = " + userId + " не найден");
        }
    }

    public Optional<Collection<User>> getUsersFriends(Integer userId) {
        if (userId == null) {
            throw new ConditionsNotMetException("Id пользователя должен быть указан");
        }
        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("пользователь с id = " + userId + " не найден");
        }
        //Optional<Collection<User>> friends = userStorage.getFriendsById(userId);
        return userStorage.getFriendsById(userId);
    }

    public Map<Integer, User> getAllUsers() {
        return userStorage.getAllUsers();
    }


}
