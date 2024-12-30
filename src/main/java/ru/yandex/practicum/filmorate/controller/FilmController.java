package ru.yandex.practicum.filmorate.controller;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FieldChecker;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FieldChecker fieldChecker;
    private final FilmService filmService;
    private static final Logger log =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(FilmController.class);

    @Autowired
    public FilmController(FieldChecker fieldChecker, FilmService filmService) {
        this.fieldChecker = fieldChecker;
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        fieldChecker.checkFilmField(film);
        Film addedFilm = filmService.addFilm(film);
        log.info("новый фильм записан в таблицу");
        return addedFilm;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film newfilm) {
        fieldChecker.checkFilmField(newfilm);
        Film updatedFilm = filmService.updateFilm(newfilm);
        log.info("существующий фильм обновлен в таблице");
        return updatedFilm;
    }

    @GetMapping
    public Collection<Film> getPopularFilms(
            @RequestParam(required = false) Integer count
    ) {
        if (count == null) {
            if (count <= 0) {
                throw new ValidationException("Параметр count-" + count + " не должен быть меньше либо равен 0");
            }
            Collection<Film> films = filmService.getPopularFilms(count);
            log.info("список всех фильмов получен из таблицы");
            return films;
        }
        Collection<Film> films = filmService.getAllFilms().values();
        log.info("список всех фильмов получен из таблицы");
        return films;
    }
}