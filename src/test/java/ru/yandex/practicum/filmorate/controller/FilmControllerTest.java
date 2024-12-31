package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FieldChecker;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class FilmControllerTest {
    private Film normal = new Film("name1", "desciption1", LocalDate.parse("2024-12-06"), 1);
    private Film emptyName = new Film("", "desciption1", LocalDate.parse("2024-12-06"), 2);
    private Film emptyDesciption = new Film("name1", "", LocalDate.parse("2024-12-06"), 3);
    private Film wrangReleaseDate = new Film("name1", "desciption1", LocalDate.parse("1000-12-06"), 4);
    private Film wrangDuration = new Film("name1", "desciption1", LocalDate.parse("2000-12-06"), -1);

    @Test
    void addFilmNormal() {
        FilmController filmController = new FilmController(new FieldChecker(), new FilmService());
        filmController.addFilm(normal);
        assertTrue(filmController.getFilms().contains(normal));
    }

    @Test
    void addFilmEmptyName() {
        FilmController filmController = new FilmController(new FieldChecker(), new FilmService());
        assertThrows(RuntimeException.class, () -> filmController.addFilm(emptyName));
    }

    @Test
    void addFilmEmptyDesciption() {
        FilmController filmController = new FilmController(new FieldChecker(), new FilmService());
        assertThrows(RuntimeException.class, () -> filmController.addFilm(emptyDesciption));
    }

    @Test
    void addFilmWrangReleaseDate() {
        FilmController filmController = new FilmController(new FieldChecker(), new FilmService());
        assertThrows(RuntimeException.class, () -> filmController.addFilm(wrangReleaseDate));
    }

    @Test
    void addFilmWrangDuration() {
        FilmController filmController = new FilmController(new FieldChecker(), new FilmService());
        assertThrows(RuntimeException.class, () -> filmController.addFilm(wrangDuration));
    }

    @Test
    void addEmpty() {
        FilmController filmController = new FilmController(new FieldChecker(), new FilmService());
        assertThrows(RuntimeException.class, () -> filmController.addFilm(null));
    }
}