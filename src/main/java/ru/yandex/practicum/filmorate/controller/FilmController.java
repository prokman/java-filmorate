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

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        filmService.addLike(filmId, userId);
        log.info("фильму " + filmId + " поставлен лайк пользователем " + userId);
    }

    @GetMapping
    public Collection<Film> getFilms() {
        Collection<Film> films = filmService.getAllFilms().values();
        log.info("список всех фильмов получен из таблицы");
        return films;
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(
            @RequestParam(defaultValue = "10") Integer count
    ) {
        if (count <= 0) {
            throw new ValidationException("Параметр count-" + count + " не должен быть меньше либо равен 0");
        }
        Collection<Film> films = filmService.getPopularFilms(count);
        log.info("список всех фильмов получен из таблицы");
        return films;
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        filmService.removeLike(filmId, userId);
        log.info("у фильма " + filmId + " удален лайк пользователем " + userId);
    }
}