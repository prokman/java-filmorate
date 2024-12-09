package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
}
