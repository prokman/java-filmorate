package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Data
public class FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
}
