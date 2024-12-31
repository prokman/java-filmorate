package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.repository.InMemoryUserStorage;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    public FilmService() {
        this.filmStorage = new InMemoryFilmStorage();
        this.userStorage = new InMemoryUserStorage();
    }

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) {
        film.setId(filmStorage.getNextId());
        filmStorage.addFilm(film);
        return film;
    }

    public Film updateFilm(Film newfilm) {
        if (newfilm.getId() == null) {
            throw new ConditionsNotMetException("Id фильма должен быть указан");
        }
        Film existingFilm = filmStorage.getFilmById(newfilm.getId());
        if (existingFilm != null) {
            existingFilm.setName(newfilm.getName());
            existingFilm.setDescription(newfilm.getDescription());
            existingFilm.setDuration(newfilm.getDuration());
            existingFilm.setReleaseDate(newfilm.getReleaseDate());
            filmStorage.addFilm(existingFilm);
            return existingFilm;
        }
        throw new NotFoundException("фильм с id = " + newfilm.getId() + " не найден");
    }

    public Map<Integer, Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().values().stream()
                .sorted(new FilmRateComparator())
                .limit(count)
                .collect(Collectors.toList());
    }

    public void deleteFilmById(Integer id) {

    }

    public void addLike(Integer filmId, Integer userId) {
        if (filmId == null) {
            throw new ConditionsNotMetException("Id фильма должен быть указан");
        }
        if (userId == null) {
            throw new ConditionsNotMetException("Id пользователя должен быть указан");
        }

        if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("фильм с ид " + filmId + " отсутствует");
        }

        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("пользователь с ид " + userId + " отсутствует");
        }
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        if (filmId == null) {
            throw new ConditionsNotMetException("Id фильма должен быть указан");
        }
        if (userId == null) {
            throw new ConditionsNotMetException("Id пользователя должен быть указан");
        }

        if (filmStorage.getFilmById(filmId) == null) {
            throw new NotFoundException("фильм с ид " + filmId + " отсутствует");
        }

        if (userStorage.getUserById(userId) == null) {
            throw new NotFoundException("пользователь с ид " + userId + " отсутствует");
        }
        filmStorage.removeLike(filmId, userId);
    }


}
