package ru.yandex.practicum.filmorate.controller;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FieldChecker;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

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

    @GetMapping
    public Collection<User> getAllUsers() {
        Collection<User> users = userService.getAllUsers().values();
        log.info("список всех фильмов получен из таблицы");
        return users;
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        userService.addFriend(userId, friendId);
        log.info("Пользователь " + userId + " добавил друга " + friendId);
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> getUsersFriends(@PathVariable Integer userId) {
        Collection<User> friends = userService.getUsersFriends(userId).orElse(new HashSet<>());
        log.info("список всех друзей пользователя " + userId + " получен");
        return friends;
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend (@PathVariable Integer userId, @PathVariable Integer friendId) {

    }

}
