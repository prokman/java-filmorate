package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.service.FieldChecker;

import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private User userNormal = new User("asdsd@ya.ru","Login", "Name", LocalDate.parse("2024-10-06"));
    private User userEmptyMail = new User("","Login", "Name", LocalDate.parse("2024-10-06"));
    private User userWrangMail = new User("asdasd.ru","Login", "Name", LocalDate.parse("2024-10-06"));
    private User userEmptyLogin = new User("asdsd@ya.ru","", "Name", LocalDate.parse("2024-10-06"));

    @Test
    void addUserNormal() {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()),
                new FieldChecker());
        userController.addUser(userNormal);
        assertTrue(userController.getAllUsers().contains(userNormal));
    }

    @Test
    void addUserEmptyMail() {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()),
                new FieldChecker());
        assertThrows(RuntimeException.class, () -> userController.addUser(userEmptyMail));
    }

    @Test
    void addUserWrangMail() {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()),
                new FieldChecker());
        assertThrows(RuntimeException.class, () -> userController.addUser(userWrangMail));
    }

    @Test
    void addUserEmptyLogin() {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()),
                new FieldChecker());
        assertThrows(RuntimeException.class, () -> userController.addUser(userEmptyLogin));
    }

    @Test
    void addEmpty() {
        UserController userController = new UserController(new UserService(new InMemoryUserStorage()),
                new FieldChecker());
        assertThrows(RuntimeException.class, () -> userController.addUser(null));
    }

}