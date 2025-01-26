package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserDbStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserDbStorage userDbStorage;

    @Autowired
    public UserService(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    public UserDto addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return UserMapper.mapUserToDto(userDbStorage.addUser(user));
    }

    public UserDto updateUser(User newUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id пользователя должен быть указан");
        }
        if (userDbStorage.findById(newUser.getId()).isPresent()) {

            userDbStorage.update(newUser);
            return UserMapper.mapUserToDto(userDbStorage.update(newUser));
        } else {
            throw new NotFoundException("пользователь с id = " + newUser.getId() + " не найден");
        }
    }

    public void addFriend(Integer userId, Integer friendId) {
        if (userId == null) {
            throw new ConditionsNotMetException("Id пользователя должен быть указан");
        }
        if (friendId == null) {
            throw new ConditionsNotMetException("Id друга должен быть указан");
        }

        if (userDbStorage.findById(userId).isPresent() && userDbStorage.findById(friendId).isPresent()) {
            userDbStorage.addFriend(userId, friendId);
        } else {
            throw new NotFoundException("пользователи с id = " + userId + "или друг с " + friendId + " не найдены");
        }
    }

    public List<User> getListOfFriendsById(Integer userId) {
        if (userId == null) {
            throw new ConditionsNotMetException("Id пользователя должен быть указан");
        }
        if (userDbStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("пользователь с id = " + userId + " не найден");
        }
        return userDbStorage.getListOfFriendsById(userId);
    }

    public List<UserDto> getAllUsers() {
        return userDbStorage.getAllUsers()
                .stream()
                .map(UserMapper::mapUserToDto)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer userId, Integer friendId) {
        if (userId == null) {
            throw new ConditionsNotMetException("Id пользователя должен быть указан");
        }
        if (friendId == null) {
            throw new ConditionsNotMetException("Id друга должен быть указан");
        }
        if (userDbStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("пользователь с ид " + userId + " отсутствует");
        }
        if (userDbStorage.findById(friendId).isEmpty()) {
            throw new NotFoundException("пользователь с ид " + friendId + " отсутствует");
        }

        return userDbStorage.getListOfFriendsById(userId)
                .stream()
                .filter(user -> userDbStorage
                        .getListOfFriendsById(friendId)
                        .contains(userDbStorage.findById(user.getId()).get()))
                .collect(Collectors.toList());
    }

    public void removeFriend(Integer userId, Integer friendId) {
        if (userId == null) {
            throw new ConditionsNotMetException("Id пользователя должен быть указан");
        }
        if (friendId == null) {
            throw new ConditionsNotMetException("Id друга должен быть указан");
        }
        if (userDbStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("пользователь с ид " + userId + " отсутствует");
        }
        if (userDbStorage.findById(friendId).isEmpty()) {
            throw new NotFoundException("пользователь с ид " + friendId + " отсутствует");
        }
        if (!userDbStorage.getListOfFriendsById(userId).isEmpty()) {
            userDbStorage.removeFriend(userId, friendId);
        }
    }
}



