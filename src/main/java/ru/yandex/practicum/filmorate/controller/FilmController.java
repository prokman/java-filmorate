package ru.yandex.practicum.filmorate.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FieldChecker;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage = new FilmStorage();
    private final FieldChecker fieldChecker = new FieldChecker();
    private static final Logger log =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(FilmController.class);
    private int filmId = 0;

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        if (film == null) {
            log.info("передан пустой запрос");
            throw new NotFoundException("передан пустой запрос");
        }
        log.setLevel(Level.INFO);
        fieldChecker.checkFilmField(film);
        film.setId(getNextId());
        log.info("фильму присвоен id - {}", film.getId());
        filmStorage.getFilms().put(film.getId(), film);
        log.info("фильм записан в таблицу");
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film newfilm) {
        if (newfilm == null) {
            log.info("передан пустой запрос");
            throw new NotFoundException("передан пустой запрос");
        }
        fieldChecker.checkFilmField(newfilm);
        Film existingFilm = filmStorage.getFilms().get(newfilm.getId());
        if (existingFilm != null) {
            existingFilm.setName(newfilm.getName());
            existingFilm.setDescription(newfilm.getDescription());
            existingFilm.setDuration(newfilm.getDuration());
            existingFilm.setReleaseDate(newfilm.getReleaseDate());
            filmStorage.getFilms().put(existingFilm.getId(), existingFilm);
            log.info("данные фильма обновлены и помещены в таблицу");
            return existingFilm;
        }
        log.info("фильм с id = {} не найден", newfilm.getId());
        throw new NotFoundException("фильм с id = " + newfilm.getId() + " не найден");
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmStorage.getFilms().values();
    }

    private int getNextId() {
        return ++filmId;
    }
}
