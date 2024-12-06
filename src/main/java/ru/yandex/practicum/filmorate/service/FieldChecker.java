package ru.yandex.practicum.filmorate.service;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class FieldChecker {
    private final int maxDescriptionLength = 200;
    private static final Logger log =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(FilmController.class);

    public void checkFilmField(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.info("Отсутствует название фильма");
            throw new ValidationException("Отсутствует название фильма");
        }

        if (film.getDescription() == null || film.getDescription().isBlank()) {
            log.info("Отсутствует описание фильма");
            throw new ValidationException("Отсутствует описание фильма");
        }

        if (film.getDescription().length() > maxDescriptionLength) {
            log.info("Слишком длинное описание {}", film.getDescription().length());
            throw new ValidationException("Текущее описание "
                    + film.getDescription().length() + " символов. Максимальная длинна 200 символов");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Дата релиза {} не может быть ранее 28 декабря 1895 года", film.getReleaseDate());
            throw new ValidationException("дата релиза "
                    + film.getReleaseDate() + " должна быть не раньше 28 декабря 1895 года");
        }

        if (film.getDuration() == 0) {
            log.info("Продолжительность фильма не может быть нулевой");
            throw new ValidationException("Продолжительность фильма не может быть нулевой");
        }

        if (film.getDuration() < 0) {
            log.info("Продолжительность {} не может отрицательной", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }

    public void checkUserField(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.info("электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.info("логин не может быть пустым и содержать пробелы");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("дата рождения не может быть в будущем");
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }
}
