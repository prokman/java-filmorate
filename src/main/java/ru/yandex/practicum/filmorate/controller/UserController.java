package ru.yandex.practicum.filmorate.controller;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FieldChecker;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.HashSet;


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
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user) {
        fieldChecker.checkUserField(user);
        User addedUser = userService.addUser(user);
        log.info("пользователь добавлен в таблицу");
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
        fieldChecker.checkUserField(newUser);
        User updatedUser = userService.updateUser(newUser);
        log.info("существующий пользователь обновлен обновлен в таблице");
        return updatedUser;
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        userService.addFriend(userId, friendId);
        log.info("Пользователь " + userId + " добавил друга " + friendId);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        Collection<User> users = userService.getAllUsers().values();
        log.info("список всех фильмов получен из таблицы");
        return users;
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> getUsersFriends(@PathVariable Integer userId) {
        Collection<User> friends = userService.getUsersFriends(userId).orElse(new HashSet<>());
        log.info("список всех друзей пользователя " + userId + " получен");
        return friends;
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public Collection<User> getCommonFriends(@PathVariable Integer userId, @PathVariable Integer friendId) {
        Collection<User> commonFriends = userService.getCommonFriends(userId, friendId);
        log.info("список общих друзей " + userId + " и " + friendId + " получен");
        return commonFriends;
    }


    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        userService.removeFriend(userId, friendId);
        log.info("Пользователь " + userId + " удалил друга " + friendId);
    }

}
