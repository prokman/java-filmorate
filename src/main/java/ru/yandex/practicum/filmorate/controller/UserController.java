package ru.yandex.practicum.filmorate.controller;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FieldChecker;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final FieldChecker fieldChecker;
    private static final Logger log =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, FieldChecker fieldChecker) {
        this.userService = userService;
        this.fieldChecker = fieldChecker;
    }

    @PostMapping
    public UserDto addUser(@RequestBody User user) {
        fieldChecker.checkUserField(user);
        UserDto addedUser = userService.addUser(user);
        log.info("пользователь добавлен в таблицу");
        return addedUser;
    }

    @PutMapping
    public UserDto updateUser(@RequestBody User newUser) {
        fieldChecker.checkUserField(newUser);
        UserDto updatedUser = userService.updateUser(newUser);
        log.info("существующий пользователь обновлен обновлен в таблице");
        return updatedUser;
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        userService.addFriend(userId, friendId);
        log.info("Пользователь " + userId + " добавил друга " + friendId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        log.info("список всех пользователей получен из БД");
        return users;
    }

    @GetMapping("/{userId}/friends")
    public List<User> getListOfFriendsById(@PathVariable Integer userId) {
        List<User> friends = userService.getListOfFriendsById(userId);
        log.info("список всех друзей пользователя " + userId + " получен");
        return friends;
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public List<User> getCommonFriends(@PathVariable Integer userId, @PathVariable Integer friendId) {
        List<User> commonFriends = userService.getCommonFriends(userId, friendId);
        log.info("список общих друзей " + userId + " и " + friendId + " получен");
        return commonFriends;
    }


    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        userService.removeFriend(userId, friendId);
        log.info("Пользователь " + userId + " удалил друга " + friendId);
    }

}
