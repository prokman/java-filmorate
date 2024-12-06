package ru.yandex.practicum.filmorate.controller;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FieldChecker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private final FieldChecker fieldChecker = new FieldChecker();
    private static final Logger log =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public User addUser(@RequestBody User user) {
        if (user == null) {
            log.info("передан пустой запрос");
            throw new NotFoundException("передан пустой запрос");
        }
        fieldChecker.checkUserField(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя пустое. Полю \"имя пользоватлея\" присвоено значение логина - {}",
                    user.getLogin());
        }
        user.setId(getNextId());
        log.info("пользователю присвоен id - {}", user.getId());
        users.put(user.getId(), user);
        log.info("пользователь добавлен в таблицу");
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
        if (newUser == null) {
            log.info("передан пустой запрос");
            throw new NotFoundException("передан пустой запрос");
        }
        fieldChecker.checkUserField(newUser);
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
            log.info("Имя пользователя пустое. Полю \"имя пользоватлея\" присвоено значение логина - {}",
                    newUser.getLogin());
        }
        User existingUser = users.get(newUser.getId());
        if (existingUser != null) {
            existingUser.setEmail(newUser.getEmail());
            existingUser.setLogin(newUser.getLogin());
            existingUser.setName(newUser.getName());
            existingUser.setBirthday(newUser.getBirthday());
            users.put(existingUser.getId(), existingUser);
            log.info("данные пользователя обновлены и добавлены в таблицу");
            return existingUser;
        }
        log.info("пользователь с id = {} не найден", newUser.getId());
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
