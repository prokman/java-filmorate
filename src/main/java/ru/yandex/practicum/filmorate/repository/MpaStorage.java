package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Optional;

public interface MpaStorage {
    Optional<Mpa> getMpaById(Integer Mpa_id);
}
