package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<FilmDto> getAllFilms();

    Optional<FilmDto> getFilmById(Integer film_id);

    void addLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

}
