package ru.yandex.practicum.filmorate.repository;

import ch.qos.logback.classic.Logger;
import lombok.Data;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Service
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private final Map<Integer, Set<Integer>> likes = new HashMap<>();
    private static final Logger log =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(FilmController.class);
    private int filmId = 0;

    @Override
    public Map<Integer, Film> getAllFilms() {
        return films;
    }

    @Override
    public Film getFilmById(Integer id) {
        return films.get(id);
    }

    @Override
    public void addFilm(Film film) {
        films.put(film.getId(), film);
    }

    public void addLike(Integer filmId, Integer userId) {
        likes.computeIfAbsent(filmId, key -> new HashSet<>()).add(userId);
        Film filmForLike = films.get(filmId);
        filmForLike.setRating(likes.get(filmId).size());
        films.put(filmId, filmForLike);
    }

    public void removeLike(Integer filmId, Integer userId) {
        likes.computeIfPresent(filmId, (key, value) -> value).remove(userId);
        Film filmForLike = films.get(filmId);
        filmForLike.setRating(likes.get(filmId).size());
        films.put(filmId, filmForLike);
    }

    public int getNextId() {
        return ++filmId;
    }
}
