package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NoContentException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.InMemoryUserStorage;

import java.util.Collection;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return userStorage.getSetOfFriendsById(userId);
    }

    public Map<Integer, User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public Collection<User> getCommonFriends(Integer userId, Integer friendId) {
        if (userId == null) {
            throw new ConditionsNotMetException("Id пользователя должен быть указан");
        }
        if (friendId == null) {
            throw new ConditionsNotMetException("Id друга должен быть указан");
        }

        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("пользователь с ид " + userId + " отсутствует");
        }

        if (userStorage.getUserById(friendId) == null) {
            throw new NotFoundException("пользователь с ид " + friendId + " отсутствует");
        }

        return userStorage.getSetOfFriendsById(userId)
                .orElseThrow(() -> new NoContentException("пользователь с ид "
                        + userId + " не друг " + friendId))
                .stream()
                .filter(user ->
                        userStorage.getSetOfFriendsById(friendId)
                                .orElseThrow(() -> new NoContentException("пользователь с ид "
                                        + user.getId() + " не друг " + friendId)).contains(user))
                .collect(Collectors.toList());
    }

    public void removeFriend(Integer userId, Integer friendId) {
        if (userId == null) {
            throw new ConditionsNotMetException("Id пользователя должен быть указан");
        }
        if (friendId == null) {
            throw new ConditionsNotMetException("Id друга должен быть указан");
        }

        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("пользователь с ид " + userId + " отсутствует");
        }

        if (userStorage.getUserById(friendId) == null) {
            throw new NotFoundException("пользователь с ид " + friendId + " отсутствует");
        }

        if (!userStorage.getSetOfFriendsById(userId)
                .orElseThrow(() -> new NoContentException("пользователь с ид "
                        + userId + " не друг " + friendId))
                .contains(userStorage.getUserById(friendId))) {
            throw new NoContentException("пользователь с ид (юзер)" + userId + " не друг " + friendId);
        }

        if (!userStorage.getSetOfFriendsById(friendId)
                .orElseThrow(() -> new NoContentException("пользователь с ид "
                        + friendId + " не друг " + userId))
                .contains(userStorage.getUserById(userId))) {
            throw new NotFoundException("пользователь с ид (френд)" + friendId + " не друг " + userId);
        }
        userStorage.removeFriend(userId, userStorage.getUserById(friendId));
        userStorage.removeFriend(friendId, userStorage.getUserById(userId));
    }
}



